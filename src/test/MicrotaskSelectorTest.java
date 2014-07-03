package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.Answer;
import edu.uci.ics.sdcl.firefly.CodeElement;
import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.MethodParameter;
import edu.uci.ics.sdcl.firefly.MethodSignature;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.business.MicrotaskSelector;
import edu.uci.ics.sdcl.firefly.memento.MicrotaskMemento;

public class MicrotaskSelectorTest {

	private  HashMap<String, FileDebugSession> debugSessionMap = new HashMap<String, FileDebugSession>();
	private HashMap<Integer, Microtask> microtaskMap;
	MicrotaskMemento memento = new MicrotaskMemento();
	MicrotaskSelector selector = new MicrotaskSelector();
	String fileName = "SimpleSampleCode.java";

	@Before
	public void setUp() throws Exception {
		MethodSignature signature = new MethodSignature("factorial", "public", new Integer(1));
		MethodParameter arg1, arg2;
		arg1 = new MethodParameter("Integer", "Seed");
		arg2 = new MethodParameter("Integer", "Iterations");
		signature.addMethodParameters(arg1);
		signature.addMethodParameters(arg2);

		StringBuffer buffer = new StringBuffer("public Integer factorial(Integer Seed, Integer Iterations){");
		buffer.append("\n");
		buffer.append("if(Seed!=null){");
		buffer.append("\n");
		buffer.append("int aux=1;");
		buffer.append("\n");
		buffer.append("for (int i=0;i<Iterations.intValue();i++){");
		buffer.append("\n");
		buffer.append("aux =  aux * Seed;");
		buffer.append("\n");
		buffer.append("}");
		buffer.append("\n");
		buffer.append("return new Integer(aux);");
		buffer.append("\n");
		buffer.append("}");
		buffer.append("\n");
		buffer.append("else return null;");
		buffer.append("\n");
		buffer.append("}");

		String questionArg1 = "Is there maybe something wrong in the declaration of function 'factorial' at line 1 " 
				+ "(e.g., requires a parameter that is not listed, needs different parameters to produce the correct result, specifies the wrong or no return type, etc .)?";

		String questionArg2 = "Is it possible that the conditional clause at line 2 has "
				+ "problems (e.g., wrong Boolean operator, wrong comparison, misplaced parentheses, etc.)?";

		String questionArg3 = "Is there maybe something wrong with the '<L>-loop' construct at line 4 "
				+ "(e.g., incorrect initialization, wrong counter increment, wrong exit condition, etc.)?";

		CodeSnippet codeSnippetFactorial=new CodeSnippet("sample","SimpleSampleCode", buffer.toString(), new Integer (1),
				new Boolean (true), signature);

		Microtask mtask1 = new Microtask(CodeElement.METHOD_DECLARARION, codeSnippetFactorial, questionArg1, new Integer(1));
		Microtask mtask2 = new Microtask(CodeElement.IF_CONDITIONAL, codeSnippetFactorial, questionArg2, new Integer(2));
		Microtask mtask3 = new Microtask(CodeElement.FOR_LOOP, codeSnippetFactorial, questionArg3, new Integer(4));

		//Create the data structure
		this.microtaskMap =  new HashMap<Integer,Microtask>();
		microtaskMap.put(new Integer(1),mtask1);
		microtaskMap.put(new Integer(2),mtask2);
		microtaskMap.put(new Integer(3),mtask3);
	
		FileDebugSession debugSession = new FileDebugSession(fileName,microtaskMap);
		this.debugSessionMap.put(fileName, debugSession);

		//Persist microtasks
		memento.insert(fileName, debugSession); 
	}



	//Test if it is incrementing
	@Test
	public void testIncrement() {	

		 FileDebugSession debugSession = memento.read(fileName);
		 if((debugSession!=null) && (debugSession.getMicrotaskMap()!=null)){
			HashMap<Integer, Microtask> mMap = debugSession.getMicrotaskMap();
			//Associate an answer
			Integer key = new Integer (1);
			Microtask mtask1 = mMap.get(key);
			ArrayList<Answer> answerList = mtask1.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.YES));
			mtask1.setAnswer(answerList);

			debugSession.incrementAnswersReceived(answerList.size());

			int max = debugSession.getMaximumAnswerCount();

			//Check whether it actually incremented
			assertEquals(1, max);
		}
		else
			fail("Not yet implemented");
	}

	//Check whether the increment is taking effect
	@Test
	public void testGetFirst() {
		//Associate answers to two microtasks
		 FileDebugSession debugSession = memento.read(fileName);
		 if((debugSession!=null) && (debugSession.getMicrotaskMap()!=null)){
			HashMap<Integer, Microtask> mMap = debugSession.getMicrotaskMap();
			Integer key = new Integer (1);
			Microtask mtask1 = mMap.get(key);
			ArrayList<Answer> answerList = mtask1.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.YES));
			mtask1.setAnswer(answerList);
			mMap.put(key, mtask1);
			debugSession.incrementAnswersReceived(answerList.size());
			
			key = new Integer (2);
			Microtask mtask2 = mMap.get(key);
			answerList = mtask2.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.NO));
			mtask2.setAnswer(answerList);
			debugSession.incrementAnswersReceived(answerList.size());
			
			//Persist data back
			mMap.put(new Integer (1), mtask1);
			mMap.put(new Integer (2), mtask2);
			debugSession.setMicrotaskMap(mMap);
			
			memento.insert(fileName, debugSession);
						
			Microtask mtask3 = this.selector.selectMicrotask(fileName);
			
			assertEquals(null,mtask3.getAnswerList());

			//Cleans up.
			//memento.insert(fileName, debugSession);
		}	
		else{
			fail("Not yet implemented");
		}
	}

	@Test
	public void testGetAllPlusOne(){
		//Associate answers to two microtasks
		 FileDebugSession debugSession = memento.read(fileName);
		 if((debugSession!=null) && (debugSession.getMicrotaskMap()!=null)){
			HashMap<Integer, Microtask> mMap = debugSession.getMicrotaskMap();
			Integer key = new Integer (1);
			Microtask mtask1 = mMap.get(key);
			ArrayList<Answer> answerList = mtask1.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.YES));
			mtask1.setAnswer(answerList);
			mMap.put(key, mtask1);
			debugSession.incrementAnswersReceived(answerList.size());
			
			key = new Integer (2);
			Microtask mtask2 = mMap.get(key);
			answerList = mtask2.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.NO));
			mtask2.setAnswer(answerList);
			debugSession.incrementAnswersReceived(answerList.size());
			
			key = new Integer (3);
			Microtask mtask3 = mMap.get(key);
			answerList = mtask3.getAnswerList();
			if(answerList==null) answerList= new ArrayList<Answer>();
			answerList.add(new Answer(Answer.NO));
			answerList.add(new Answer(Answer.NO));
			mtask3.setAnswer(answerList);
			debugSession.incrementAnswersReceived(answerList.size());
			
			//Persist data back
			mMap.put(new Integer (1), mtask1);
			mMap.put(new Integer (2), mtask2);
			mMap.put(new Integer (3), mtask3);
			debugSession.setMicrotaskMap(mMap); 
			
			memento.insert(fileName, debugSession);
						
			Microtask mtaskActual1 = this.selector.selectMicrotask(fileName);
			
			assertEquals(1,mtaskActual1.getAnswerList().size());
			
			//Cleans up.
			//memento.insert(fileName, null);

		}
		else{
			fail("Not yet implemented");
		}
	}



}
