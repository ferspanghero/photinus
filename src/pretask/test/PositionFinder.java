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

//What is the output of the code? 
//-1, 0, 1, 2, 3
//A: 2

//At line 20, what would be the output if instead of "Ciao" we have "Hola"?
//-1, 0, 1, 2, 3
//A: 0

//At line 20, what would be the output if instead of "Ciao" we have an empty String (e.g., " ")?
//-1, 0, 1, 2, 3
//A: 3

//At line 20, what would be the output if we have "String[] NameArray = {};" ?
//Empty array
//NullPointer exception
//ArrayOutOfBounds exception
//0
//A: Empty array

//if at line 19 NameArray = {"Hola", null}, which line of the program will cause a NullPointer exception?
//19, 20, 6, 10, 11
//A: 11