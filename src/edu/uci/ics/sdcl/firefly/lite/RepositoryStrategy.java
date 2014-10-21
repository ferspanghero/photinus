package edu.uci.ics.sdcl.firefly.lite;

/**
 * Instantiates the appropriate strategy to manage Sessions, Microtasks, Workers repositories.
 * @author adrianoc
 *
 */
public class RepositoryStrategy {

	
	private static int LiteStrategy=1;
	
	private static int SerializationStrategy=0;
	
	public RepositoryStrategy(int strategyType){
		if(strategyType==0){
			
		}
		else
			if(strategyType==1){
				
			}
			else{
				System.err.println("Invalid repository strategy ="+strategyType);
			}
		
	}
	
	public MicrotaskStrategy getMicrotaskRepository(){
		return null;
	}
	
	public SessionStrategy getSessionRepository(){
		return null;
	}
	
	public WorkerStrategy getWorkerRepository(){
		return null;
	}
	
}
