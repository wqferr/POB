package ui.graphic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.database.DatabaseHandler;
import core.database.DatabaseHandler.DataType;
import core.item.Aprimoramento;
import core.item.Item;
import core.item.Pocao;
import core.item.arma.Arco;
import core.item.arma.Cajado;
import core.item.arma.Espada;
import core.item.arma.Livro;
import core.mapa.Mapa;
import core.personagem.Personagem;
import core.personagem.Profissao;

/**
 * Classe com a Interface Grafica para o banco de dados
 * @author wheatley
 *
 */

public class JanelaDatabase extends JFrame implements ActionListener {
	private static final long serialVersionUID = -398592414626114074L;
	
	private JPanel panel;
	private JComboBox<String> tipoDropDown;
	private JComboBox<String> armaDropDown;
	private JComboBox<String> profissoesDropDown;
	private JTextArea databaseText;
	private JTextField textBox[];
	private JFileChooser mapaChooser;
	private JButton adicionarButton;
	private JButton removerButton;
	private DatabaseHandler dataHandler;
	
	/**
	 * Construtor padrão
	 */
	public JanelaDatabase() {
		this("Adicionar Elemento", 500, 600);
	}
	
	/**
	 * Construtor com parâmetros definidos menos o nome do arquivo
	 * @param windowName
	 * @param height
	 * @param width
	 */
	public JanelaDatabase(String windowName, int height, int width) {
		this(windowName, height, width, "db/registry.dat");
	}
	
	/**
	 * Construtor com todos os parâmetros definidos
	 * @param windowName
	 * @param height
	 * @param width
	 * @param fileName
	 */
	public JanelaDatabase(String windowName, int height, int width, String fileName) {
		super(windowName);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(height, width);
		this.panel = (JPanel) this.getContentPane();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.PAGE_AXIS));
		this.panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.panel.setPreferredSize(new Dimension(width, height));
		
		this.tipoDropDown = new JComboBox<String>(new String[]{"Personagem", "Arma", "Poção", "Aprimoramento", "Mapa"});
		this.tipoDropDown.addActionListener(this);
		this.tipoDropDown.setActionCommand("escolhaTipo");
		
		this.adicionarButton = new JButton("Adicionar");
		this.adicionarButton.addActionListener(this);
		this.adicionarButton.setActionCommand("adicionar");
		
		this.removerButton = new JButton(" Remover");
		this.removerButton.addActionListener(this);
		this.removerButton.setActionCommand("remover");
		
		this.armaDropDown = new JComboBox<String>(new String[]{"Espada", "Arco", "Livro", "Cajado"});
		this.profissoesDropDown = new JComboBox<String>(new String[]{"GUERREIRO", "MAGO", "ARQUEIRO", "SACERDOTE"});
		
		this.databaseText = new JTextArea();
		
		this.mapaChooser = new JFileChooser();
		this.mapaChooser.setFileFilter(new FileNameExtensionFilter("*.png, *.bpm", "png", "bpm"));
		this.mapaChooser.setCurrentDirectory(new File ("db/"));
		
		this.textBox = new JTextField[10];
		for (int i=0; i<10; i++) textBox[i] = new JTextField(8);
		
		this.dataHandler = new DatabaseHandler(fileName);
		
		this.panel.add(new JLabel("Tipo de Elemento :"));
		this.panel.add(this.tipoDropDown);
		this.showDatabase();
		this.panel.add(new JLabel("Banco de Dados Atual :"));
		this.panel.add(this.databaseText);
	}
	
	/**
	 * Listener para a Janela
	 */
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("escolhaTipo")) {
			this.panel.removeAll();
			this.panel.add(new JLabel("Tipo de Elemento :"));
			this.panel.add(this.tipoDropDown);
			this.panel.add(new JLabel("Nome :"));
			this.panel.add(this.textBox[0]);
			
			if (this.tipoDropDown.getSelectedItem().equals("Personagem")) {
				this.panel.add(new JLabel("Icone :"));
				this.panel.add(this.textBox[1]);
				this.panel.add(new JLabel("Profissao :"));
				this.panel.add(this.profissoesDropDown);
				this.panel.add(new JLabel("HP :"));
				this.panel.add(this.textBox[2]);
				this.panel.add(new JLabel("Força :"));
				this.panel.add(this.textBox[3]);
				this.panel.add(new JLabel("Dextreza :"));
				this.panel.add(this.textBox[4]);
				this.panel.add(new JLabel("Inteligência :"));
				this.panel.add(this.textBox[5]);
				this.panel.add(new JLabel("Velocidade :"));
				this.panel.add(this.textBox[6]);
			}
			else if (this.tipoDropDown.getSelectedItem().equals("Arma")) {
				this.panel.add(new JLabel("Tipo de Arma :"));
				this.panel.add(this.armaDropDown);
				this.panel.add(new JLabel("Dano :"));
				this.panel.add(this.textBox[1]);
				this.panel.add(new JLabel("Alcance :"));
				this.panel.add(this.textBox[2]);
			}
			else if (this.tipoDropDown.getSelectedItem().equals("Poção")) {
				this.panel.add(new JLabel("Potencia :"));
				this.panel.add(this.textBox[1]);
			}
			else if (this.tipoDropDown.getSelectedItem().equals("Aprimoramento")) {
				this.panel.add(new JLabel("Bônus Força :"));
				this.panel.add(this.textBox[1]);
				this.panel.add(new JLabel("Bônus Dextreza :"));
				this.panel.add(this.textBox[2]);
				this.panel.add(new JLabel("Bônus Inteligência :"));
				this.panel.add(this.textBox[3]);
				this.panel.add(new JLabel("Bônus Velocidade :"));
				this.panel.add(this.textBox[4]);
			}
			else if (this.tipoDropDown.getSelectedItem().equals("Mapa")) {
				this.panel.add(new JLabel("Imagem do mapa"));
				this.panel.add(this.mapaChooser);
			}
			
			this.panel.add(this.adicionarButton);
			this.panel.add(this.removerButton);
			this.panel.add(this.databaseText);
			this.panel.revalidate();
			this.panel.repaint();
		}
		
		else if (event.getActionCommand().equals("adicionar")) {
			boolean valid = true;
			String nome = new String("Invalido");
			if (!(this.textBox[0].getText().equals(""))) {
				try { nome = this.textBox[0].getText(); }
				catch(Exception e) { System.err.println(e); valid = false;}
			}else valid = false;
			
			if (valid) {
				if (this.tipoDropDown.getSelectedItem().equals("Personagem")) {
					Profissao prof = Profissao.GUERREIRO;
					ImageIcon icone = null;
					int hp = 0, forc=0, dext=0, inte=0, vel=0;
					try {
						prof = Profissao.valueOf((String)this.profissoesDropDown.getSelectedItem());
						icone = new ImageIcon(ImageIO.read(new File(this.textBox[1].getText())));
						hp = Integer.parseInt(this.textBox[2].getText());
						forc = Integer.parseInt(this.textBox[3].getText());
						dext = Integer.parseInt(this.textBox[4].getText());
						inte = Integer.parseInt(this.textBox[5].getText());
						vel = Integer.parseInt(this.textBox[6].getText());
					}catch(Exception e) { System.err.println(e);}
	
					try { new Personagem(nome, prof, hp, vel, forc, dext, inte, icone); }
					catch(Exception e) { System.err.println(e); valid = false;}
				}
				else if (this.tipoDropDown.getSelectedItem().equals("Arma")) {
					int dano = 0, alcance = 0;
					try {
						dano = Integer.parseInt(this.textBox[1].getText());
						alcance = Integer.parseInt(this.textBox[2].getText());
					}catch(Exception e) { System.err.println(e); }
					
					try {
						if (((String)this.armaDropDown.getSelectedItem()).equals("Espada"))
							new Espada(nome, dano, alcance);
						else if (((String)this.armaDropDown.getSelectedItem()).equals("Arco"))
							new Arco(nome, dano, alcance);
						else if (((String)this.armaDropDown.getSelectedItem()).equals("Livro"))
							new Livro(nome, dano, alcance);
						else if (((String)this.armaDropDown.getSelectedItem()).equals("Cajado"))
							new Cajado(nome, dano, alcance);
					}catch(Exception e) { System.err.println(e); valid = false;}
				}
				else if (this.tipoDropDown.getSelectedItem().equals("Poção")) {
					int potencia = 0;
					try { potencia = Integer.parseInt(this.textBox[1].getText());
					}catch(Exception e) { System.err.println(e); }
	
					try { new Pocao(nome, potencia); }
					catch(Exception e) { System.err.println(e); valid = false;}
				}
				else if (this.tipoDropDown.getSelectedItem().equals("Aprimoramento")) {
					int bonFor = 0, bonDex = 0, bonInt = 0, bonVel = 0;
					try {
						bonFor = Integer.parseInt(this.textBox[1].getText());
						bonDex = Integer.parseInt(this.textBox[2].getText());
						bonInt = Integer.parseInt(this.textBox[3].getText());
						bonVel = Integer.parseInt(this.textBox[4].getText());
					}catch(Exception e) { System.err.println(e); }
					
					try { new Aprimoramento(nome, bonFor, bonDex, bonInt, bonVel); }
					catch(Exception e) { System.err.println(e); valid = false;}
				}
				else if (this.tipoDropDown.getSelectedItem().equals("Mapa")) {
					File imagem = null;
					try {
						imagem = this.mapaChooser.getSelectedFile();
					}catch(Exception e) { System.err.println(e);}
					
					try { new Mapa(nome, ImageIO.read(imagem)); }
					catch(Exception e) { System.err.println(e); valid = false;}
				}
			}
			
			if (valid) this.dataHandler.writeAllDatabase();
			else System.err.println("Elemento Invalido");
			this.showDatabase();
		}
		
		else if (event.getActionCommand().equals("remover")) {
			String nome = this.textBox[0].getText();
			DataType type;
			if (this.tipoDropDown.getSelectedItem().equals("Personagem")) type = DataType.PERSONAGEM;
			else if (this.tipoDropDown.getSelectedItem().equals("Mapa")) type = DataType.MAPA;
			else type = DataType.ITEM;
			
			this.dataHandler.removeFromDatabase(nome, type);
			this.showDatabase();
		}
	}
	
	private void showDatabase() {
		this.databaseText.setText("");
		Iterator<Entry<String, Item>> itemIt = Item.getIterator();
		Iterator<Entry<String, Personagem>> persIt = Personagem.getIterator();
		Iterator<Entry<String, Mapa>> mapIt = Mapa.getIterator();
	
		while(itemIt.hasNext()) this.databaseText.setText(this.databaseText.getText() + "Item -> " + itemIt.next().getKey() + "\n");
		while(persIt.hasNext()) this.databaseText.setText(this.databaseText.getText() + "Personagem -> " + persIt.next().getKey() + "\n");
		while(mapIt.hasNext()) this.databaseText.setText(this.databaseText.getText() + "Mapa -> " +  mapIt.next().getKey() + "\n");
	}
	
	
}
