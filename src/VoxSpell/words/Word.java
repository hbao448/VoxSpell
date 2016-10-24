package VoxSpell.words;

public class Word {
	private String word;
	private int attempts = 0;
	private boolean correct = false;
	
	public Word(String word) {
		this.word = word;
	}
	
	/**
	 * Returns the word as a string
	 * @return
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Returns the boolean on whether or not the user has spelled the word correctly, case insensitive
	 * @param attempt The user's attempt at a word
	 * @return
	 */
	public boolean spellcheck(String attempt) {
		attempts++;
		correct = word.toLowerCase().equals(attempt.toLowerCase());
		return correct;
	}
	
	/**
	 * Resets the number of attempts and correct status
	 */
	public void reset() {
		attempts = 0;
		correct = false;
	}
	
	/**
	 * Returns the number of attempts that the user has had on the word
	 * @return
	 */
	public int getAttempts() {
		return attempts;
	}
	
	/**
	 * Returns whether or not the user has spelled the word correctly
	 * @return
	 */
	public boolean isCorrect() {
		return correct;
	}
	
	@Override
	public boolean equals(Object word) {
		Word wordA = (Word) word;
		return this.word.equals(wordA.word);
	}
}
