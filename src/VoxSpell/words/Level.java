package VoxSpell.words;

import java.util.ArrayList;
import java.util.Random;

/** 
 * This class represents a wordlist level, and stores a list of words inside the level
 * @author Hunter
 *
 */
public class Level {
	
	private ArrayList<Word> words;
	private ArrayList<Word> testedWords;
	
	public Level(ArrayList<Word> words) {
		this.words = words;
		testedWords = new ArrayList<Word>();
	}
	
	/**
	 * Resets the tested status of every word
	 */
	public void reset() {
		for (Word word : testedWords) {
			word.reset();
		}
		testedWords.clear();
	}
	
	/**
	 * Returns a word that has not been tested so far
	 * @return
	 */
	public Word nextWord() {
		Random rand = new Random();
		int wordNumber = (Math.abs(rand.nextInt()) % words.size());
		while (testedWords.contains(words.get(wordNumber))) {
			wordNumber = (Math.abs(rand.nextInt()) % words.size());
		}
		Word word = words.get(wordNumber);
		testedWords.add(word);
		return word;
	}
	
	/**
	 * Returns the number of words in the level
	 * @return
	 */
	public int size() {
		return words.size();
	}

}
