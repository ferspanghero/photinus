package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.util.MethodMatcher;

public class MethodMatcherTest {

	//Rejectable
	ArrayList<String> rejectList = new ArrayList<String>();

	//Acceptable
	ArrayList<String> acceptList = new ArrayList<String>();

	//Method name to be found
	String original = "Code";


	@Before
	public void setup(){

		rejectList.add("MyCode");
		rejectList.add("CodeCode");
		rejectList.add("_Code");
		rejectList.add("1Code");
		rejectList.add("0Code");
		rejectList.add("9Code");
		rejectList.add("Code.");
		rejectList.add("'Code");
		rejectList.add("Code'");
		
		

		acceptList.add(" Code");
		acceptList.add("	Code"); //a tab before the method call.
		acceptList.add("Code ");
		acceptList.add("Code	"); //a tab after
		acceptList.add("Code(");
		acceptList.add(".Code");
		acceptList.add(")Code");
		acceptList.add("=Code");
		acceptList.add("+Code");
		acceptList.add("-Code");
		acceptList.add("*Code");
		acceptList.add("/Code");
		acceptList.add("?Code");
		acceptList.add("&Code");
		acceptList.add("^Code");
		acceptList.add("!Code");
		acceptList.add(";Code");
		acceptList.add("]Code");
		acceptList.add("[Code");
		acceptList.add("}Code");
		acceptList.add("{Code");
		acceptList.add("|Code");
		acceptList.add("~Code");

	}


	@Test
	public void testRejectable() {

		for(String methodName: rejectList){
			boolean isDifferentMethod = MethodMatcher.containsDifferentMethod(methodName, original);	
			Assert.assertTrue("methodName: "+ methodName+" shouldn't contain the method "+original, isDifferentMethod);
		}
	}



	@Test
	public void testAcceptable() {

		for(String methodName: acceptList){
			boolean isDifferentMethod = MethodMatcher.containsDifferentMethod(methodName, original);
			Assert.assertFalse("methodName: "+ methodName+" should contain the method "+original, isDifferentMethod);
		}
	}

}


