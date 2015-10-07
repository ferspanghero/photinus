package edu.uci.ics.sdcl.firefly.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import edu.uci.ics.sdcl.firefly.Microtask;

public class PathUtil {
	
	/**	
	 * 	Remove the path and optionally the extension of a file name with path and extension
	 * 
	 * 	@param 	String that includes the path, the file name and the extension (or not) of this file
	 * 			Boolean that set the option for removing(true) or not(false) the file extension
	 * 	@return The new String without the path and optionally without the extension
	 * 
	 * 	@author Danilo Cardoso
	 */
	public static String removePath(String pathNameExtension, boolean alsoRemoveExtension){
		while (pathNameExtension.contains("\\") || pathNameExtension.contains("/")){	// for Windows and Linux base systems
			if (pathNameExtension.contains("\\"))
				pathNameExtension = pathNameExtension.substring(pathNameExtension.indexOf("\\")+1);
			else
				pathNameExtension = pathNameExtension.substring(pathNameExtension.indexOf("/")+1);
		}
		String nameExtension = pathNameExtension;	// no path anymore
		if (alsoRemoveExtension && nameExtension.contains(".")){
			String name = nameExtension.substring(0, nameExtension.indexOf("."));
			return name;
		}
		return nameExtension;
	}
	
	/**	
	 * 	Convert a micrtotask Map into a Map which key is the name of the Method and Value is a ArrayList
	 * 	of all its microtasks.
	 * 
	 * 	@param 	HashMap<Integer, Microtask> where key is the microtask ID and value is the microtask
	 * 	@return HashMap<String, ArrayList<Microtask>> where the key is the method name and value its microtasks
	 * 
	 * 	@author Danilo Cardoso
	 */
	public static HashMap<String, ArrayList<Microtask>> convertToMicrotasksPerMethod(HashMap<Integer, Microtask> microtasksMap){
		HashMap<String, ArrayList<Microtask>> microtasksPerMethod = new HashMap<String, ArrayList<Microtask>>();
		// iterating over the microtask's Map
		Set<Map.Entry<Integer, Microtask>> set = microtasksMap.entrySet();
		Iterator<Entry<Integer, Microtask>> i = set.iterator();
		while(i.hasNext())
		{	
			Map.Entry<Integer, Microtask> me = (Map.Entry<Integer, Microtask>)i.next();
			// populating the new map
			String methodName = me.getValue().getCodeSnippet().getMethodSignature().getName();
			if (microtasksPerMethod.containsKey(methodName)){
				microtasksPerMethod.get(methodName).add(me.getValue());
			} else {
				ArrayList<Microtask> microtaksForThisMethod = new ArrayList<Microtask>();
				microtaksForThisMethod.add(me.getValue());
				microtasksPerMethod.put(methodName, microtaksForThisMethod);
			}
		}
		return microtasksPerMethod;
	}

}
