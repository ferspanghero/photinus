package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;
import java.util.Vector;

public class MethodSignature implements Serializable{

	protected String Name;
	protected String Modifier;							//Visibility
	protected Vector<MethodParameter> ParameterList;


	public MethodSignature(String name, String modifier) {
		Name = name;
		Modifier = modifier;
		this.ParameterList = new Vector<MethodParameter>();
	}

	/**
	 * Compare the current method against a provided one.
	 * @return true is methods are the same (name and parameters match), otherwise false
	 */
	public boolean isEqualTo(MethodSignature target){
		boolean matched = true;
		
		if(target.getName().compareTo(this.Name)==0){
			Vector<MethodParameter> targetList = target.getParameterList();
			if(targetList.size() == this.ParameterList.size()){
				int i = 0;
				int j = 0;
				while((i<ParameterList.size() && j<targetList.size()) && matched){
					MethodParameter sourceParam = this.ParameterList.get(i);
					MethodParameter targetParam = targetList.get(j);
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

	public Vector<MethodParameter> getParameterList() {
		return ParameterList;
	}

	public void setParameterList(Vector<MethodParameter> parameters) {
		ParameterList = parameters;
	}

	public boolean hasParameters(){
		if(this.ParameterList.size()>0)
			return true;
		else
			return false;
	}
	
	@Override
	public String toString()
	{
		StringBuffer methodSignature = new StringBuffer();
		methodSignature.append(Modifier);
		methodSignature.append(" ");
		methodSignature.append(Name);
		methodSignature.append("(");
		for (MethodParameter parameter : ParameterList)
		{
			methodSignature.append(parameter.getTypeName());
			methodSignature.append(" ");
			methodSignature.append(parameter.getName());
			if (parameter != ParameterList.get(ParameterList.size()-1) ) // if it is not the last one...
				methodSignature.append(", ");
		}		
		return methodSignature.toString();
	}
 
}
