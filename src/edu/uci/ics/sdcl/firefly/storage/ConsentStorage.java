package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.FileDebugSession;

public class ConsentStorage {
	private String persistentFileName = "consent.ser";

	public MicrotaskStorage(){

		try{
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String,FileDebugSession> debugSessionMap = new HashMap<String,FileDebugSession>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( debugSessionMap );
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

}
