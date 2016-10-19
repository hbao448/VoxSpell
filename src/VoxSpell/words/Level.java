package VoxSpell.words;

import java.util.ArrayList;
import java.util.Random;

public class Level {
	
	private ArrayList<Word> words;
	private ArrayList<Word> testedWords;
	
	public Level(ArrayList<Word> words) {
		this.words = words;
		testedWords = new ArrayList<Word>();
	}
	
	public void reset() {
		for (Word word : testedWords) {
			word.reset();
		}
		testedWords.clear();
	}
	
	public Word nextWord() {
		Random rand = new Random();
		int wordNumber = (Math.abs(rand.nextInt()) % words.size());
		while (testedWords.contains(words.get(wordNumber))) {
			wordNumber = (Math.abs(rand.nextInt()) % words.size());
		}
		Word word = words.get(wordNumber);
		testedWords.add(word);
		//System.out.println(word.getWord());
		return word;
	}
	
	public int size() {
		return words.size();
	}

}
