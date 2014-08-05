package pretask.test;

public class IntermediateTest1 {

	private int[] numbers;

	public IntermediateTest1(int[] numbersArg){
		this.numbers = numbersArg;
	}

	public Integer getValue(int indexArg){
		try {
			return this.numbers[indexArg];
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Index must be a positive "
					+ "value less than " + this.numbers.length);
			System.exit(-1);
			return null;
		}
	}

	public Integer findHigher(int lowerIndex, int higherIndex){
		if (lowerIndex > higherIndex){
			System.out.println("Lower index cannot be higher "
					+ "than higher index");
			return null;
		}
		Integer higherValue = this.getValue(lowerIndex);
		for(int i = lowerIndex; i <= higherIndex; i++){
			higherValue = ( higherValue < this.getValue(i) ) ? 
					this.getValue(i) : higherValue;
		}
		return higherValue;
	}
	
	public static void main(String[] args) {
		int myNumbers[] = {-2, 0, 2, 4, 8, 16};
		IntermediateTest1 numbers = new IntermediateTest1(myNumbers);
		System.out.println( numbers.findHigher(4, 1));
	}
}