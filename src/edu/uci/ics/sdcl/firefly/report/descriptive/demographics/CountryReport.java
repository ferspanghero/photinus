package edu.uci.ics.sdcl.firefly.report.descriptive.demographics;

import java.util.HashMap;

import edu.uci.ics.sdcl.firefly.Worker;
import edu.uci.ics.sdcl.firefly.report.descriptive.FileConsentDTO;

/**
 * Categorizes the countries that were manually entered by workers in the survey.
 * @author adrianoc
 *
 */
public class CountryReport {

	String[] countryCategories = { "America", "US", "U.S.", "USA", "United States","United States of America", "U.S.A.", "UK", "England", "India", "Sri Lanka", "Srilanka", 
			"Brazil", "Italy", "South Africa", "Poland", "China", "New Zealand", "Latvia", "Canada", "Germany", "Romania", "Turkey",
			"Serbia", "Macedonia", "Australia", "Colombia", "El Salvador", "Costa Rica", "Bulgaria", "Portugal", "Philipphines", "Pakistan", "Croatia",
			"Indonesia"
	} ;

	String consentFile =  "consent-log-consolidated.txt";
	String path = "C://firefly//topcoder//";

	public static String question[] = {"Gender", "Age", "Country", "Experience", "Language", "YearsProgramming", "Learned"}; 

	public HashMap<String, Integer> countryCountMap = new HashMap<String, Integer>();


	//------------------------------------------------------------------------------------------

	public static void main(String[] args){
		CountryReport report = new CountryReport();
		report.countCountries();
		report.printCategories();
	}

	public void countCountries(){

		FileConsentDTO consentDTO = new FileConsentDTO(path+consentFile);	
		HashMap<String, Worker> workerMap = consentDTO.getWorkers();

		this.countryCountMap.put("misclassified", new Integer(0));
		int i=1;
		for(Worker worker: workerMap.values()){
			String country = worker.getSurveyAnswer(question[2]);
			if(country!=null){
				categorizeCountry(country);
				i++;
			}
		}

		System.out.println("i:"+i);
	}


	private void categorizeCountry(String lang){

		lang = lang.toLowerCase();
		//lang = lang.replace("/",",");
		String[] langList = lang.split(",");

		

		for(String langWord : langList){
		
			boolean notFound =true;//reset
		
			for(String category : countryCategories){
				
				if(langWord.trim().compareTo(category.toLowerCase())==0){
					Integer count = this.countryCountMap.get(category);
					if(count==null)
						count = new Integer(0);
					count++;
					this.countryCountMap.put(category, count);
					notFound=false;
				}
			}
			
			if(notFound){
				Integer count = this.countryCountMap.get("misclassified");
				count++;
				this.countryCountMap.put("misclassified", count); 
			}
		}
	}

	public void printCategories(){
		if(this.countryCountMap!=null)
			for(String category: this.countryCountMap.keySet()){
				System.out.println(category+":"+this.countryCountMap.get(category));
			}
	}

}
