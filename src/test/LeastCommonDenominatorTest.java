package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.util.LeastCommonDenominator;

public class LeastCommonDenominatorTest {

	ArrayList<Integer> list;
	
	@Before
	public void setup(){
		list= new ArrayList<Integer>();
		list.add(6);
		list.add(12);
		list.add(48);
	}
	
	@Test
	public void testLeastCommonMultiplier() {
		
		long common = LeastCommonDenominator.lcm(list.toArray());
		assertEquals(48, common);
	}


	@Test
	public void testGreatesCommonDivisor() {
		long common = LeastCommonDenominator.gcd(list.toArray());
		assertEquals(6, common);
	}
	

}
