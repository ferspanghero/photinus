package edu.uci.ics.sdcl.firefly.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyManager {

	public String serializationPath;

	public String reportPath;

	private String fileName="firefly.properties";

	private String devPropertyPath= "C:/firefly/";

	private String serverPropertyPath ="./"; //It will be in the same place as the Webapp deployed. 

	public PropertyManager(){

		String OS = System.getProperty("os.name").toLowerCase();

		if(this.isDevelopmentHost(OS)){
			readDevelopmentProperties();
		}
		else
			if(isServerHost(OS)){
				readServerProperties();
			}
			else{
				Properties properties = new Properties();
				this.reportPath = properties.getProperty("reportPath");
				this.reportPath = properties.getProperty("repositoryPath");
			}	
	}
	
	private void readDevelopmentProperties(){
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(this.devPropertyPath));
			this.reportPath = properties.getProperty("developmentSerializationPath");
			this.serializationPath = properties.getProperty("developmentSerializationPath");
		} 
		catch (IOException e) {
			System.out.println("Could not load properties from path: "+this.devPropertyPath);
		}
	}
	
	private void readServerProperties(){
		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(this.serverPropertyPath));
			this.reportPath = properties.getProperty("serverSerializationPath");
			this.serializationPath = properties.getProperty("serverSerializationPath");
		} 
		catch (IOException e) {
			System.out.println("Could not load properties from path: "+this.devPropertyPath);
		}
	}

	private boolean isDevelopmentHost(String OS) {	 
		return (OS.indexOf("win") >= 0);
	}

	private boolean isServerHost(String OS) {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

}
