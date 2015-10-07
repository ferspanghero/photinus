package edu.uci.ics.sdcl.firefly.util;

import java.util.Random;

public class RandomKeyGenerator {

	private Integer init=0;

	public static void main(String [] args){
		RandomKeyGenerator gen =  new RandomKeyGenerator();
		//System.out.println("randomSequence = "+ gen.generate());
		
				
		for(int i=0;i<20;i++){
				System.out.println(gen.getRandom(i));
		}
	}
	
	public int getRandom(int max){
				
		int timeMillis = new Double(System.currentTimeMillis()).intValue();
		int digit = timeMillis % 10;
		
		Random rand = new Random(); 
		
		
		return  rand.nextInt(20);
		
		
	}
	
	public String generate(){
		
		//Five Random numbers and Four random letters.
		Random rand = new Random(); 
		int one = rand.nextInt() % 10;
		
		int two = rand.nextInt() % 10;
		
		int three = rand.nextInt() % 10;
		
		int four = rand.nextInt() % 10;
		
		int five = rand.nextInt() % 10;
		
		int letterOne = rand.nextInt();
		
		int letterTwo = rand.nextInt();
		
		int letterThree = rand.nextInt();
		
		int letterFour = rand.nextInt();
		
		
		StringBuffer buffer= new StringBuffer();
		
		buffer.append(this.init++);
		buffer.append(this.getUnicodeAtoZ(letterOne));
		buffer.append(this.getUnicodeAtoZ(letterTwo));
		buffer.append(one);
		buffer.append(this.getUnicodeAtoZ(letterThree));
		buffer.append(two);
		buffer.append(this.getUnicodeAtoZ(letterFour));
		buffer.append(three);
		buffer.append(four);
		buffer.append(five);
		return buffer.toString();
	}
	
	
	public String getUnicodeAtoZ(int i){

		int remainder = i % 10;
		if(remainder<0)
			remainder= remainder * (-1);
	
		String character;	
		if((remainder&1) ==0){
			//even, then lowercase
			remainder = remainder+97; //97 is the unicode for a
			character = Character.toString((char)remainder);
		}
		else{
			//odd, then uppercase
			remainder = remainder+64; //65 is the unicode for A
			character = Character.toString((char)remainder);
		}

		//System.out.println("unicode of "+ remainder + " is " + character);
		
		return character;
	}
	
	
}
