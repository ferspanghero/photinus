package sample.tiny;

public class TinySample {

	Integer SeedLimit;
	
	public TinySample(Integer seedValue) {

		if(SeedLimit == null)

			this.SeedLimit = new Integer(seedValue);

	}

	public Integer seedCounter(Integer Seed, Integer Iterations){

		if(Seed!=null){

			int aux=1;

			for (int i=0;i<Iterations.intValue();i++){

				aux =  aux * Seed;

			}

			return new Integer(aux);

		}

		else return null;

	}

}
