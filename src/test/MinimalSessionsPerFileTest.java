package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.uci.ics.sdcl.firefly.WorkerSessionFactory;

public class MinimalSessionsPerFileTest {

	public int numberOfMicrotaksPerSession =3;
	public int answersPerMicrotask = 10;
	WorkerSessionFactory factory;
	

	String[] fileList = {"HIT01_8.java", "HIT02_24.java", "HIT03_6.java", "HIT04_7.java",
			"HIT05_35.java","HIT06_51.java","HIT07_33.java","HIT08_54.java",
			"HIT09_29.java","HIT10_59.java", "HIT11_43.java"};
	
	public int[] expectedNumberOfSessions = {4, 2, 6,  12, 3, 6, 3, 7, 1, 6, 9};
	
	public int[] questionsPerFile =        {10, 6, 16, 35, 9, 16, 7, 21, 3, 17, 26};
	
	@Before
	public void setup(){
		factory = new WorkerSessionFactory(numberOfMicrotaksPerSession);
	}
	
	@Test
	public void test() {
		int totalSessions = 0;
		for(int i=0; i<fileList.length; i++){
			int actualNumberOfSessions = 		factory.minimalSessionsPerFile(questionsPerFile[i], this.numberOfMicrotaksPerSession);
			assertEquals("Wrong number of sessions for"+ fileList[i], expectedNumberOfSessions[i],actualNumberOfSessions);
			totalSessions = totalSessions + actualNumberOfSessions;
		}
		System.out.println("total distinct Sessions:"+ totalSessions);
		int totalFinalSessions = totalSessions * this.answersPerMicrotask;
		System.out.println("total Final Sessions:"+ totalFinalSessions);
		
	}

}
