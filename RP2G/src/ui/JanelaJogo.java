package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Jogo;
import core.mapa.Posicao;

public class JanelaJogo extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = -398592414626114074L;
	
	private JPanel panel;
	private Jogo jogo;
	private QuadradoUI[][] mapaGUI;
	
	public JanelaJogo(Jogo jogo){
		this(jogo, "Water Emblem Tactics Online II - Revengence of the Lich King", 700, 600);
	}
	
	public JanelaJogo(Jogo jogo, String windowName, int height, int width){
		super(windowName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(height, width);
		this.panel = (JPanel) this.getContentPane();
		
		this.panel.setLayout(new GridBagLayout());

		this.jogo = jogo;
		this.mapaGUI = new QuadradoUI[this.jogo.getMapa().getNLinhas()][this.jogo.getMapa().getNColunas()];
		GridBagConstraints gcons = new GridBagConstraints();
		gcons.insets = new Insets(0, 0, 0, 0);
		for (int i=0; i<this.jogo.getMapa().getNLinhas(); i++){
			for (int j=0; j<this.jogo.getMapa().getNColunas(); j++){
				mapaGUI[i][j] = new QuadradoUI(jogo.getMapa().getQuadrado(new Posicao(i, j)), this);
				mapaGUI[i][j].setName((""+i) + (" "+ j));
				mapaGUI[i][j].setPreferredSize(new Dimension(40, 40));
				
				gcons.gridx = i;
				gcons.gridy = j;
				this.panel.add(mapaGUI[i][j], gcons);
			}
		}
	}
	
	public void actionPerformed(ActionEvent event){
		
	}
	

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(((JPanel)e.getSource()).getName());
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

}
