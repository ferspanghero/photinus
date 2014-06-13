package test;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;

public class MethodSignatureTest{

	MethodSignature signatureOne;
	MethodSignature signatureTwo;
	MethodSignature signatureThree;
	
	@Before
	public void setUp() throws Exception {
		signatureOne = new MethodSignature("factorial", "public", new Integer (12));
		signatureOne.addMethodParameters(new MethodParameter("Integer", "Seed"));
		signatureOne.addMethodParameters(new MethodParameter("Integer", "Iterations"));
		
		signatureTwo = new MethodSignature("factorial", "public", new Integer (12));
		signatureTwo.addMethodParameters(new MethodParameter("Integer","Seed"));
		signatureTwo.addMethodParameters(new MethodParameter("Integer","Iterations"));
		
		signatureThree = new MethodSignature("SimpleSampleCode", "public", new Integer (7));
	}

	@Test
	public void testCompareMethod_equals() {
		org.junit.Assert.assertTrue(signatureOne.isEqualTo(signatureTwo));
	}
	
	@Test
	public void testCompareMethod_distinct(){
		org.junit.Assert.assertFalse(signatureOne.isEqualTo(signatureThree));
	}

}
