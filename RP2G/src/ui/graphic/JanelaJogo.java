package ui.graphic;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.client.Cliente;
import net.client.Controlador;
import core.Jogo;
import core.Ordem;
import core.Ordem.Comando;
import core.mapa.Posicao;
import core.personagem.Personagem;

public class JanelaJogo extends JFrame implements ActionListener, MouseListener, Controlador, Consumer<Void> {
	private static final long serialVersionUID = -398592414626114074L;
	
	private JPanel panel;
	private JLabel mensagemPos;
	private JLabel mensagemRodada;
	private JLabel mensagemVez;
	private JButton atacarButton;
	private JButton moverButton;
	private JButton fimButton;
	private JTextArea gameInfo;
	
	private Jogo jogo;
	private QuadradoUI[][] mapaGUI;
	private int curI;
	private int curJ;
	private String curBotao;
	private Cliente client;
	
	/**
	 * Construtor basico
	 * @param jogo
	 */
	public JanelaJogo(Jogo jogo) {
		this(jogo, null);
	}
	
	/**
	 * Construtor com Cliente definido
	 * @param jogo
	 * @param client
	 */
	public JanelaJogo(Jogo jogo, Cliente client) {
		this(jogo, client, "Water Emblem Tactics Online II - Revengence of the Lich King | Game of the Year Edition", 1200, 600);
	}
	
	/**
	 * Construtor completo
	 * @param jogo
	 * @param client
	 * @param windowName
	 * @param height
	 * @param width
	 */
	public JanelaJogo(Jogo jogo, Cliente client, String windowName, int height, int width) {
		super(windowName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(width, height);
		this.curI = 0;
		this.curJ = 0;
		this.curBotao = null;
		this.panel = (JPanel) this.getContentPane();
		this.panel.setLayout(new GridBagLayout());
		this.jogo = jogo;
		this.jogo.setOuvinte(this);
		this.client = client;
		
		this.mensagemVez = new JLabel(this.jogo.getTimeAtual()==this.client.getTime() ? "Sua Vez" : "Vez do Outro");
		this.mensagemRodada = new JLabel(this.jogo.personagemAtual().getNome());
		this.mensagemPos = new JLabel("Posicao: {0, 0}");
		this.mensagemRodada.setPreferredSize(new Dimension(125, 25));
		this.mensagemPos.setPreferredSize(new Dimension(125, 25));
		
		this.atacarButton = this.configureButton("Atacar", "atacar");
		this.moverButton = this.configureButton("Mover", "mover");
		this.fimButton = this.configureButton("Finalizar", "fim");
		
		this.gameInfo = new JTextArea();
	
		GridBagConstraints gcons = new GridBagConstraints();
		gcons.insets = new Insets(0, 0, 0, 0);
		gcons.gridx = 0;
		gcons.gridy = 0;
		this.panel.add(mensagemVez, gcons);
		gcons.gridx = 1;
		this.panel.add(mensagemRodada, gcons);
		gcons.gridx = 0;
		gcons.gridy = 1;
		this.panel.add(mensagemPos, gcons);
		gcons.gridy = 5;
		this.panel.add(atacarButton, gcons);
		gcons.gridx = 1;
		this.panel.add(moverButton, gcons);
		gcons.gridx = 0;
		gcons.gridy = 6;
		this.panel.add(fimButton, gcons);
		gcons.gridy = 7;
		this.panel.add(this.gameInfo);
		
		this.mapaGUI = new QuadradoUI[this.jogo.getMapa().getNLinhas()][this.jogo.getMapa().getNColunas()];
		for (int i=0; i<this.jogo.getMapa().getNLinhas(); i++) {
			for (int j=0; j<this.jogo.getMapa().getNColunas(); j++) {
				mapaGUI[i][j] = new QuadradoUI(jogo.getMapa().getQuadrado(new Posicao(i, j)), this);
				mapaGUI[i][j].setName(i + (" "+ j));
				mapaGUI[i][j].setPreferredSize(new Dimension(40, 40));

				gcons.gridx = j + 10;
				gcons.gridy = i + 10;
				this.panel.add(mapaGUI[i][j], gcons);
			}
		}
		
		//this.pack();
		this.markAllDirty();
		this.updateUI();
	}
	
	/**
	 * Funcao para atualizar a interface de 
	 * forma a manter o que é mostrado na tela 
	 * de acordo com o jogo
	 */
	public void updateUI() {
		this.markAllDirty();
		this.mapaGUI[this.curI][this.curJ].setDirty(true);
		
		this.mensagemRodada.setText(this.jogo.personagemAtual().getNome() + "{" + this.jogo.personagemAtual().getPosicao().getLinha() + ", " + this.jogo.personagemAtual().getPosicao().getColuna() + "}");
		this.mensagemVez.setText(this.jogo.getTimeAtual()==this.client.getTime() ? "Sua Vez" : "Vez do Outro");
		
		this.gameInfo.setText("");
		this.imprimirTime(this.jogo.getPersonagensTime1());
		this.imprimirTime(this.jogo.getPersonagensTime2());
		
		for (int i=0; i<this.jogo.getMapa().getNLinhas(); i++) {
			for (int j=0; j<this.jogo.getMapa().getNColunas(); j++) {
				if (jogo.getMapa().getQuadrado(new Posicao(i, j)).isOcupado()) {
					mapaGUI[i][j].revalidate();
					mapaGUI[i][j].setDirty(true);
				}
			}
		}
		
		this.panel.validate();
		this.panel.paint(this.panel.getGraphics());
		this.panel.revalidate();
		this.panel.repaint();
		this.markAllDirty();
	}
	
	/**
	 * Listener para a janela
	 */
	public void actionPerformed(ActionEvent e) {	
		if (e.getActionCommand().equals("atacar")) this.curBotao = new String("atacar");
		else if(e.getActionCommand().equals("mover")) this.curBotao = new String("mover");
		else if (e.getActionCommand().equals("fim")) this.curBotao = new String("fim");
		else this.curBotao = null;
	}
	
	/**
	 * Listener para as posicoes do tabuleiro
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		this.curI = ((QuadradoUI)e.getSource()).getQuadrado().getPosicao().getLinha();
		this.curJ = ((QuadradoUI)e.getSource()).getQuadrado().getPosicao().getColuna();
		this.mensagemPos.setText(("Posicao: {" + this.curI + ", ") + (this.curJ + "}"));
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Funcao que retorna a proxima ordem ao jogo
	 */
	@Override
	public Ordem proximaOrdem(Jogo j) {
		try{ while (this.curBotao==null) Thread.sleep(10); }
		catch(Exception e) { e.printStackTrace(); }
		
		Ordem order = null;
		if (this.curBotao.equals("atacar")) order = new Ordem(Comando.ATACAR, new Posicao(this.curI, this.curJ));
		else if (this.curBotao.equals("mover")) order = new Ordem(Comando.MOVER, new Posicao(this.curI, this.curJ));
		else if (this.curBotao.equals("fim")) order = new Ordem(Comando.ENCERRAR);
		
		this.curBotao = null;
		return order;
	}
	
	/**
	 * Fucao que atualiza a Interface quando houver mudanças no jogo
	 */
	@Override
	public void accept(Void t) {
		this.updateUI();
	}
	
	
	private JButton configureButton(String buttonName, String actionName) {
		JButton button = new JButton(buttonName);
		button.setActionCommand(actionName);
		button.addActionListener(this);
		button.setPreferredSize(new Dimension(125, 25));
		
		return button;
	}
	
	private void markAllDirty() {
		for(Personagem p : this.jogo.getPersonagensTime1())
			if(p!=null) this.mapaGUI[p.getPosicao().getLinha()][p.getPosicao().getColuna()].setDirty(true);
		
		for(Personagem p : this.jogo.getPersonagensTime2())
			if(p!=null) this.mapaGUI[p.getPosicao().getLinha()][p.getPosicao().getColuna()].setDirty(true);
	}
	
	private void imprimirTime(List<Personagem> time){
		Iterator<Personagem> it = time.iterator();
		while (it.hasNext()) this.gameInfo.append(it.next().toString());
		this.gameInfo.append("\n\n");
	}
	
}
