package test;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

/**
 * Class created just to test my round robin algorithm to select
 * equally spaced microtasks
 * 
 * @author adrianoc
 *
 */
public class RoundRobinTest {


	public int[] list1 = {1,2,3,4,5,6,7,8,9,10};
	public int[] bug8List = {1,2,3,4,5,6,7,8,9,10};//2
	public int[] bug24List = {1,2,3,4,5,6};//0
	public int[] bug6List = {1,2,3,4,5,6,7,8,9,10,11,12,13};//2	
	public int[] bug7List = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};//1
	public int[] bug51List = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};//1
	public int[] bug33List = {1,2,3,4,5,6,7};//2
	public int[] bug35List = {1,2,3,4,5,6,7,8,9};
	public int[] bug54List = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21};
	public int[] bug29List = {1,2,3};
	public int[] bug59List = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};//1
	public int[] bug43List = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};//1
	
	
	
	public class Session{
		ArrayList<Integer> microtaskList = new ArrayList<Integer>();
		
	}

	int microtaskPerSession = 3;

	/**
	 * Stack of available positions in the list.
	 */

	public Stack<Integer> initializeIndexStack(int[] list){
		Stack<Integer> availablePositionsStack = new Stack<Integer>();
		for(int j=list.length-1;j>=0;j--){
			Integer position = new Integer(j);
			availablePositionsStack.push(position);
		}
		return availablePositionsStack;
	}

	
	private ArrayList<Boolean> initializeAvailabilityList(int size){
		ArrayList<Boolean> availabilityList= new ArrayList<Boolean>();
		for (int i=0;i<size;i++){
			availabilityList.add(new Boolean(true));
		}
		return availabilityList;
	}

	public int computeSessionsPerFile(int numberOfQuestionsPerFile, int numberOfMicrotaksPerSession){
		int sessionsPerFile = numberOfQuestionsPerFile/numberOfMicrotaksPerSession;
		double remainder = Math.IEEEremainder(numberOfQuestionsPerFile,numberOfMicrotaksPerSession);
		int remainderInt = new Double(remainder).intValue();
		sessionsPerFile = sessionsPerFile+Math.abs(remainderInt);
		return sessionsPerFile;
	}
	
	public ArrayList<Session> roundRobin(int[] list){

		ArrayList<Session> sessionList = new ArrayList<Session>();
		
		Stack<Integer> availablePositionsStack = this.initializeIndexStack(list);
		ArrayList<Boolean> availabilityList = this.initializeAvailabilityList(list.length);
		
		//Double sessionPerFile = list.length/this.microtaskPerSession;
		//sessionPerFile.
	
		int sessionsPerFile = this.computeSessionsPerFile(list.length, this.microtaskPerSession);
		int step = list.length / sessionsPerFile;
		System.out.println("list.length: "+list.length+ ", sessionsPerFile: "+sessionsPerFile);
		
		for(int i=0;i<sessionsPerFile;i++)//Compose all different sessions for one file 

			if(!availablePositionsStack.isEmpty()){	
				int pos=availablePositionsStack.pop().intValue(); //get the top one
				Session session = new Session();		
				for(int j=0;  j<microtaskPerSession; j++){ //Compose one session with N microtasks			
					int	next = pos;
					availabilityList.set(next,new Boolean(false));//Make the microtask not available anymore
					int microtask = list[next];
					session.microtaskList.add(new Integer(microtask));
					pos = next + sessionsPerFile;
					while( pos<list.length && availabilityList.get(pos).booleanValue()==false)//find the next available microtask
						pos++;
					if(pos>=list.length)
						pos = pos - list.length;
				}
				sessionList.add(session);
			}
		return sessionList;
	}
	
	@Test
	public void testRoundRobin(){
		this.initializeAvailabilityList(this.microtaskPerSession);
		ArrayList<Session> sessionList = this.roundRobin(this.list1);
		
		for(Session session: sessionList){
			System.out.println("Session:");
			for(Integer microtask: session.microtaskList){
				System.out.print(microtask.toString()+",");
			}
		}	
	}
	
	
	public void testRemainder(){

		System.out.println(Math.IEEEremainder(5, 3));
		System.out.println(Math.IEEEremainder(6, 3));
		System.out.println(Math.IEEEremainder(9, 3));
		System.out.println(Math.IEEEremainder(7, 3));
		System.out.println(Math.IEEEremainder(8, 3));
		System.out.println(Math.IEEEremainder(10, 3));
		System.out.println(Math.IEEEremainder(11, 3));

		int value = 35/3;
		double remainder = Math.IEEEremainder(35, 3);
		int remainderInt = new Double(remainder).intValue();
		
		int steps = value+Math.abs(remainderInt);
		System.out.println("steps:"+steps);

	}
	
	public static void main(String[] args){
				
				
		RoundRobinTest test = new RoundRobinTest();
		ArrayList<Session> sessionList = test.roundRobin(test.bug43List);
		
		for(Session session: sessionList){
			System.out.print("Session:");
			for(Integer microtask: session.microtaskList){
				System.out.print(microtask.toString()+",");
			}
			System.out.println();
		}
	}
	
}
