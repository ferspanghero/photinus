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
	
	private ArrayList<HashMap<String, Microtask>> debugSessionMicrotasks;
	
	private String persistentFileName = "microtasks.ser";
	
	public MicrotaskMemento(){
	
		try{
			
		//Try to retrieve an existing file.
		ObjectInputStream objInputStream = new ObjectInputStream( 
                 // By using "FileOutputStream" we will 
                 // Read it to a File in the file system
                 new FileInputStream(new File(this.persistentFileName)));
		
		debugSessionMicrotasks = (ArrayList<HashMap<String, Microtask>>) objInputStream.readObject();
		
		if(this.debugSessionMicrotasks!=null){
			((ObjectOutput) objInputStream).writeObject( debugSessionMicrotasks );
			objInputStream.close();
		
		}
		// No files has been created yet. 
        else{
			// Create a sample object, that contains the default values.
			this.debugSessionMicrotasks = new ArrayList<HashMap<String, Microtask>>();

	        ObjectOutputStream objOutputStream = new ObjectOutputStream( 
	                               // By using "FileOutputStream" we will 
	                               // Write it to a File in the file system
	                                new FileOutputStream(new File(this.persistentFileName)));

	        objOutputStream.writeObject( debugSessionMicrotasks );
	        objOutputStream.close();
		}
		
		}
		catch(IOException exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
		}
		catch(Exception exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
		}
	}
	
	public Microtask read(String key){
		//TO DO
		return null;
	}
	
	public void insert(String key, Microtask microtask){
		//TO DO
	}
	
	public void insert(ArrayList <Microtask> microtaskList){
		//TO DO
	}
	
	public void update(String key, Microtask microtask){
		//TO DO
	}
	
	public void delete(String key){
		//TO DO
	}

}
