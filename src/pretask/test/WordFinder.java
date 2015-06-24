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
		String mySentence = "cogito ergo sum";
		WordFinder finder = new WordFinder(mySentence);
		System.out.println(finder.wordPosition("cogito"));
	}
}


//What is the output of the code above?
//-1, 0, 1, 2, 3
//A: 0

//If at line 22 mySentence is "cogito ergo cogito", what would be output?
//-1, 0, 1, 2, 3
//A: 2

//If at line 13 we replace "int i=0;" with "int i=1;"  what would be the outcome?
//-1, 0, 1, 2, 3
//A: -1

//If we only replace the statement at line 15 for "return i;". What would be output?
//-1, 0, 1, 2, 3
//A: 0

//if in line 22 mySentence is null instead of "cogito ergo sum", what line in the program will cause a Null Pointer Exception?
//23, 24, 7, 11, 14
//A: 11
