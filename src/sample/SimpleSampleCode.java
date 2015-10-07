package sample;

import org.apache.commons.lang3.StringUtils;



public class SimpleSampleCode {


	Integer SeedLimit= new Integer(10);
	Code code=null;

	public SimpleSampleCode(int i){
		this.SeedLimit = new Integer(i);
		this.coder(i);
	}
	
	public Integer updateSeed(Integer seedValue) {
		if (this.SeedLimit == null) this.SeedLimit = new Integer(seedValue); 
		else this.SeedLimit = this.SeedLimit + seedValue;
		return SeedLimit;
	}
	
	
	public Integer coder(int i){
        Integer SeedLimit= new Integer(i);
        updateSeed(SeedLimit);
		if(this.equals(SeedLimit)){
			SeedLimit.doubleValue();
			SimpleSampleCode.factorial(SeedLimit, SeedLimit);
		}
		return i;
	}
	
	public String coder(){
		return "";
	}
	
	
	private class Code{
		
		private String code;
		
		public Code(String code){
			this.code = code;
		}
		
		public void  setCode(String code){
			this.code = code;
		}
		
		public int countOccurrences(CharSequence word){
			return StringUtils.countMatches(word, this.code.subSequence(0, code.length()-1));
		}
	}
	
	
	public Code getCode(){
		if(code==null) this.code = new Code("new");
		return code;
	}
	
	int limit = Integer.getInteger("10").compareTo(10);
	
	public void statementChecker(String value){
		SimpleSampleCode sample= new SimpleSampleCode(10);
		sample.getCode().countOccurrences(value.subSequence(0, value.length()-1));
	}
	
	
	public  void SeedVerifier(Integer seedValue) {
		if (SeedLimit == null) this.SeedLimit = new Integer(seedValue);	// all at the same line
		int value = seedValue.intValue();
		if (value == 5)							// if without braces
			this.SeedLimit = new Integer(value);
		if (value == 8) {this.SeedLimit = new Integer(value);	} // If with curly braces at the same line
		if (value == 10) 
		{
												// empty line
			this.SeedLimit = new Integer(value); // If with curly braces
		}
		SimpleSampleCode.newCases();
	}

	public static Integer factorial(Integer Seed, Integer Iterations) {
		if (Seed != null) 	// if with else
		{	int aux = 1;
			for (int i = 0; i < Iterations.intValue(); i++) {
				aux = aux * Seed;
			}
			return new Integer(aux);
		}
		else
			return null;
	}

	public static void newCases() {
		Integer cases = 2;
		cases -= 1;
		switch (cases) // switch case
		
		{
		case 1:
			System.out.println("1");
			break;
		case 2:
			System.out.println("2");
			break;
		case 3:
			System.out.println("3");
			break;
		default:
			System.out.println("Default");
			break;
		}
		Integer myArray[] = { 0, 1, 2 };
		for (Integer numero : myArray)
			System.out.println(numero.toString());
		Integer i = 0;
		for (; i < 3; i++) { // For with curly braces at the same starting line
			System.out.println(myArray[i]);
			System.out.println(myArray[i]);
			System.out.println(myArray[i]);
			if (1 == cases) {	// if with ifs and else-if
				if (2 == myArray[i]) { // If with curly braces at the same
					for (Integer j = 1;;) {
						j++;
						break;
					}
				}
				else if (3 == myArray[i])	// else-if
					break;
				else if (4 == myArray[i])	// with another else if 
				{
						cases = 1;
						myArray[i] = 5;
						System.out.println("Another else-if");
				}
				else
					cases = 2;
			}
			else if (2 == cases) {	// else if braces
				break;
			}
		}
		Integer j = 0;
		do {
			System.out.println("Do While here");
			j = SimpleSampleCode.factorial(j, j);
			j++;
		}
		while (j < 3);
		do {
			System.out.println("Do While here");
			j++;
		}
		while (j < 5);
		do {
			System.out.println("Do While here");
			j++;
		}
		while (j < 7);
		if (j == 11) {
			j--;
			System.out.println("Another if teste");
		}
		if (j == 10) {
			j--;
			System.out.println("Another if teste");
		}
		else {
			j++;
			System.out.println("Another if teste");
		}
	}
}
