package test;

import static org.junit.Assert.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Logger logger = LoggerFactory.getLogger(LeastCommonDenominatorTest.class);
		logger.info("testing logger");
		long common = LeastCommonDenominator.lcm(list.toArray());
		assertEquals(48, common);
	}


	@Test
	public void testGreatesCommonDivisor() {
		long common = LeastCommonDenominator.gcd(list.toArray());
		assertEquals(6, common);
	}
	

}
