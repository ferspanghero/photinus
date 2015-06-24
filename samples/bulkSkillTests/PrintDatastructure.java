package pretask.test;
import java.util.*;

public class PrintDatastructure {

	public static void main(String[] args){
		
		HashMap<String, String[]> rootMap =  new HashMap<String, String[]>();
		String[] list1 = { "A","B"} ;
		String[] list2 = {};
		
		rootMap.put("0", list1);
		rootMap.put("1", null);
		rootMap.put("2", list2);
		
		printData(rootMap);
		
	}
	
	public static void printData(HashMap<String, String[]> map){
		
		Iterator<String> keySet = map.keySet().iterator();
		while(keySet.hasNext()){
			String key =  keySet.next();
			System.out.print(key);
			String[] list = map.get(key);
			if((list==null) || list.length==0)
				System.out.print("!");
			//else	
				for(int i= list.length-1;i>=0;i--)
					System.out.print(list[i]);
		}
		System.out.println(";");		
	}
}
