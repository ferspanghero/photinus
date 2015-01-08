package edu.uci.ics.sdcl.firefly;


public class QuestionType {

	public static final String METHOD_INVOCATION = "METHOD_INVOCATION";
	public static final String METHOD_PARAMETERS = "METHOD_PARAMETERS";
	public static final String CONDITIONAL_STATEMENT = "CONDITIONAL_STATEMENT";
	public static final String LOOP_STATEMENT = "LOOP_STATEMENT";
	public static final String METHOD_DECLARATION = "METHOD_DECLARATION";
	public static final String CONDITIONAL_BODY = "CONDITIONAL_BODY";
	public static final String LOOP_BODY = "LOOP_BODY";
	public static final String METHOD_BODY = "METHOD_BODY";
	
	public static final String BODY="BODY";
	public static final String DECLARATION="DECLARATION";
	
	public static final String FOR_LOOP = "FOR_LOOP";
	public static final String WHILE_LOOP = "WHILE_LOOP";
	
	public static final String IF_CONDITIONAL = "IF_CONDITIONAL";
	public static final String CASE_CONDITIONAL = "CASE_CONDITIONAL";
	
	private String type = null;
	private String methodType = null;
	private String conditionalType = null;
	private String loopType = null;
	private String contentType = null; //either body or declaration
	
	private Integer id;
	
	public QuestionType(Integer id, String type, String conditionalType, String loopType,
			String contentType) {
		super();
		this.id = id;
		this.type = type;
		this.conditionalType = conditionalType;
		this.loopType = loopType;
		this.contentType = contentType;
	}

	public Integer getId(){
		return id;
	}
	
	public String getType() {
		return type;
	}

	public String getConditionalType() {
		return conditionalType;
	}

	public String getLoopType() {
		return loopType;
	}

	public String getContentType() {
		return contentType;
	}
	
	public boolean isConditionalType(){
		return conditionalType!=null;
	}
	
	public boolean isLoopType(){
		return loopType!=null;
	}
	
	public boolean isBodyType(){
		if(contentType!=null)
			return contentType.matches(BODY);
		else return false;
	}
	
	public boolean isMethodType(){
		return methodType!=null;
	}
	
}
