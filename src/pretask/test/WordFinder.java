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

//At line 20, if mySentence is set to "cogito ergo cogito", what would be the output?
//-1, 0, 1, 2, 3
//A: 2

//At line 12, if we replace "int i=0;" with "int i=1;",  what would be the output?
//-1, 0, 1, 2, 3
//A: -1

//At line 14, if we replace "found= i;" with "return i;", what would be the output?
//-1, 0, 1, 2, 3
//A: 0

//At line 20, if mySentence is set to null instead of "cogito ergo sum", what line in the program will cause a Null Pointer Exception?
//21, 22, 6, 10, 13
//A: 10
