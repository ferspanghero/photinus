package sample.JustOneSample;

public class SimpleSampleCode {

	Integer SeedLimit = null;

	public SimpleSampleCode(Integer seedValue) {
		if (SeedLimit == null) this.SeedLimit = new Integer(seedValue);	// all at the same line
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
}
