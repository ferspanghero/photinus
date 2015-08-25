package edu.uci.ics.sdcl.firefly.report.descriptive.demographics;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileConsentDTO;

/**
 * Categorizes the programming language that was manually entered by workers in the survey.
 * @author adrianoc
 *
 */
public class LanguageReport {

	String[] languageCategories = { "Bash/Shell", "Java","C", "C++", "C#","HTML", "Javascript","PHP","Python", "SQL", 
			"VisualBasic", "VB", "Groovy", "Scala", "SAP", "ABAP", "Perl", "Ruby", "Basic"} ;

	String consentFile =  "consent-log-consolidated.txt";
	String path = "C://firefly//topcoder//";

	public static String question[] = {"Gender", "Age", "Country", "Experience", "Language", "YearsProgramming", "Learned"}; 

	public HashMap<String, Integer> langCountMap = new HashMap<String, Integer>();


	//------------------------------------------------------------------------------------------

	public static void main(String[] args){
		LanguageReport report = new LanguageReport();
		report.countProgrammingLanguages();
		report.printCategories();
	}

	public void countProgrammingLanguages(){

		FileConsentDTO consentDTO = new FileConsentDTO();	
		HashMap<String, Worker> workerMap = consentDTO.getWorkers();

		this.langCountMap.put("other", new Integer(0));
		
		for(Worker worker: workerMap.values()){
			String language = worker.getSurveyAnswer(question[4]);
			if(language!=null)
				categorizeLanguage(language);
		}

	}


	private void categorizeLanguage(String lang){

		lang = lang.toLowerCase();
		lang = lang.replace("/",",");
		String[] langList = lang.split(",");

		

		for(String langWord : langList){
		
			boolean notFound =true;//reset
		
			for(String category : languageCategories){
				
				if(langWord.trim().compareTo(category.toLowerCase())==0){
					Integer count = this.langCountMap.get(category);
					if(count==null)
						count = new Integer(0);
					count++;
					this.langCountMap.put(category, count);
					notFound=false;
				}
			}
			
			if(notFound){
				Integer count = this.langCountMap.get("other");
				count++;
				this.langCountMap.put("other", count); 
			}
		}
	}

	public void printCategories(){
		if(this.langCountMap!=null)
			for(String category: this.langCountMap.keySet()){
				System.out.println(category+":"+this.langCountMap.get(category));
			}
	}

}
