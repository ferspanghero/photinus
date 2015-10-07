package test;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

 




import org.junit.Before;
import org.junit.Test;

public class SampleEntryTest {

	Map<String, Counter> countsFixed=new TreeMap<String, Counter>();
	
	Map<String, Counter> countsBuggy=new TreeMap<String, Counter>();
	
	
	private  class Counter {
		int counter=1;
		String item;
		public Counter(String item) {
			this.item=item;
		}
	}
	private String convertFixed(Map<String, Counter> counts) {
		StringBuilder sb = new StringBuilder(); 
		boolean isFirstElement = true;
		for (Entry<String,Counter> type : counts.entrySet()) {
			if (isFirstElement) {
				isFirstElement = false;
			} else {
				sb.append(", ");
			}

			int count = type.getValue().counter;
			sb.append(count);
			sb.append(" ");
			sb.append(type.getValue().item);
		}
		return sb.toString();
	}
	
	
	private String convertBuggy(Map<String, Counter> counts) {
		StringBuilder sb = new StringBuilder(); 
		boolean isFirstElement = true;
		for (Entry<String,Counter> type : counts.entrySet()) {
			if (isFirstElement) {
				isFirstElement = false;
			} else {
				sb.append(", ");
			}

			//int count
			Counter counter =counts.get(type); 
			//int count = type.getValue().counter;
			sb.append(counter.counter);
			sb.append(" ");
			sb.append(counts.get(type).item);
			//sb.append(type.getValue().item);
		}
		return sb.toString();
	}
	
	@Before
	public void setUpFix() throws Exception {
		 
		String key = "orange";
		this.countsFixed.put(key, new Counter(key));
		this.countsFixed.get(key).counter++;
		
		key = "banana";
		this.countsFixed.put(key, new Counter(key));
		this.countsFixed.get(key).counter++;
		
		key = "apple";
		this.countsFixed.put(key, new Counter(key));
		this.countsFixed.get(key).counter++;
		
		key = "orange";
		this.countsBuggy.put(key, new Counter(key));
	//	this.countsBuggy.get(key).counter++;
		
		key = "banana";
		this.countsBuggy.put(key, new Counter(key));
	//	this.countsBuggy.get(key).counter++;
		
		key = "apple";
		this.countsBuggy.put(key, new Counter(key));
	//	this.countsBuggy.get(key).counter++;
	
	}
	
	
	 
	
	@Test
	public void test() {
		
		String bufferFixed = this.convertFixed(this.countsFixed);
		String bufferBuggy = this.convertBuggy(this.countsBuggy);
		int compare = bufferFixed.compareTo(bufferBuggy);
		assertTrue(compare==0);
		
		System.out.println("bufferFixed: "+bufferFixed);
		System.out.println("bufferBuggy: "+bufferBuggy);
	}

}
