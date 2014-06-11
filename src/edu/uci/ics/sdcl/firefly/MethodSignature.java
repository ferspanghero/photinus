package edu.uci.ics.sdcl.firefly;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class MethodSignature {

	protected String Name;
	protected String Modifier;							//Visibility 
	protected ArrayList<SingleVariableDeclaration> ParameterList;
	protected Integer LineNumber;

	public MethodSignature(String name, String modifier, Integer lineNumber) {
		Name = name;
		Modifier = modifier;
		LineNumber = lineNumber;
		this.ParameterList = new ArrayList<SingleVariableDeclaration>();
	}

	/**
	 * Compare the current method against a provided one.
	 * @return true is methods are the same (name and parameters match), otherwise false
	 */
	public boolean isEqualTo(MethodSignature target){
		boolean matched = true;
		
		if(target.getName().compareTo(this.Name)==0){
			ArrayList<SingleVariableDeclaration> targetList = target.getParameterList();
			if(targetList.size() == this.ParameterList.size()){
				int i = 0;
				int j = 0;
				while((i<ParameterList.size() && j<targetList.size()) && matched){
					SingleVariableDeclaration sourceParam = this.ParameterList.get(i);
					SingleVariableDeclaration targetParam = targetList.get(j);
					if(!sourceParam.getName().toString().
							equalsIgnoreCase(targetParam.getName().toString()))
					{
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

	public void addMethodParameters(SingleVariableDeclaration parameter){
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

	public ArrayList<SingleVariableDeclaration> getParameterList() {
		return ParameterList;
	}

	public void setParameterList(ArrayList<SingleVariableDeclaration> parameterList) {
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
