package edu.uci.ics.sdcl.firefly.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.ScreeningTest;

public class ConsentStorage {
	private String persistentFileName = "consent.ser";

	public ConsentStorage() {
		try{
			File file = new File(this.persistentFileName);
			if(!file.exists() ||  file.isDirectory()){
				// No files has been created yet. 

				// Create a sample object, that contains the default values.
				HashMap<String, ScreeningTest> consentMap = new HashMap<String, ScreeningTest>();

				ObjectOutputStream objOutputStream = new ObjectOutputStream( 
						new FileOutputStream(new File(this.persistentFileName)));

				objOutputStream.writeObject( consentMap );
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
	
	public boolean insert(String userId, ScreeningTest subjectTest){

		HashMap<String, ScreeningTest> consentMap = this.retrieveIndex();

		if(consentMap!=null){
			consentMap.put(userId, subjectTest);
			return this.updateIndex(consentMap);	
		}		
		else
			return false;
	}
	
	public ScreeningTest read(String userId){

		HashMap<String, ScreeningTest> consentMap = this.retrieveIndex();

		if(consentMap!=null && consentMap.containsKey(userId))
			return consentMap.get(userId);
		else
			return null;
	}
	
	public boolean remove(String userId) {

		HashMap<String, ScreeningTest> consentMap = this.retrieveIndex();

		if(consentMap!=null && !consentMap.isEmpty()){
			consentMap.remove(userId);
			return this.updateIndex(consentMap);				
		}		
		else
			return false;
	}
	
	private boolean updateIndex(HashMap<String, ScreeningTest> consentMap){
		try{
			ObjectOutputStream objOutputStream = new ObjectOutputStream( 
					new FileOutputStream(new File(this.persistentFileName)));

			objOutputStream.writeObject( consentMap );
			objOutputStream.close();
			return true;
		}
		catch(IOException exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return false;
		}
		catch(Exception exception){
			System.err.print("Error while opening microtasks serialized file:" + exception.toString());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, ScreeningTest> retrieveIndex(){
		try{
			HashMap<String,ScreeningTest> consentMap;
			ObjectInputStream objInputStream = new ObjectInputStream( 
					new FileInputStream(new File(this.persistentFileName)));

			consentMap = (HashMap<String, ScreeningTest>) objInputStream.readObject();

			objInputStream.close();
			return consentMap;
		}
		catch(IOException exception){
			exception.printStackTrace();
			return null;
		}
		catch(Exception exception){
			exception.printStackTrace();
			return null;
		}
	}
	

}
