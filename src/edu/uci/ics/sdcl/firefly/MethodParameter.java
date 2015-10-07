package edu.uci.ics.sdcl.firefly;

import java.io.Serializable;

public class MethodParameter implements Serializable {

	private String TypeName;
	private String Name;
	
	public MethodParameter(String typeName, String name) {
		super();
		TypeName = typeName;
		Name = name;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}
		
	public boolean isEqualTo(MethodParameter param) {
		if(this.TypeName.compareTo(param.getTypeName())!=0 ){
			return false;
		}
		else
			return true;
	}
	
}
