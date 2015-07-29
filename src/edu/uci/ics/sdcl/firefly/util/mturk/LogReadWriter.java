package edu.uci.ics.sdcl.firefly.util.mturk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/** Reads and writes from the existing session and consent logs.
 * It is used while cleaning and consolidating logs
 * 
 * It supports a three step processing:
 * Remove SmokeTest entries
 * Rename the SessionIDs from workers who quit
 * Replace workerIDs with consolidated ones
 * 
 * For each step there is a local folder to save temporary files
 * 
 * @author adrianoc
 *
 */
public class LogReadWriter {

	String path = "C:/firefly/stage/logs/";
	String original = "\\0.original\\";
	String step1_folder = "\\1.unSmoked\\";
	String step2_folder = "\\2.unQuit\\";
	String step3_folder = "\\3.consolidate\\";

	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param destFileName
	 */
	void writeBackToBuffer(ArrayList<String> newBuffer, int step, String destFileName){

		String folder = "";
		switch(step){
			case 1: folder = this.step1_folder; break;
			case 2: folder = this.step2_folder; break;
			case 3: folder = this.step3_folder; break;
			default: folder = null;
		}

		this.writeBackToBuffer(newBuffer, folder, destFileName);
	}


	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param destFileName
	 */
	ArrayList<String>  readToBuffer( int step, String destFileName){

		String folder = "";
		switch(step){
			case 1: folder = this.original; break;
			case 2: folder = this.step1_folder; break;
			case 3: folder = this.step2_folder; break;
			default: folder = null;
		}

		return this.readToBuffer(folder, destFileName);
	}


	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param destFileName
	 */
	private void writeBackToBuffer(ArrayList<String> newBuffer, String folder, String destFileName){

		String destination = path + folder + destFileName;
		BufferedWriter log;
		try {
			log = new BufferedWriter(new FileWriter(destination));
			for(String line : newBuffer)
				log.write(line+"\n");
			log.close();
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + destination);
			e.printStackTrace();
		}
	}

	/** Load all the file into a StringBuffer */
	private ArrayList<String> readToBuffer(String folder, String sessionLog){

		ArrayList<String> buffer = new ArrayList<String>();

		BufferedReader log;
		try {
			log = new BufferedReader(new FileReader(path +folder+ sessionLog));
			String line = null;
			while ((line = log.readLine()) != null) {
				buffer.add(line);
			}
			log.close();
			return buffer;
		} 
		catch (Exception e) {
			System.out.println("ERROR while processing file:" + path+folder+sessionLog);
			e.printStackTrace();
			return null;
		}
	}


	/**
	 *  Returns a reading path depending no 
	 *  @param step the step of processing 
	 * */
	public String getPath(int step) {

		switch(step){
			case 1: return path + original;
			case 2: return path + step1_folder; 
			case 3: return path + step2_folder; 
			default: return null;
		}
		
	}	

}
