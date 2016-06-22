package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.client.Controlador;
import core.Jogo;
import core.mapa.Posicao;
import core.Ordem;
import core.Ordem.Comando;

public class JanelaJogo extends JFrame implements ActionListener, MouseListener, Controlador {
	private static final long serialVersionUID = -398592414626114074L;
	
	private JPanel panel;
	private Jogo jogo;
	private QuadradoUI[][] mapaGUI;
	private JLabel mensagemPos;
	private JLabel mensagemRodada;
	private JButton atacarButton;
	private JButton moverButton;
	private JButton fimButton;
	private int curI;
	private int curJ;
	private String curBotao;
	
	public JanelaJogo(Jogo jogo){
		this(jogo, "Water Emblem Tactics Online II - Revengence of the Lich King | Game of the Year Edition", 800, 60);
	}
	
	public JanelaJogo(Jogo jogo, String windowName, int height, int width){
		super(windowName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(height, width);
		this.curI = 0;
		this.curJ = 0;
		this.curBotao = null;
		this.panel = (JPanel) this.getContentPane();
		this.panel.setLayout(new GridBagLayout());
		this.jogo = jogo;
		
		this.mensagemRodada = new JLabel(this.jogo.personagemAtual().getNome());
		this.mensagemPos = new JLabel("Posicao: {0, 0}");
		this.mensagemRodada.setPreferredSize(new Dimension(125, 25));
		this.mensagemPos.setPreferredSize(new Dimension(125, 25));
		
		this.atacarButton = new JButton("Atacar");
		this.moverButton = new JButton("Mover");
		this.fimButton = new JButton("Terminar");
		this.atacarButton.addActionListener(this);
		this.moverButton.addActionListener(this);
		this.fimButton.addActionListener(this);
		this.atacarButton.setPreferredSize(new Dimension(125, 25));
		this.moverButton.setPreferredSize(new Dimension(125, 25));
		this.fimButton.setPreferredSize(new Dimension(125, 25));
		this.atacarButton.setActionCommand("atacar");
		this.moverButton.setActionCommand("mover");
		this.fimButton.setActionCommand("fim");
		
		GridBagConstraints gcons = new GridBagConstraints();
		gcons.insets = new Insets(0, 0, 0, 0);
		gcons.gridx = 0;
		gcons.gridy = 0;
		this.panel.add(mensagemRodada, gcons);
		gcons.gridy = 1;
		this.panel.add(mensagemPos, gcons);
		gcons.gridy = 5;
		this.panel.add(atacarButton, gcons);
		gcons.gridx = 1;
		this.panel.add(moverButton, gcons);
		gcons.gridx = 0;
		gcons.gridy = 6;
		this.panel.add(fimButton, gcons);
		
		this.mapaGUI = new QuadradoUI[this.jogo.getMapa().getNLinhas()][this.jogo.getMapa().getNColunas()];
		for (int i=0; i<this.jogo.getMapa().getNLinhas(); i++){
			for (int j=0; j<this.jogo.getMapa().getNColunas(); j++){
				mapaGUI[i][j] = new QuadradoUI(jogo.getMapa().getQuadrado(new Posicao(i, j)), this);
				mapaGUI[i][j].setName(i + (" "+ j));
				mapaGUI[i][j].setPreferredSize(new Dimension(40, 40));

				gcons.gridx = j + 10;
				gcons.gridy = i + 10;
				this.panel.add(mapaGUI[i][j], gcons);
			}
		}
	}
	
	public void updateUI(){
		this.mensagemRodada.setText(this.jogo.personagemAtual().getNome());
		for (int i=0; i<this.jogo.getMapa().getNLinhas(); i++){
			for (int j=0; j<this.jogo.getMapa().getNColunas(); j++){
				if (mapaGUI[i][j].getQuadrado().getOcupante()!=null || jogo.getMapa().getQuadrado(new Posicao(i, j)).getOcupante()!=null){
					mapaGUI[i][j].setQuadrado(jogo.getMapa().getQuadrado(new Posicao(i, j)));
					mapaGUI[i][j].revalidate();
				}
			}
		}
		
		this.panel.validate();
		this.panel.paint(this.panel.getGraphics());
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getActionCommand().equals("atacar")) {
			if (this.jogo.atacar(new Posicao(this.curI, this.curJ))){
				this.curBotao = new String("atacar");
				this.updateUI();
				this.jogo.exibir();
			}
		}
		else if(e.getActionCommand().equals("mover")){
			if (this.jogo.mover(new Posicao(this.curI, this.curJ))){
				this.curBotao = new String("mover");
				this.updateUI();
				this.jogo.exibir();
			}
		}
		else if (e.getActionCommand().equals("fim") && !jogo.acabou()){
			this.curBotao = new String("fim");
			this.jogo.proximoPersonagem();
			this.updateUI();
			this.jogo.exibir();
		}
	}
	

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

	@Override
	public Ordem proximaOrdem(Jogo j) {
		try{
			while (this.curBotao==null) Thread.sleep(10);
			if (this.curBotao.equals("atacar")) return new Ordem(Comando.ATACAR, new Posicao(this.curI, this.curJ));
			else if (this.curBotao.equals("mover")) return new Ordem(Comando.MOVER, new Posicao(this.curI, this.curJ));
			else if (this.curBotao.equals("fim")) return new Ordem(Comando.ENCERRAR);
			
			this.curBotao = null;
		}catch(Exception e) { e.printStackTrace(); }
		
		return null;
	}
	
}
