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

public class DatabaseHandler {
	private String fileName;
	
	public DatabaseHandler(){
		this("db/registry.dat");
	}
	
	public DatabaseHandler(String fileName){
		this.fileName = fileName;
		this.readAllDatabase();
	}
	
	public String getFileName(){
		return this.fileName;
	}
	
	public void setFileName(String str){
		this.fileName = str;
	}
	
	public void readAllDatabase(){
		FileInputStream fileInStream = null;
		try{ fileInStream = new FileInputStream(this.fileName); }
		catch (FileNotFoundException notFound){
			try{
				File newFile = new File(fileName);
				newFile.createNewFile();
				fileInStream = new FileInputStream(this.fileName);
			}
			catch(Exception e){ System.err.println(e);}
		}
		catch(Exception e){ System.err.println(e);}
		
		ObjectInputStream objectInStream = null;
		try{ objectInStream  = new ObjectInputStream(fileInStream); }
		catch (EOFException eof) {}
		catch(Exception e){ System.err.println(e);}
		
		DatabaseHandler.readAllStream(objectInStream);
		
		try{ objectInStream.close(); }
		catch (NullPointerException nullP){}
		catch(Exception e){ System.err.println(e); }
	}
	
	public void writeToDatabase(Object obj){
		FileOutputStream fileOutStream = null;
		ObjectOutputStream objectOutStream = null;
		try{
			fileOutStream  = new FileOutputStream(this.fileName);
			objectOutStream = new ObjectOutputStream(fileOutStream);
		}catch(Exception e){ System.err.println(e); }
		
		DatabaseHandler.writeToStream(objectOutStream);
		
		try { objectOutStream.close(); }
		catch(Exception e){ System.err.println(e); }
	}
	
	public static void readAllStream(ObjectInputStream objectInStream){
		Object obj = null;
		boolean notEOF = true;
		
		int size = 0, i=0;
		try { size = (Integer)objectInStream.readObject(); }
		catch (EOFException eof) { obj = null; notEOF = false; }
		catch (NullPointerException nullP){ obj = null; notEOF = false; }
		catch (Exception e) { System.err.println(e);}
		System.out.println(size);
		while (i<size && notEOF){
			try { obj = objectInStream.readObject(); }
			catch (EOFException eof) { obj = null; notEOF = false; }
			catch (NullPointerException nullP){ obj = null; notEOF = false; }
			catch (Exception e) { System.err.println(e);}
			
			if (obj != null){
				try{
					if (obj instanceof Personagem){
						Personagem pers = (Personagem) obj;
						Personagem.add(pers);
						System.out.println("Lido : " + ((Personagem)obj).getNome());
					}	
					else if (obj instanceof Item){
						Item item = (Item) obj;
						Item.add(item);
						System.out.println("Lido : " + ((Item)obj).getNome());
					}
					else if (obj instanceof Mapa){
						Mapa mapa = (Mapa) obj;
						Mapa.add(mapa);
						System.out.println("Lido : " + ((Mapa)obj).getNome());
					}
				}
				catch(Exception e){ System.err.println(e); }
			}
			else notEOF = false;
			i++;
		}
	}
	
	public static void writeToStream(ObjectOutputStream objectOutStream){
		List<Object> allData = new LinkedList<Object>();
		Iterator<Entry<String, Item>> itemIt = Item.getIterator();
		Iterator<Entry<String, Personagem>> persIt = Personagem.getIterator();
		Iterator<Entry<String, Mapa>> mapIt = Mapa.getIterator();
		
		while(itemIt.hasNext()) allData.add(Item.get(itemIt.next().getKey()));
		while(persIt.hasNext()) allData.add(Personagem.get(persIt.next().getKey()));
		while(mapIt.hasNext()) allData.add(Mapa.get(mapIt.next().getKey()));
		allData.add(0, allData.size());
		
		try{
			Iterator<Object> it = allData.iterator();
			while(it.hasNext()) objectOutStream.writeObject(it.next());
		}catch(Exception e){ System.err.println(e); }
	}
}
