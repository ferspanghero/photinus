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
 * Cut the extra answers, so we have exactly 20 answers per questions (total 2580)
 * Add the day, month, year to each log line (currently there was only hour,min, sec, millsec.
 * 
 * For each step there is a local folder to save temporary files
 * 
 * @author adrianoc
 *
 */
public class LogReadWriter {

	String path = "C:/firefly/stage/logs/";
	String original = "\\original\\";
	String step0_folder = "\\0.unCookied\\";  //consolidated two workers who removed cookies
	String step1_folder = "\\1.unSmoked\\";
	String step2_folder = "\\2.unQuit\\";
	String step3_folder = "\\3.consolidated\\";
	String step4_folder = "\\4.cut\\";
	String step5_folder = "\\5.missingWorker\\";
	String step6_folder = "\\6.timeStamped\\";

	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param destFileName
	 */
	void writeBackToBuffer(ArrayList<String> newBuffer, int step, String destFileName){

		String folder = "";
		switch(step){
			case 0: folder = this.step0_folder; break;
			case 1: folder = this.step1_folder; break;
			case 2: folder = this.step2_folder; break;
			case 3: folder = this.step3_folder; break;	
			case 4: folder = this.step4_folder; break;
			case 5: folder = this.step5_folder; break;
			case 6: folder = this.step6_folder; break;
			default: folder = null;
		}

		this.writeBackToBuffer(newBuffer, folder, destFileName);
	}


	/** 
	 * Flush the buffer back to the file
	 * @param newBuffer
	 * @param sourceFileName
	 */
	ArrayList<String>  readToBuffer( int step, String sourceFileName){

		String folder = "";
		switch(step){
			case 0: folder = this.original; break;
			case 1: folder = this.step0_folder; break;
			case 2: folder = this.step1_folder; break;
			case 3: folder = this.step2_folder; break;
			case 4: folder = this.step3_folder; break;
			case 5: folder = this.step4_folder; break;
			case 6: folder = this.step5_folder; break;
			default: folder = null;
		}

		return this.readToBuffer(folder, sourceFileName);
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
			case 0: return path + original;
			case 1: return path + step0_folder;
			case 2: return path + step1_folder; 
			case 3: return path + step2_folder; 
			case 4: return path + step3_folder; 
			case 5: return path + step4_folder; 
			case 6: return path + step5_folder; 
			default: return null;
		}
		
	}	

}
