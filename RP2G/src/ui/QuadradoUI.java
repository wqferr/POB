package ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.mapa.Quadrado;

public class QuadradoUI extends JPanel {
	private static final long serialVersionUID = 653126213653L;
	private Quadrado tile;
	
	public QuadradoUI(Quadrado tile, MouseListener mlis){
		super();
		this.setQuadrado(tile);
		this.setLayout(new GridLayout());
		this.addMouseListener(mlis);
	}
	
	public void setQuadrado(Quadrado tile){
		this.tile = tile;
	}
	
	public Quadrado getQuadrado(){
		return this.tile;
	}
	
	public void paint(Graphics g){
		super.paint(g);
		this.removeAll();
		Image grass = null, black = null, generic = null;
		try { 
			generic = ImageIO.read(new File("db/generic.png"));
			grass = ImageIO.read(new File("db/grass.png"));
			black = ImageIO.read(new File("db/black.png"));
		}
		catch (Exception e){ System.out.println(e); e.printStackTrace(); }
		
		Image image = null;
		if (this.tile.isOcupado()){
			if (this.tile.getOcupante().getIcone()!=null) image = this.tile.getOcupante().getIcone().getImage();
			else image = generic;
		}
		else {
			if (this.tile.isTransponivel()) image = grass;
			else image = black;
		}

		ImageIcon icon = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		this.add(new JLabel("", icon, JLabel.CENTER), BorderLayout.CENTER);
		
	}
	
}
