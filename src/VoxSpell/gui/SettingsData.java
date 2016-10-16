package VoxSpell.gui;

import java.io.File;

import VoxSpell.festival.Festival;
import VoxSpell.words.Wordlist;

public class SettingsData {

	private Festival festival;
	private Wordlist wordlist;
	
	public SettingsData() {
		wordlist = new Wordlist(new File("resources/Default Wordlist.txt"));
		festival = new Festival();
	}
	
	public void setWordlist(File file) {
		wordlist = new Wordlist(file);
	}
	
	public Wordlist getWordlist() {
		return wordlist;
	}
	
	public Festival getFestival() {
		return festival;
	}
	
	
}
