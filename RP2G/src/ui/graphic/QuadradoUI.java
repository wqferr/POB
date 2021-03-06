package ui.graphic;

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

/**
 * Classe que armazena um dos quadrados do tabuleiro para a interface gráfica
 *
 */

public class QuadradoUI extends JPanel {
	private static final long serialVersionUID = 653126213653L;
	private Quadrado tile;
	private static BufferedImage grass = null, black = null, generic = null;
	private boolean dirty;
	
	/**
	 * Construtor padrão
	 * @param tile
	 * @param mlis
	 */
	public QuadradoUI(Quadrado tile, MouseListener mlis) {
		super();
		this.setQuadrado(tile);
		this.setLayout(new GridLayout());
		this.addMouseListener(mlis);
		this.dirty = true;
		
		if (QuadradoUI.grass==null) {
			try { 
				QuadradoUI.generic = ImageIO.read(new File("db/generic.png"));
				QuadradoUI.grass = ImageIO.read(new File("db/grass.png"));
				QuadradoUI.black = ImageIO.read(new File("db/black.png"));
			}
			catch (Exception e) { System.out.println(e); e.printStackTrace(); }
		}
	}
	
	/**
	 * Setter para o quadrado da que o objeto esta tratando
	 * @param tile
	 */
	public void setQuadrado(Quadrado tile) {
		this.tile = tile;
	}
	
	/**
	 * Getter para o Quadrado
	 * @return
	 */
	public Quadrado getQuadrado() {
		return this.tile;
	}
	
	/**
	 * Setter que define que o quadrado precisa ser redesenhado
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	/**
	 * Getter que retorna se o quadrado precisa ser redesenhado
	 * @return
	 */
	public boolean getDirty() {
		return this.dirty;
	}
	
	/**
	 * Override da funcao desenhar o quadrado
	 */
	public void paint(Graphics g) {
		super.paint(g);
		if (this.dirty) {
			this.removeAll();
			
			BufferedImage image = null;
			if (this.tile.isOcupado()) {
				if (this.tile.getOcupante().getIcone()!=null) {
					image = new BufferedImage(this.tile.getOcupante().getIcone().getIconWidth(), this.tile.getOcupante().getIcone().getIconHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics gr = image.createGraphics();
					this.tile.getOcupante().getIcone().paintIcon(null, gr, 0,0);
					gr.dispose();
				}
				else image = QuadradoUI.generic;
			}
			else {
				if (this.tile.isTransponivel()) image = QuadradoUI.grass;
				else image = QuadradoUI.black;
			}
			
			this.dirty = false;
			ImageIcon icon = new ImageIcon(image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
			this.add(new JLabel("", icon, JLabel.CENTER), BorderLayout.CENTER);
		}
	}
	
}
