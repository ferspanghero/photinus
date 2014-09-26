package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;

public class RandomKeyGeneratorTest {

	//@Test
	public void test() {
	
		RandomKeyGenerator generator = new RandomKeyGenerator();
		
		HashMap<String,String> map =  new HashMap<String,String>();
		
		for(double i=0;i<50000;i++){
			String key = generator.generate();
			if(map.containsKey(key)){
				fail("key :" + " was generated more than once!!");
			}
			else
				map.put(key,key);
		}
		System.out.println("Number of keys generated: "+map.size());
	}
	
	@Test
	public void testPosition(){
		ArrayList<String> list = new ArrayList<String>();
		
		list.add("null");
		list.add("eins");
		list.add("zwei");
		list.add("drei");
		
		for(int i=list.size()-1;i>=0;i--){
			int randomPosition = list.size()/2;
			String value = list.get(randomPosition);
			System.out.println("i: "+i+" , randomPosition: "+randomPosition+" value:"+ value);
			list.remove(randomPosition); 
		}
		
		assertTrue(list.isEmpty());
	}

}
