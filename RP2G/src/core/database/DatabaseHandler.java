package core.database;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import core.item.Item;
import core.mapa.Mapa;
import core.personagem.Personagem;

/**
 * Classe para gerenciar a base de dados de Itens, Mapas e Personagens.
 */
public class DatabaseHandler {
	private String fileName;
	
	/**
	 * Constrói um novo gerenciador usando o arquivo db/registry\.dat.
	 */
	public DatabaseHandler() {
		this("db/registry.dat");
	}
	
	/**
	 * Constrói um novo gerenciador especificando qual arquivo deve ser manipulado.
	 * @param fileName O nome do arquivo.
	 */
	public DatabaseHandler(String fileName) {
		this.fileName = fileName;
		this.readAllDatabase();
	}
	
	/**
	 * Retorna o nome do arquivo que este gerenciador manipula.
	 * @return O nome do arquivo.
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Altera o arquivo a ser manipulado.
	 * @param str O nome do novo arquivo.
	 */
	public void setFileName(String str) {
		this.fileName = str;
	}
	
	/**
	 * Lê o arquivo binário e atualiza os registros de Item, Personagem e Mapa.
	 */
	public void readAllDatabase() {
		FileInputStream fileInStream = null;
		try { fileInStream = new FileInputStream(this.fileName); }
		catch (FileNotFoundException notFound) {
			try {
				File newFile = new File(fileName);
				newFile.createNewFile();
				fileInStream = new FileInputStream(this.fileName);
			}
			catch(Exception e) { System.err.println(e);}
		}
		catch(Exception e) { System.err.println(e);}
		
		ObjectInputStream objectInStream = null;
		try { objectInStream  = new ObjectInputStream(fileInStream); }
		catch (EOFException eof)  {}
		catch(Exception e) { System.err.println(e);}
		
		DatabaseHandler.readAllStream(objectInStream);
		
		try { objectInStream.close(); }
		catch (NullPointerException nullP) {}
		catch(Exception e) { System.err.println(e); }
	}
	
	/**
	 * Adiciona um objeto ao arquivo binário.
	 * @param obj O objeto a ser adicionado.
	 */
	public void writeToDatabase(Object obj) {
		FileOutputStream fileOutStream = null;
		ObjectOutputStream objectOutStream = null;
		try {
			fileOutStream  = new FileOutputStream(this.fileName);
			objectOutStream = new ObjectOutputStream(fileOutStream);
		}catch(Exception e) { System.err.println(e); }
		
		DatabaseHandler.writeToStream(objectOutStream);
		
		try  { objectOutStream.close(); }
		catch(Exception e) { System.err.println(e); }
	}
	
	/**
	 * Lê os objetos enviados por uma stream e atualiza a base de dados.
	 * O primeiro objeto lido deve ser um Integer, cujo valor é o número de
	 * objetos que seguem.
	 * @param objectInStream A stream a ser lida.
	 */
	public static void readAllStream(ObjectInputStream objectInStream) {
		Object obj = null;
		boolean notEOF = true;
		
		int size = 0, i=0;
		try { size = (Integer)objectInStream.readObject(); }
		catch (EOFException eof) { obj = null; notEOF = false; }
		catch (NullPointerException nullP){ obj = null; notEOF = false; }
		catch (Exception e) { System.err.println(e);}
		while (i<size && notEOF) {
			try { obj = objectInStream.readObject(); }
			catch (EOFException eof) { obj = null; notEOF = false; }
			catch (NullPointerException nullP) { obj = null; notEOF = false; }
			catch (Exception e)  { System.err.println(e);}
			
			if (obj != null) {
				try {
					if (obj instanceof Personagem) {
						Personagem pers = (Personagem) obj;
						Personagem.add(pers);
						System.out.println("Lido : " + ((Personagem)obj).getNome());
					}	
					else if (obj instanceof Item) {
						Item item = (Item) obj;
						Item.add(item);
						System.out.println("Lido : " + ((Item)obj).getNome());
					}
					else if (obj instanceof Mapa) {
						Mapa mapa = (Mapa) obj;
						Mapa.add(mapa);
						System.out.println("Lido : " + ((Mapa)obj).getNome());
					}
				}
				catch(Exception e) { System.err.println(e); }
			}
			else notEOF = false;
			i++;
		}
	}
	
	/**
	 * Escreve a base de dados inteira para uma stream dada.
	 * O primeiro objeto enviado é um inteiro representando o número de objetos
	 * a serem transmitidos, seguido por todos os registros encontrados na
	 * base de dados.
	 * @param objectOutStream A stream de saída.
	 */
	public static void writeToStream(ObjectOutputStream objectOutStream) {
		List<Object> allData = new LinkedList<Object>();
		Iterator<Entry<String, Item>> itemIt = Item.getIterator();
		Iterator<Entry<String, Personagem>> persIt = Personagem.getIterator();
		Iterator<Entry<String, Mapa>> mapIt = Mapa.getIterator();
		
		while(itemIt.hasNext()) allData.add(Item.get(itemIt.next().getKey()));
		while(persIt.hasNext()) allData.add(Personagem.get(persIt.next().getKey()));
		while(mapIt.hasNext()) allData.add(Mapa.get(mapIt.next().getKey()));
		allData.add(0, allData.size());
		
		try {
			Iterator<Object> it = allData.iterator();
			while(it.hasNext()) objectOutStream.writeObject(it.next());
		}catch(Exception e) { System.err.println(e); }
	}
}
