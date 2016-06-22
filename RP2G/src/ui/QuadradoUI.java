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
	private static Image grass = null, black = null, generic = null;
	
	public QuadradoUI(Quadrado tile, MouseListener mlis){
		super();
		this.setQuadrado(tile);
		this.setLayout(new GridLayout());
		this.addMouseListener(mlis);
		
		if (QuadradoUI.grass==null){
			try { 
				QuadradoUI.generic = ImageIO.read(new File("db/generic.png"));
				QuadradoUI.grass = ImageIO.read(new File("db/grass.png"));
				QuadradoUI.black = ImageIO.read(new File("db/black.png"));
			}
			catch (Exception e){ System.out.println(e); e.printStackTrace(); }
		}
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
		
		Image image = null;
		if (this.tile.isOcupado()){
			if (this.tile.getOcupante().getIcone()!=null) image = this.tile.getOcupante().getIcone().getImage();
			else image = QuadradoUI.generic;
		}
		else {
			if (this.tile.isTransponivel()) image = QuadradoUI.grass;
			else image = QuadradoUI.black;
		}

		ImageIcon icon = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		this.add(new JLabel("", icon, JLabel.CENTER), BorderLayout.CENTER);
		
	}
	
}
