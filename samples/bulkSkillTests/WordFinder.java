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