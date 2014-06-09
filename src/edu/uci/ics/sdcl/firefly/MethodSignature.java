package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;

public class MethodSignature {

	protected String Name;
	protected String Modifier;
	protected ArrayList<MethodParameter> ParameterList;
	protected Integer LineNumber;

	public MethodSignature(String name, String modifier, Integer lineNumber) {
		Name = name;
		Modifier = modifier;
		LineNumber = lineNumber;
		this.ParameterList = new ArrayList<MethodParameter>();
	}

	/**
	 * Compare the current method against a provided one.
	 * @return true is methods are the same (name and parameters match), otherwise false
	 */
	public boolean isEqualTo(MethodSignature target){
		boolean matched = true;
		
		if(target.getName().compareTo(this.Name)==0){
			ArrayList<MethodParameter> targetList = target.getParameterList();
			if(targetList.size() == this.ParameterList.size()){
				int i = 0;
				int j = 0;
				while((i<ParameterList.size() && j<targetList.size()) && matched){
					MethodParameter sourceParam = this.ParameterList.get(i);
					MethodParameter targetParam = targetList.get(j);
					if(!sourceParam.isEqualTo(targetParam)){
						matched=false;
					}
					else{
						i++;
						j++;
					}	
				}
			}
			else 
				return false;
			}
		else
			return false;
		
		return matched;
	}

	public void addMethodParameters(MethodParameter parameter){
		this.ParameterList.add(parameter);
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getModifier() {
		return Modifier;
	}

	public void setModifier(String modifier) {
		Modifier = modifier;
	}

	public ArrayList<MethodParameter> getParameterList() {
		return ParameterList;
	}

	public void setParameterList(ArrayList<MethodParameter> parameterList) {
		ParameterList = parameterList;
	}

	public Integer getLineNumber() {
		return LineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		LineNumber = lineNumber;
	}
	
	public boolean hasParameters(){
		if(this.ParameterList.size()>0)
			return true;
		else
			return false;
	}
 
}
