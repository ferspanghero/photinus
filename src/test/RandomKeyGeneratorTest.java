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
	
	//@Test
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

	@Test
	public void removeReturns(){
	
		String test1= "\n\rlinha1\n\rlinha2\n\r";
		System.out.println(test1);
		test1 = test1.replaceAll("[\r\n]", " ");
		System.out.println("..................");
		System.out.println(test1);
		
		String test2= "\n linha1\n linha2 \n";
		System.out.println(test2);
		test2 = test2.replaceAll("[\n]", " ");
		System.out.println("..................");
		System.out.println(test2);

		String test3= "\r\n linha1\r\n linha2 \r\n";
		System.out.println(test3);
		test3 = test3.replaceAll("[\r]", " ");
		System.out.println("..................");
		System.out.println(test3);
	}
	
}
