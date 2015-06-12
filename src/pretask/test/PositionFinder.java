package pretask.test;
public class PositionFinder {

	public static void findPosition(String[] NameArray, String term){

		if (NameArray.length==0 && NameArray==null)
			System.out.print("Empty array");
		else{ 
			int position=-1;
			for (String name: NameArray){
				if(name.compareTo(term)==0)
					break;		
				position++;
			}
			System.out.print(position > 0? position: 0);
		}
	}

	public static void main(String[] args){
		String[] NameArray = {"Hola", "Kumusta", "Hello", "Ciao"};
		findPosition(NameArray, "Hola");
	}
}
