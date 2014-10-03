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

	private String path = ".";
	
	private HashMap<Integer, Microtask> microtaskMap =  new HashMap<Integer, Microtask>();

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
			 questionFactory.generateMicrotasks(list, "Bug report message", 0);
			HashMap<Integer, Microtask> microtaskMap = questionFactory.getConcreteQuestions();

			FileDebugSession fileDebuggingSession = new FileDebugSession("TinySample.java",fileContent, microtaskMap);

			//Persist data
			MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();;
			memento.insert(fileName, fileDebuggingSession);

			//Generate the WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory(10);
			HashMap<String,HashMap<String,ArrayList<Microtask>>> actualFileMethodMap = sessionFactory.getFileMethodMap();
			

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

	//------------------------------------------------------------------------------------------------------------------

	//Object attributes to test the nextMicrotaskList()
	private ArrayList<Microtask> mtaskList0= new ArrayList<Microtask>();
	private	ArrayList<Microtask> mtaskList1= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList2= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList3= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList4= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList5= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList6= new ArrayList<Microtask>();
	private ArrayList<Microtask> mtaskList7= new ArrayList<Microtask>();


	@Before
	public void setUpObtainMicrotaskList() throws Exception {
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
			questionFactory.generateMicrotasks(list, "Bug report message", 0);
			HashMap<Integer, Microtask> microtaskMap = questionFactory.getConcreteQuestions(); 
			FileDebugSession fileDebuggingSession = new FileDebugSession("TinySample.java",fileContent, microtaskMap);

			//Persist data
			MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();;
			memento.insert(fileName, fileDebuggingSession);

			//Generate the WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory(10);
			HashMap<String,HashMap<String,ArrayList<Microtask>>> actualFileMethodMap = sessionFactory.getFileMethodMap();

			HashMap<String,ArrayList<Microtask>> methodMap = actualFileMethodMap.get("TinySample.java");
			Assert.assertNotNull(methodMap);

			//Obtain a list of N microtasks from N different codesnippets (i.e., methods)
			ArrayList<Microtask> actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 0 does not match", 0, this.compareArrayLists(actual, mtaskList0));

			actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 1 does not match", 0, this.compareArrayLists(actual, mtaskList1));

			actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 2 does not match", 0, this.compareArrayLists(actual, mtaskList2));

			actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 3 does not match", 0, this.compareArrayLists(actual, mtaskList3));

			actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 4 does not match", 0, this.compareArrayLists(actual, mtaskList4));

			actual = sessionFactory.nextMicrotaskList(10);
			Assert.assertEquals("MicrotasksList 5 does not match", 0, this.compareArrayLists(actual, mtaskList5));

			actual = sessionFactory.nextMicrotaskList(10);
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

	//------------------------------------------------------------------------------------------------------------------

	private HashMap<Integer, Microtask> mtaskMap = new HashMap<Integer, Microtask>();
	private Stack<WorkerSession> expectedStack = new Stack<WorkerSession>();
	

	@Before
	public void setUpGenerateSessions() throws Exception {
		this.expectedStack = new Stack<WorkerSession>();
		this.mtaskMap = QuestionFactoryMock.generateQuestions();

		ArrayList<Microtask> mtaskList0= new ArrayList<Microtask>();
		mtaskList0.add(mtaskMap.get(0));
		mtaskList0.add(mtaskMap.get(4));

		ArrayList<Microtask> mtaskList1= new ArrayList<Microtask>();
		mtaskList1.add(mtaskMap.get(1));
		mtaskList1.add(mtaskMap.get(5));

		ArrayList<Microtask> mtaskList2= new ArrayList<Microtask>();	
		mtaskList2.add(mtaskMap.get(2));
		mtaskList2.add(mtaskMap.get(6));

		ArrayList<Microtask> mtaskList3= new ArrayList<Microtask>();	
		mtaskList3.add(mtaskMap.get(3));
		mtaskList3.add(mtaskMap.get(7));

		ArrayList<Microtask> mtaskList4= new ArrayList<Microtask>();		
		mtaskList4.add(mtaskMap.get(8));

		ArrayList<Microtask> mtaskList5= new ArrayList<Microtask>();	
		mtaskList5.add(mtaskMap.get(9));

		ArrayList<Microtask> mtaskList6= new ArrayList<Microtask>();	
		mtaskList6.add(mtaskMap.get(10));

		WorkerSession original0 = new WorkerSession("0",mtaskList0);
		WorkerSession original1 = new WorkerSession("1",mtaskList1);
		WorkerSession original2 = new WorkerSession("2",mtaskList2);
		WorkerSession original3 = new WorkerSession("3",mtaskList3);
		WorkerSession original4 = new WorkerSession("4",mtaskList4);
		WorkerSession original5 = new WorkerSession("5",mtaskList5);
		WorkerSession original6 = new WorkerSession("6",mtaskList6);

		WorkerSession copy0 = new WorkerSession("7",mtaskList0);
		WorkerSession copy1 = new WorkerSession("8",mtaskList1);
		WorkerSession copy2 = new WorkerSession("9",mtaskList2);
		WorkerSession copy3 = new WorkerSession("10",mtaskList3);
		WorkerSession copy4 = new WorkerSession("11",mtaskList4);
		WorkerSession copy5 = new WorkerSession("12",mtaskList5);
		WorkerSession copy6 = new WorkerSession("13",mtaskList6);

		WorkerSession copy00 = new WorkerSession("14",mtaskList0);
		WorkerSession copy11 = new WorkerSession("15",mtaskList1);
		WorkerSession copy22 = new WorkerSession("16",mtaskList2);
		WorkerSession copy33 = new WorkerSession("17",mtaskList3);
		WorkerSession copy44 = new WorkerSession("18",mtaskList4);
		WorkerSession copy55 = new WorkerSession("19",mtaskList5);
		WorkerSession copy66 = new WorkerSession("20",mtaskList6);

		this.expectedStack.push(copy66);
		this.expectedStack.push(copy55);
		this.expectedStack.push(copy44);
		this.expectedStack.push(copy33);
		this.expectedStack.push(copy22);
		this.expectedStack.push(copy11);
		this.expectedStack.push(copy00);
		this.expectedStack.push(copy6);
		this.expectedStack.push(copy5);
		this.expectedStack.push(copy4);
		this.expectedStack.push(copy3);
		this.expectedStack.push(copy2);
		this.expectedStack.push(copy1);
		this.expectedStack.push(copy0);
		this.expectedStack.push(original6);
		this.expectedStack.push(original5);
		this.expectedStack.push(original4);
		this.expectedStack.push(original3);
		this.expectedStack.push(original2);
		this.expectedStack.push(original1);
		this.expectedStack.push(original0); //so the first original is at the top of the stack.
	}


	@Test
	public void testGenerateSessions(){

		String fileName = "TinySample.java";
		String filePath = "C:/Users/Christian Adriano/Documents/GitHub/crowd-debug-firefly/samples-discarded/sample/tiny/";
		String fileContent = SourceFileReader.readFileToString(filePath+fileName);

		CodeSnippetFactory snippetFactory = new CodeSnippetFactory("TinySample.java", fileContent);

		ArrayList<CodeSnippet> list = snippetFactory.generateSnippetsForFile();
		if((list ==null ) || (list.size()!=2))
			Assert.fail("Null list of snippets or file does not match test data, actual size is: "+list.size());
		else{
			QuestionFactory questionFactory = new QuestionFactory();
			questionFactory.generateMicrotasks(list, "Bug report message", 0);
			HashMap<Integer, Microtask> microtaskMap = questionFactory.getConcreteQuestions(); 

			FileDebugSession fileDebuggingSession = new FileDebugSession("TinySample.java",fileContent, microtaskMap);

			//Persist data
			MicrotaskStorage memento = MicrotaskStorage.initializeSingleton();;
			memento.insert(fileName, fileDebuggingSession);

			//Generate the WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory(2);

			//Test the original Stack
			Stack<WorkerSession> actualStack = sessionFactory.generateSessions(3-1);
			while(!actualStack.isEmpty() && !expectedStack.isEmpty()){
				WorkerSession actual = actualStack.pop();
				WorkerSession expected = expectedStack.pop();
				Assert.assertEquals("WorkerSession ID: "+ expected.getId()+" does not match", actual.getId(),expected.getId());

			}			
		}
	}

}
