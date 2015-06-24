package pretask.test; 

public class PositionFinder {

		public static void findPosition(String[] list, String term){
			if (list==null || list.length==0)
				System.out.print("Empty array");
			else{ 
				int position=-1;
				for (String name: list){
					if(name.compareTo(term)==0)
						break;	
					position++;
				}
				System.out.print(position > 0 ? position: 0);
			}
		}
		public static void main(String[] args){
			String[] myArray = {};//{"Hola","Kumusta", "Hello", "Ciao"};
			findPosition(myArray, "Ciao");
		}
	}
