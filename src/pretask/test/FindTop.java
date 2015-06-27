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
		System.out.println( numbers.findHigher(0, 5));
	}
	
}

//FINDTOP.JAVA ANSWERS
//Q: What is the output of the code above?
//O: -1, 0, 2, 4, 8
//A: 8

//Q: At line 21, if we have "numbers.findHigher(1,1)", what would be the output?
//O: 10, 5, 2, 4, 8
//A: 5

//Q: At line 11, if we replace "int i=lowerIndex;" with "int i=0;", what would be the output?
//O: 10, 5, 2, 4, 8
//A: 10

//Q: At line 13, if we replace "top = this.numbers[i];" with "return i;", what would be the output?
//O: 10, 5, 2, 4, 8
//A: 4

//Q: At line 21, if we have "numbers.findHigher(0,5)", what line in the program will cause an ArrayIndexOutOfBounds Exception?
//O: 6, 10, 11, 12, 13
//A: 12