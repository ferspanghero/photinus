package edu.uci.ics.sdcl.firefly.util;

import java.util.regex.Pattern;

public class MethodMatcher {

	public static boolean containsDifferentMethod(String line , String methodName){

		boolean isDifferentMethod = false;
		
		if(line.contains(methodName)){
			if(line.length()>0){
				int index = line.indexOf(methodName);
				if (index>0){//checking the character before the method name
					String prefix = String.valueOf(line.charAt(index-1));
					isDifferentMethod = Pattern.matches("[_a-zA-Z0-9]",prefix);
					/*if(isDifferentMethod)
						System.out.println("Line: "+ line+" contains a method different from "+methodName);
					else
						System.out.println("Line: "+ line+" contains same method as "+methodName);*/
				}
				else
					if(line.length()>(index+methodName.length())){
						String suffix = String.valueOf(line.charAt(index+methodName.length()));
						isDifferentMethod = Pattern.matches("[._a-zA-Z0-9]",suffix);
						/*if(isDifferentMethod)
							System.out.println("Line: "+ line+" contains a method different from "+methodName);
						else
							System.out.println("Line: "+ line+" contains same method as "+methodName);*/
					}
					else{
						//System.out.println("Line: "+ line+" contains same method as "+methodName);
						isDifferentMethod=true;
					}
			}
		}

		return isDifferentMethod;
	}



}
