package pretask.test;

public class BasicTest1 {
	private String sentence;
	
	public BasicTest1(String text){
		this.sentence = new String(text);
	}

	public boolean hasAWord(String wordToBeFound){
		String[] words = this.sentence.split(" ");
		for (String word : words) {
			if (word.equals(wordToBeFound))
				return true;
				// break;
		}
		return false;
	}
	
	public static void main(String[] args) {
		BasicTest1 mySentence = new BasicTest1("Sentence goes here");
		System.out.println(mySentence.hasAWord("goes"));
	}

}