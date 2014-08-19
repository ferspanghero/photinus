package sample;

public class CapFactorial {

	private Integer currentSeed = null;

	public CapFactorial(Integer initialSeed){
		this.currentSeed = initialSeed;
	}
	
	public Integer run(int number, int max){
		Integer value = updateSeed(number);
		return computeFactorial(8,value);
	}


	public Integer updateSeed(Integer seedValue) {
		if (this.currentSeed == null) this.currentSeed = new Integer(seedValue); 
		else this.currentSeed = this.currentSeed + seedValue;
		return currentSeed;
	}

	
	/** Computes the factorial of a number (seed) by a number of maximum iterations
	 *  
	 * @param seed the number which will be multiplied
	 * @param max maximum number of multiplications
	 * @return 
	 */
	private Integer computeFactorial(Integer seed, int max) {
		if (seed != null){
			int seedValue = seed.intValue();
			int count = 1;
			while(seed>0 && count<max) {
				seed--;
				count++;
				seed = seed * (seed-1);
			}
			return new Integer(seedValue);
		}
		else
			return null;
	}
}
