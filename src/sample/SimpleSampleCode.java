package sample;

public class SimpleSampleCode {

	public SimpleSampleCode() {
	}
	
	public Integer factorial(Integer Seed, Integer Iterations){
		
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
