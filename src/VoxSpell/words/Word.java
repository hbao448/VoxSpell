package VoxSpell.words;

public class Word {
	private String word;
	private int attempts = 0;
	private boolean correct = false;
	
	public Word(String word) {
		this.word = word;
	}
	
	public String getWord() {
		return word;
	}
	
	public boolean spellcheck(String attempt) {
		attempts++;
		correct = word.toLowerCase().equals(attempt.toLowerCase());
		return correct;
	}
	
	public void reset() {
		attempts = 0;
		correct = false;
	}
	
	public int getAttempts() {
		return attempts;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	@Override
	public boolean equals(Object word) {
		Word wordA = (Word) word;
		return this.word.equals(wordA.word);
	}
}
