package test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.sun.javafx.collections.MappingChange.Map;

import edu.uci.ics.sdcl.firefly.util.RandomKeyGenerator;

public class RandomKeyGeneratorTest {

	@Test
	public void test() {
	
		RandomKeyGenerator generator = new RandomKeyGenerator(0);
		
		HashMap<String,String> map =  new HashMap<String,String>();
		
		for(double i=0;i<5000000;i++){
			String key = generator.generate();
			if(map.containsKey(key)){
				fail("key :" + " was generated more than once!!");
			}
			else
				map.put(key,key);
		}
		System.out.println("Number of keys generated: "+map.size());
	}
	
	

}
