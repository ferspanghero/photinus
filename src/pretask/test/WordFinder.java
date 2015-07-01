package pretask.test;

public class WordFinder {
	private String sentence;
	
	public WordFinder(String text){
		this.sentence = text;
	}

	public int wordPosition(String wordToBeFound){
		String[] wordlist = this.sentence.split(" ");
		int found=-1;
		for (int i=0; i<wordlist.length;i++) {
			if (wordlist[i].compareTo(wordToBeFound)==0)
				found= i;
		}
		return found;
	}
	
	public static void main(String[] args) {
		String mySentence = null;//"cogito ergo sum";
		WordFinder finder = new WordFinder(mySentence);
		System.out.println(finder.wordPosition("cogito"));
	}
}


//WORDFINDER ANSWERS
//What is the output of the code above?
//-1, 0, 1, 2, 3
//A: 0

//What would have been the output if the variable mySentence was set to "cogito ergo cogito" at line 20?
//-1, 0, 1, 2, 3
//A: 2

//What would have been the output if instead of "int i=0;" we had "int i=1;" at line 12?
//-1, 0, 1, 2, 3
//A: -1

//What would have been the output if instead of "found= i;" we had "return i;" at line 14?
//-1, 0, 1, 2, 3
//A: 0

//What line in the program would have caused a Null Pointer exception if we set the variable mySentence to null at line 20? 
//21, 22, 6, 10, 13
//A: 10