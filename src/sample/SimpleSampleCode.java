package sample;

import org.apache.commons.lang3.StringUtils;



public class SimpleSampleCode {

	Integer SeedLimit = null;
	
	Code code=null;
	
	
	public SimpleSampleCode(int i){
		SeedLimit = new Integer(i);
	}
	
	private class Code{
		
		private String code;
		
		public void setCode(String code){
			this.code = code;
		}
		
		public int countOccurrences(CharSequence word){
			return StringUtils.countMatches(word, this.code.subSequence(0, code.length()-1));
		}
	}
	
	
	public Code getCode(){
		if(code==null) this.code = new Code();
		return code;
	}
	
	public void statementChecker(String value){
		SimpleSampleCode sample= new SimpleSampleCode(10);
		sample.getCode().countOccurrences(value.subSequence(0, value.length()-1));
	}
	
	
	public void SeedVerifier(Integer seedValue) {
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
	}

	public Integer factorial(Integer Seed, Integer Iterations) {
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

	public void newCases() {
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
			j = this.factorial(j, j);
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
