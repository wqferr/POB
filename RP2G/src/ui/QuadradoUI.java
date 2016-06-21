package ui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.mapa.Quadrado;

public class QuadradoUI extends JPanel {
	private static final long serialVersionUID = 653126213653L;
	private Quadrado tile;
	
	public void setQuadrado(Quadrado tile){
		this.tile = tile;
	}
	
	public Quadrado getQuadrado(){
		return this.tile;
	}
	
	public QuadradoUI(Quadrado tile, MouseListener mlis){
		super();
		this.setQuadrado(tile);
		this.addMouseListener(mlis);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		Image image = null;
		if (this.tile.isOcupado()){
			if (this.tile.getOcupante().getIcone()!=null) image = this.tile.getOcupante().getIcone().getImage();
			else {
				try { image = ImageIO.read(new File("db/generic.png")); }
				catch (Exception e){ System.out.println(e); e.printStackTrace(); }
			}
		}
		else {
			try { 
				if (this.tile.isTransponivel()) image = ImageIO.read(new File("db/grass.png"));
				else image = ImageIO.read(new File("db/black.png"));
			}
			catch (Exception e){ System.out.println(e); e.printStackTrace(); }
		}
		
		ImageIcon icon = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		this.add(new JLabel("", icon, JLabel.CENTER), BorderLayout.CENTER);
		
	}
	
}
