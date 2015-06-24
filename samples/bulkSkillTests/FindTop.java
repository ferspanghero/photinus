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
