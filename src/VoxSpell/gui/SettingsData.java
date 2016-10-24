package VoxSpell.gui;

import java.io.File;

import VoxSpell.festival.Festival;
import VoxSpell.words.Wordlist;

/**
 * This class is used to store references to a festival and wordlist object
 * @author Hunter
 *
 */
public class SettingsData {

	private Festival festival;
	private Wordlist wordlist;
	
	public SettingsData() {
		wordlist = new Wordlist(new File("resources/Data/Default Wordlist.txt"));
		festival = new Festival();
	}
	
	/**
	 * Saves a new wordlist object
	 */
	public void setWordlist(File file) {
		wordlist = new Wordlist(file);
	}
	
	/**
	 * Returns the current wordlist
	 * @return
	 */
	public Wordlist getWordlist() {
		return wordlist;
	}
	
	/**
	 * Returns the festival object
	 * @return
	 */
	public Festival getFestival() {
		return festival;
	}
}
