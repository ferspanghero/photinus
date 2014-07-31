package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.junit.AfterClass;
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
import edu.uci.ics.sdcl.firefly.storage.WorkerSessionStorage;

public class WorkerSessionStorageTest {

	/**private HashMap <Integer, WorkerSession>  expectedActiveMap = new HashMap <Integer, WorkerSession> ();

	@Before
	public void setUpActivateWorkerSessions() throws Exception {
		this.expectedActiveMap = new HashMap <Integer, WorkerSession> ();
		HashMap<Integer, Microtask> mtaskMap = QuestionFactoryMock.generateQuestions();

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

		WorkerSession original0 = new WorkerSession(0,mtaskList0);
		WorkerSession original1 = new WorkerSession(1,mtaskList1);
		WorkerSession original2 = new WorkerSession(2,mtaskList2);
		WorkerSession original3 = new WorkerSession(3,mtaskList3);
		WorkerSession original4 = new WorkerSession(4,mtaskList4);
		WorkerSession original5 = new WorkerSession(5,mtaskList5);
		WorkerSession original6 = new WorkerSession(6,mtaskList6);

		WorkerSession copy0 = new WorkerSession(7,mtaskList0);
		WorkerSession copy1 = new WorkerSession(8,mtaskList1);
		WorkerSession copy2 = new WorkerSession(9,mtaskList2);
		WorkerSession copy3 = new WorkerSession(10,mtaskList3);
		WorkerSession copy4 = new WorkerSession(11,mtaskList4);
		WorkerSession copy5 = new WorkerSession(12,mtaskList5);
		WorkerSession copy6 = new WorkerSession(13,mtaskList6);

		WorkerSession copy00 = new WorkerSession(14,mtaskList0);
		WorkerSession copy11 = new WorkerSession(15,mtaskList1);
		WorkerSession copy22 = new WorkerSession(16,mtaskList2);
		WorkerSession copy33 = new WorkerSession(17,mtaskList3);
		WorkerSession copy44 = new WorkerSession(18,mtaskList4);
		WorkerSession copy55 = new WorkerSession(19,mtaskList5);
		WorkerSession copy66 = new WorkerSession(20,mtaskList6);

		this.expectedActiveMap.put(copy66.getId(),copy66);
		this.expectedActiveMap.put(copy55.getId(),copy55);
		this.expectedActiveMap.put(copy44.getId(),copy44);
		this.expectedActiveMap.put(copy33.getId(),copy33);
		this.expectedActiveMap.put(copy22.getId(),copy22);
		this.expectedActiveMap.put(copy11.getId(),copy11);
		this.expectedActiveMap.put(copy00.getId(),copy00);
		this.expectedActiveMap.put(copy6.getId(),copy6);
		this.expectedActiveMap.put(copy5.getId(),copy5);
		this.expectedActiveMap.put(copy4.getId(),copy4);
		this.expectedActiveMap.put(copy3.getId(),copy3);
		this.expectedActiveMap.put(copy2.getId(),copy2);
		this.expectedActiveMap.put(copy1.getId(),copy1);
		this.expectedActiveMap.put(copy0.getId(),copy0);
		this.expectedActiveMap.put(original6.getId(),original6);
		this.expectedActiveMap.put(original5.getId(),original5);
		this.expectedActiveMap.put(original4.getId(),original4);
		this.expectedActiveMap.put(original3.getId(),original3);
		this.expectedActiveMap.put(original2.getId(),original2);
		this.expectedActiveMap.put(original1.getId(),original1);
		this.expectedActiveMap.put(original0.getId(),original0);   


		//

	}
*/
	
	

	/**
	 * Tests consuming all new WorkerSessions and transition them to active
	 */
/**	@Test
	public void testActivateWorkerSessions(){

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
			MicrotaskStorage microtaskStorage = new MicrotaskStorage();
			microtaskStorage.replace(fileName, fileDebuggingSession);

			//Generate the stack of WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
			Stack<WorkerSession> actualStack = sessionFactory.generateSessions(10);
			Stack<WorkerSession> actualDuplicateStack = sessionFactory.duplicateSessions(actualStack,2);


			WorkerSessionStorage sessionStorage = new WorkerSessionStorage();

			boolean successNew = sessionStorage.appendNewWorkerSessionStack(actualStack,sessionStorage.NEW);
			boolean successNewCopies = sessionStorage.appendNewWorkerSessionStack(actualDuplicateStack,sessionStorage.NEW_COPIES);

			if(!successNew)
				Assert.fail("WorkerSessionStorage failed to update NEW WorkerSessions");
			else
				if(!successNewCopies)
					Assert.fail("WorkerSessionStorage failed to update NEW_COPIES WorkerSessions");
				else{
					//Retrieve all new WorkerSessions
					WorkerSession session = sessionStorage.readNewWorkerSession();
					while(session!=null){
						session = sessionStorage.readNewWorkerSession();
					}

					//Test that the stack of new Sessions is actually empty.
					Assert.assertNull(sessionStorage.readNewWorkerSession());

					//Test that all WorkerSessions are now in the Active Map.
					for(int id=0;id<21;id++){
						WorkerSession actualActiveSession = sessionStorage.readActiveWorkerSessionByID(id);	
						WorkerSession expectedSession = this.expectedActiveMap.get(id);
						Assert.assertEquals("WorkerSession ID: "+ id+" does not match", actualActiveSession.getId().intValue(),expectedSession.getId().intValue());
					}
				}
		}
	}
	
	@AfterClass
	public void dispose(){
		 
	 }
	
	**/

	//-----------------------------------------------------------------------------------------

	private ArrayList<WorkerSession>  expectedClosedList = new ArrayList<WorkerSession> ();

	@Before
	public void setUpClosedteWorkerSessions() throws Exception {
		expectedClosedList = new ArrayList<WorkerSession> ();
		HashMap<Integer, Microtask> mtaskMap = QuestionFactoryMock.generateQuestions();

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

		WorkerSession original0 = new WorkerSession(0,mtaskList0);
		WorkerSession original1 = new WorkerSession(1,mtaskList1);
		WorkerSession original2 = new WorkerSession(2,mtaskList2);
		WorkerSession original3 = new WorkerSession(3,mtaskList3);
		WorkerSession original4 = new WorkerSession(4,mtaskList4);
		WorkerSession original5 = new WorkerSession(5,mtaskList5);
		WorkerSession original6 = new WorkerSession(6,mtaskList6);

		WorkerSession copy0 = new WorkerSession(7,mtaskList0);
		WorkerSession copy1 = new WorkerSession(8,mtaskList1);
		WorkerSession copy2 = new WorkerSession(9,mtaskList2);
		WorkerSession copy3 = new WorkerSession(10,mtaskList3);
		WorkerSession copy4 = new WorkerSession(11,mtaskList4);
		WorkerSession copy5 = new WorkerSession(12,mtaskList5);
		WorkerSession copy6 = new WorkerSession(13,mtaskList6);

		WorkerSession copy00 = new WorkerSession(14,mtaskList0);
		WorkerSession copy11 = new WorkerSession(15,mtaskList1);
		WorkerSession copy22 = new WorkerSession(16,mtaskList2);
		WorkerSession copy33 = new WorkerSession(17,mtaskList3);
		WorkerSession copy44 = new WorkerSession(18,mtaskList4);
		WorkerSession copy55 = new WorkerSession(19,mtaskList5);
		WorkerSession copy66 = new WorkerSession(20,mtaskList6);

		this.expectedClosedList.add(original0);  
		this.expectedClosedList.add(original1);
		this.expectedClosedList.add(original2);
		this.expectedClosedList.add(original3);
		this.expectedClosedList.add(original4);
		this.expectedClosedList.add(original5);
		this.expectedClosedList.add(original6);
		this.expectedClosedList.add(copy0);
		this.expectedClosedList.add(copy1);
		this.expectedClosedList.add(copy2);
		this.expectedClosedList.add(copy3);
		this.expectedClosedList.add(copy4);
		this.expectedClosedList.add(copy5);
		this.expectedClosedList.add(copy6);
		this.expectedClosedList.add(copy00);
		this.expectedClosedList.add(copy11);
		this.expectedClosedList.add(copy22);
		this.expectedClosedList.add(copy33);
		this.expectedClosedList.add(copy44);
		this.expectedClosedList.add(copy55);
		this.expectedClosedList.add(copy66);

	}


	/**
	 * Tests consuming all new Active WorkerSessions and transition them to Closed
	 */
	@Test
	public void testClosedWorkerSessions(){	

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
			MicrotaskStorage microtaskStorage = new MicrotaskStorage();
			microtaskStorage.replace(fileName, fileDebuggingSession);

			//Generate the stack of New and Duplicated WorkerSession
			WorkerSessionFactory sessionFactory = new WorkerSessionFactory();
			Stack<WorkerSession> actualStack = sessionFactory.generateSessions(10);
			Stack<WorkerSession> actualDuplicateStack = sessionFactory.duplicateSessions(actualStack,2);
			WorkerSessionStorage sessionStorage = new WorkerSessionStorage();
			sessionStorage.appendNewWorkerSessionStack(actualStack,sessionStorage.NEW);
			sessionStorage.appendNewWorkerSessionStack(actualDuplicateStack,sessionStorage.NEW_COPIES);

			//Transition all New and COPIES to ACTIVE
			WorkerSession session = sessionStorage.readNewWorkerSession();
			while(session!=null){
				session = sessionStorage.readNewWorkerSession();
			}

			//Transition all ACTIVE to CLOSED
			for(int id=0;id<21;id++){
				WorkerSession actualActiveSession = sessionStorage.readActiveWorkerSessionByID(id);	
				//Simulates resolving all mi
				actualActiveSession = answerAllMicrotasks(actualActiveSession);
				
				sessionStorage.updateActiveWorkerSession(actualActiveSession);//already moves the WorkerSession
			}

			ArrayList<WorkerSession> actualClosedList = sessionStorage.readClosedWorkerSessionList();	
			int id=0;
			//Test whether all WorkerSessions are now CLOSED
			while(id<21 && id<actualClosedList.size()){
				WorkerSession actualClosedSession = actualClosedList.get(id); 
				WorkerSession expectedClosedSession = this.expectedClosedList.get(id);
				Assert.assertEquals("WorkerSession ID: "+ id+" does not match", actualClosedSession.getId().intValue(),expectedClosedSession.getId().intValue());
				id++;
			}
		}			
	}
	

	/** Helper method
	 */
	private WorkerSession answerAllMicrotasks(WorkerSession session){

		Microtask microtask = session.getCurrentMicrotask();
		while(microtask!=null){
			session.storeCurrentMicrotask(microtask);
			microtask = session.getCurrentMicrotask();
		}
		return session;
	}
}

