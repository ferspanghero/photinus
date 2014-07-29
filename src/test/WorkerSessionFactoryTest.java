package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.CodeSnippet;
import edu.uci.ics.sdcl.firefly.CodeSnippetFactory;
import edu.uci.ics.sdcl.firefly.FileDebugSession;
import edu.uci.ics.sdcl.firefly.Microtask;
import edu.uci.ics.sdcl.firefly.QuestionFactory;
import edu.uci.ics.sdcl.firefly.SourceFileReader;
import edu.uci.ics.sdcl.firefly.WorkerSession;
import edu.uci.ics.sdcl.firefly.WorkerSessionFactory;
import edu.uci.ics.sdcl.firefly.storage.MicrotaskStorage;

public class WorkerSessionFactoryTest {

	HashMap<Integer, Microtask> microtaskMap;
	Stack<WorkerSession> stack;
	
	@Before
	public void setUpDuplicatedStack() throws Exception {
		this.stack = new Stack<WorkerSession>();
		this.microtaskMap = QuestionFactoryMock.generateQuestions();
	
		ArrayList<Microtask> list= new ArrayList<Microtask>();
		list.add(microtaskMap.get(1));
		list.add(microtaskMap.get(2));
		list.add(microtaskMap.get(3));
		list.add(microtaskMap.get(4));
		list.add(microtaskMap.get(5));
		list.add(microtaskMap.get(6));
		list.add(microtaskMap.get(7));
		list.add(microtaskMap.get(8));
		list.add(microtaskMap.get(9));
		list.add(microtaskMap.get(10));
		
		WorkerSession original = new WorkerSession(1,list);
		
		WorkerSession copy1 = new WorkerSession(2,list);
		WorkerSession copy2 = new WorkerSession(3,list);
		
		this.stack.push(copy2);
		this.stack.push(copy1);
		this.stack.push(original);
		
	}
	
	
	@Before
	public void setUpMethodMap() throws Exception {
		this.microtaskMap = QuestionFactoryMock.generateQuestions();

		HashMap<String,HashMap<String,ArrayList<Microtask>>> fileMethodMap = new HashMap<String,HashMap<String,ArrayList<Microtask>>>();
		HashMap<String, ArrayList<Microtask>> methodMap = new HashMap<String, ArrayList<Microtask>>();
		
		String method1 = "TinySample";
		String method2 = "seedCounter";

		ArrayList<Microtask> mtaskList1 = new ArrayList<Microtask>();		
		//first method
		mtaskList1.add(microtaskMap.get(0));
		mtaskList1.add(microtaskMap.get(1));
		mtaskList1.add(microtaskMap.get(2));
		mtaskList1.add(microtaskMap.get(3));
		methodMap.put(method1, mtaskList1);
		
		ArrayList<Microtask> mtaskList2 = new ArrayList<Microtask>();	
		//second method
		mtaskList2.add(microtaskMap.get(4));
		mtaskList2.add(microtaskMap.get(5));
		mtaskList2.add(microtaskMap.get(6));
		mtaskList2.add(microtaskMap.get(7));
		mtaskList2.add(microtaskMap.get(8));
		mtaskList2.add(microtaskMap.get(9));
		mtaskList2.add(microtaskMap.get(10));
		methodMap.put(method2, mtaskList2);
		
		fileMethodMap.put("TinySample.java", methodMap);
		
	}
	

	
	
	
	@Test
	public void testMethodMap(){
		
		String fileName = "TinySample.java";
		String filePath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/samples-discarded/sample/tiny/";
		String fileContent = SourceFileReader.readFileToString(filePath+fileName);
		
		CodeSnippetFactory snippetFactory = new CodeSnippetFactory("TinySample.java", fileContent);
		
		ArrayList<CodeSnippet> list = snippetFactory.generateSnippetsForFile();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data, actual size is: "+list.size());
		else{
			QuestionFactory questionFactory = new QuestionFactory();
			HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(list);
			
			FileDebugSession fileDebuggingSession = new FileDebugSession("TinySample.java",fileContent, microtaskMap);

			//Persist data
			MicrotaskStorage memento = new MicrotaskStorage();
			memento.replace(fileName, fileDebuggingSession);
			
			//Generate the WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
			HashMap<String,HashMap<String,ArrayList<Microtask>>> actualFileMethodMap = sessionFactory.buildMethodMap();
			
			HashMap<String,ArrayList<Microtask>> methodMap = actualFileMethodMap.get("TinySample.java");
			Assert.assertNotNull(methodMap);
			
			ArrayList<Microtask> method1List = methodMap.get("TinySample");
			if(method1List==null || method1List.size()!=4)
				Assert.fail("List of microtasks for method TinySample is wrong, actual size is: "+method1List.size());
			else{
				ArrayList<Microtask> method2List = methodMap.get("seedCounter");
				if(method2List==null || method2List.size()!=7)
					Assert.fail("List of microtasks for method seedCounter is wrong, actual size is: "+method2List.size());
				else{
					Microtask mtask1 = method1List.get(0);
					Assert.assertEquals("First Microtask (ID="+mtask1.getID().toString()+") of TinySample does not match", 0, mtask1.getID().toString().compareTo("0"));
					
					Microtask mtask2 = method2List.get(0);
					Assert.assertEquals("First Microtask (ID="+mtask2.getID().toString()+") of seedCounter does not match", 0, mtask2.getID().toString().compareTo("4"));
					
				}
			}
		}
	}
	
	ArrayList<Microtask> mtaskList0= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList1= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList2= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList3= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList4= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList5= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList6= new ArrayList<Microtask>();
	ArrayList<Microtask> mtaskList7= new ArrayList<Microtask>();
	
	
	@Before
	public void setObtainMicrotaskList() throws Exception {
		this.microtaskMap = QuestionFactoryMock.generateQuestions();

		mtaskList0.add(microtaskMap.get(0));
		mtaskList0.add(microtaskMap.get(4));
		
		mtaskList1.add(microtaskMap.get(1));
		mtaskList1.add(microtaskMap.get(5));
		
		mtaskList2.add(microtaskMap.get(2));
		mtaskList2.add(microtaskMap.get(6));
		
		mtaskList3.add(microtaskMap.get(3));
		mtaskList3.add(microtaskMap.get(7));
					
		mtaskList4.add(microtaskMap.get(8));
		mtaskList5.add(microtaskMap.get(9));
		mtaskList6.add(microtaskMap.get(10));
		
	}
	
	
	@Test
	public void testObtainMicrotaskList(){
		
		String fileName = "TinySample.java";
		String filePath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/samples-discarded/sample/tiny/";
		String fileContent = SourceFileReader.readFileToString(filePath+fileName);
		
		CodeSnippetFactory snippetFactory = new CodeSnippetFactory("TinySample.java", fileContent);
		
		ArrayList<CodeSnippet> list = snippetFactory.generateSnippetsForFile();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data, actual size is: "+list.size());
		else{
			QuestionFactory questionFactory = new QuestionFactory();
			HashMap<Integer, Microtask> microtaskMap = questionFactory.generateQuestions(list);
			
			FileDebugSession fileDebuggingSession = new FileDebugSession("TinySample.java",fileContent, microtaskMap);

			//Persist data
			MicrotaskStorage memento = new MicrotaskStorage();
			memento.replace(fileName, fileDebuggingSession);
			
			//Generate the WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
			HashMap<String,HashMap<String,ArrayList<Microtask>>> actualFileMethodMap = sessionFactory.buildMethodMap();
			
			HashMap<String,ArrayList<Microtask>> methodMap = actualFileMethodMap.get("TinySample.java");
			Assert.assertNotNull(methodMap);
			
			//Obtain a list of N microtasks from N different codesnippets (i.e., methods)
			ArrayList<Microtask> actual = sessionFactory.obtainMicrotaskList(10);
			
			Assert.assertEquals("MicrotasksList 0 does not match", 0, this.compareArrayLists(actual, mtaskList0));
			Assert.assertEquals("MicrotasksList 1 does not match", 0, this.compareArrayLists(actual, mtaskList1));
			Assert.assertEquals("MicrotasksList 2 does not match", 0, this.compareArrayLists(actual, mtaskList2));
			Assert.assertEquals("MicrotasksList 3 does not match", 0, this.compareArrayLists(actual, mtaskList3));
			Assert.assertEquals("MicrotasksList 4 does not match", 0, this.compareArrayLists(actual, mtaskList4));
			Assert.assertEquals("MicrotasksList 5 does not match", 0, this.compareArrayLists(actual, mtaskList5));
			Assert.assertEquals("MicrotasksList 6 does not match", 0, this.compareArrayLists(actual, mtaskList6));
			
		}
	}
	
	private int compareArrayLists(ArrayList<Microtask>actual, ArrayList<Microtask>expected){
		if(actual==null || expected == null || (actual.size() != expected.size()))
			return -1;
		else{
			int areEquivalent = 0; 
			for(int i=0; i< actual.size(); i++){
				Integer actualID = actual.get(i).getID();
				Integer expectedID = expected.get(i).getID();
				if(actualID.intValue()!=expectedID.intValue())
					areEquivalent = -1;
			}
			return areEquivalent;
		}	
			
	}

}
