package pretask.test;

public class FindTop {

	private int[] numbers;

	public FindTop(int[] numbersArg){
		this.numbers = numbersArg;
	}

	public int findHigher(int lowerIndex, int higherIndex){
		int top = this.numbers[lowerIndex];
		for(int i = lowerIndex; i <= higherIndex; i++){
			if (top < this.numbers[i]) 
				top = this.numbers[i];
		}
		return top;
	}
	
	public static void main(String[] args) {
		int myNumbers[] = {10, 5, 2, 4, 8};
		FindTop numbers = new FindTop(myNumbers);
		System.out.println( numbers.findHigher(1, 4));
	}
	
}

//What is the output of the code above?
//-1, 0, 2, 4, 8
//A: 8

//If at line 26 we have "numbers.findHigher(1,1)", what would be output?
//10, 5, 2, 4, 8
//A: 5

//If at line 13 we replace "int i=lowerIndex;" with "int i=0;"  what would be the outcome?
//10, 5, 2, 4, 8
//A: 10

//If we only replace the statement at line 15 for "return i;". What would be output?
//10, 5, 2, 4, 8
//A: 4

//If at line 26 we have "numbers.findHigher(-1,5)",, what line in the program will cause an ArrayIndexOutOfBounds Exception?
//22, 23, 8, 12, 14
//A: 12