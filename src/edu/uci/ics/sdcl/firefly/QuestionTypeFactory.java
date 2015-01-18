package edu.uci.ics.sdcl.firefly;

import java.util.HashMap;

/** Initializes all the 215 different questions with the correct type.
 * 
 * @author adrianoc
 *
 */
public class QuestionTypeFactory {

	/** Indexed by all of the types in QuestionType */
	public HashMap<String,HashMap<Integer,QuestionType>> questionTypeMap = new HashMap<String,HashMap<Integer,QuestionType>>(); 
	
	/** Indexes all IDs to their question types */
	public HashMap<Integer, String> idQuestionTypeMap = new HashMap<Integer, String>(); 
	
	/** BugPointing question map */
	public HashMap<Integer, Integer> bugPointingMap = new HashMap<Integer,Integer>();
	
	/** Indexes all IDs to their question super types */
	//public HashMap<Integer, String> idQuestionSuperTypeMap = new HashMap<Integer, String>(); 

	/** Indexes all IDs to their question content types */
	//public HashMap<Integer, String> idQuestionContentTypeMap = new HashMap<Integer, String>(); 
	
	
	/** Instantiate a question type for each microtask. 
	 * Also initializes the questionTypeMap and idQuestionTypeMap
	 */
	public void generateQuestionTypes(){
		
		int[] bugPointingList ={1, 3, 20, 25, 18, 61, 53, 51, 33, 69,71, 139,137,119,132,147,145,151,149,156,153,163,164,171,170,167,176,174,178,188,180};
		for(int id:bugPointingList){
			Integer IdObj = new Integer(id);
			this.bugPointingMap.put(IdObj,IdObj);
		}
		
		//Yes questions come first than there is a space
		
		int[] taskIdList1 = {25,71,137,139,149,151,164,171,188,   6,12,16,23,26,34,35,38,46,56,57,59,67,75,83,84,85,86,87,88,89,91,94,95,97,98,100,101,102,105,106,108,112,114,116,117,123,126,127,129,130,136,138,140,143,148,158,159,172,181,183,184,185,190,193,198,201,208,212,213,214,11,13,14,15,21,22,24,29,36,40,44,45,58,64,66,70,74,76,77,78,79,82,90,93,96,99,103,109,113,115,122,133,141,142,150,154,157,161,165,168,175,182,186,187,191,194,197,199,200,202,205,206,207,211};
		generateType(QuestionType.METHOD_INVOCATION,null, null, null, taskIdList1);
		
		int[] taskIdList2 = {176,  41,65,92,104,160,37,39,47,128,189,192}; //one expected YES questions
		generateType(QuestionType.METHOD_PARAMETERS,null, null, null, taskIdList2);
		
		int[] taskIdList3 = {32, 68, 118, 166, 0, 144, 152, 162, 173, 177}; //only expected NO questions
		generateType(QuestionType.METHOD_DECLARATION,null, null, QuestionType.DECLARATION, taskIdList3);

		int[] taskIdList4 = {1,33,69,119,145,153,163,167,174, 178};//only expected YES questions
		generateType(QuestionType.METHOD_BODY,null, null, QuestionType.BODY, taskIdList4);

		int[] taskIdList5_1 = {18,53,132,147,   81,121,210,204};
		generateType(QuestionType.LOOP_BODY,null, QuestionType.FOR_LOOP, QuestionType.BODY, taskIdList5_1);

		int[] taskIdList5_2 = {3, 170, 180     }; //only expected YES questions 56 47 = 103
		generateType(QuestionType.LOOP_BODY,null, QuestionType.WHILE_LOOP, QuestionType.BODY, taskIdList5_2);
		
		int[] taskIdList6_1= {20, 51, 61, 156, 10,   31,73,111,135,28,43,49,55,125,196};
		generateType(QuestionType.CONDITIONAL_BODY,QuestionType.IF_CONDITIONAL, null, QuestionType.BODY, taskIdList6_1);
		
		int[] taskIdList6_2= {};//There were no case statements in my sample code
		generateType(QuestionType.CONDITIONAL_BODY,QuestionType.CASE_CONDITIONAL, null, QuestionType.BODY, taskIdList6_2);

		int[] taskIdList7_1 = {80, 146, 203, 209, 17, 52, 120, 131}; //only expected NO questions
		generateType(QuestionType.LOOP_STATEMENT,null, QuestionType.FOR_LOOP,  QuestionType.DECLARATION, taskIdList7_1);
		
		int[] taskIdList7_2 = {179, 2, 169}; //only expected NO questions
		generateType(QuestionType.LOOP_STATEMENT,null, QuestionType.WHILE_LOOP,  QuestionType.DECLARATION, taskIdList7_2);

		int[] taskIdList8_1 = {7,19,27,48,54,62,63,107,110,134,195,4,5,8,9,30,42,50,60,72,124,155};//only expected NO questions
		generateType(QuestionType.CONDITIONAL_STATEMENT,QuestionType.IF_CONDITIONAL, null, QuestionType.DECLARATION, taskIdList8_1);
		
		int[] taskIdList8_2 = {};//There were no case statements in my sample code
		generateType(QuestionType.CONDITIONAL_STATEMENT,QuestionType.CASE_CONDITIONAL, null, QuestionType.DECLARATION, taskIdList8_2);

		int size = taskIdList1.length+taskIdList2.length+taskIdList3.length+taskIdList4.length+taskIdList5_1.length+taskIdList5_2.length+taskIdList6_1.length+taskIdList6_2.length+taskIdList7_1.length+taskIdList7_2.length+taskIdList8_1.length+taskIdList8_2.length;
		System.out.println("Total tasks: "+size);	
		
	}
	
	private void generateType(String type, String conditionalType, String loopType, String contentType, int[] taskIdList){
		for(int id: taskIdList){
			QuestionType questionType = new QuestionType(id, type, conditionalType, loopType, contentType);
			addTypeToMap(questionType,type);
			addTypeToMap(questionType,conditionalType);
			addTypeToMap(questionType,loopType);
			addTypeToMap(questionType,contentType);		
			this.idQuestionTypeMap.put(new Integer(id), type);
		}
	}
	
	private void addTypeToMap(QuestionType questionTypeObj, String typeStr){
		
		if(typeStr!=null){
			HashMap<Integer,QuestionType> idQuestionMap = this.questionTypeMap.get(typeStr);
			if(idQuestionMap==null)
				idQuestionMap = new HashMap<Integer,QuestionType>();
			idQuestionMap.put(questionTypeObj.getId(), questionTypeObj);
			this.questionTypeMap.put(typeStr, idQuestionMap);
		}
	}
	
	public HashMap<String,HashMap<Integer,QuestionType>> getQuestionTypeMap(){
		return this.questionTypeMap;
	}
	
	/**
	 * Prints in the standard output all questions ids followed by their type, 
	 * and whether they are bug pointing
	 */
	public void printNumberType(){
		
		System.out.println("Question ID | type | isBugPointing");
		
		for(int i=0;i<215;i++){
			Integer ID = new Integer(i);
			String type = this.idQuestionTypeMap.get(ID);
			boolean isBugPointing = this.bugPointingMap.containsKey(ID);
			
			System.out.println(ID+"|"+type+"|"+isBugPointing);
			
			
		}
		
	}
	
	public static void main(String args[]){
		QuestionTypeFactory factory = new QuestionTypeFactory();
		factory.generateQuestionTypes();
		factory.printNumberType();
	}
	
	
	
}
