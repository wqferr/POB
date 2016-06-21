package core.database;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TileGUI extends JPanel {
	private static final long serialVersionUID = 653126213653L;
	private int i;
	private int j;
	private BufferedImage image;
	
	public void setI(int i){
		this.i = i;
	}
	
	public void setJ(int j){
		this.j = j;
	}
	
	public void setImage(BufferedImage image){
		this.image = image;
	}
	
	public int getI(){
		return this.i;
	}
	
	public int getJ(){
		return this.j;
	}
	
	public BufferedImage getImage(){
		return this.image;
	}
	
	public TileGUI(int i, int j, BufferedImage image, MouseListener mlis){
		super();
		this.setI(i);
		this.setJ(j);
		this.setImage(image);
		this.addMouseListener(mlis);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		ImageIcon icon = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), BufferedImage.SCALE_SMOOTH));
		this.add(new JLabel("", icon, JLabel.CENTER), BorderLayout.CENTER);
	}
	
}
