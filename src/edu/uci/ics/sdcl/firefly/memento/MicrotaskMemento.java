package edu.uci.ics.sdcl.firefly.memento;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Microtask;

/** Decouples the persistence mechanism for Microtasks
 * 
 * @author Christian Adriano
 *
 */
public class MicrotaskMemento {

	/** Holds the list of microtasks for all files. 
	 * String key = file name, ArrayList is the list of microtasks*/
	private HashMap<String,ArrayList<Microtask>> debugSessionMicrotaskMap;

	private String persistentFileName = "c:/Users/Christian Adriano/My Documents/microtasks.ser";

	private File file;

	public MicrotaskMemento(){

		try{
			file = new File(this.persistentFileName);
			if(file.exists() && !file.isDirectory()){

				//Try to retrieve an existing file.
				ObjectInputStream objInputStream = new ObjectInputStream( 
						// By using "FileOutputStream" we will 
						// Read it from a File in the file system
						new FileInputStream(file));

				this.debugSessionMicrotaskMap = (HashMap<String,ArrayList<Microtask>>) objInputStream.readObject();

				objInputStream.close();
			}
			// No files has been created yet. 
			else{
				// Create a sample object, that contains the default values.
				this.debugSessionMicrotaskMap = new HashMap<String,ArrayList<Microtask>>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						// By using "FileOutputStream" we will 
						// Write it to a File in the file system
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( debugSessionMicrotaskMap );
				objOutputStream.close();
			}

		}
		catch(IOException exception){
			exception.printStackTrace();
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
	}

	/** Retrieves the list of microtasks for provided file
	 * 
	 * @param fileName
	 * @return
	 */
	public ArrayList<Microtask> read(String fileName){

		try{	
			ObjectInputStream objInputStream = new ObjectInputStream( 
					// By using "FileOutputStream" we will 
					// Read it to a File in the file system
					new FileInputStream(new File(this.persistentFileName)));

			this.debugSessionMicrotaskMap = (HashMap<String,ArrayList<Microtask>>) objInputStream.readObject();
			objInputStream.close();

			return this.debugSessionMicrotaskMap.get(fileName);
		}
		catch(IOException exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return null;
		}
		catch(Exception exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return null;
		}
	}

	public void insert(String key, Microtask microtask){
		//TO DO
	}

	/** Insert a new List of Microtasks. It overwrites any existing one for the same file.
	 * 
	 * @param fileName
	 * @param microtaskList
	 */
	public void insert(String fileName, ArrayList <Microtask> microtaskList){
		try{

			this.debugSessionMicrotaskMap.put(fileName, microtaskList);

			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					// By using "FileOutputStream" we will 
					// Write it to a File in the file system
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( debugSessionMicrotaskMap );
			objOutputStream.close();
		}
		catch(IOException exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
		}
		catch(Exception exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
		}
	}

	public void update(String key, Microtask microtask){
		//TO DO
	}

	public void delete(String key){
		//TO DO
	}

}
