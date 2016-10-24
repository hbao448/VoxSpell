package VoxSpell.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Wordlist {

	private int maxLevel;
	private File wordlist;
	private ArrayList<Level> wordLevels = new ArrayList<Level>();

	public Wordlist(File wordlist) {
		this.wordlist = wordlist;
	}

	/**
	 * This method reads each line in a file and stores each line into an
	 * ArrayList of strings and returns the ArrayList
	 * 
	 * Reused code from A2
	 * 
	 * @param file The name of the file to be read
	 * @return An ArrayList containing the lines of a file in sequential order
	 */
	public ArrayList<String> readList() {

		ArrayList<String> results = new ArrayList<String>();

		try {

			FileReader fr = new FileReader(wordlist);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				results.add(line);
			}
			br.close();
			fr.close();

		} catch (IOException e1) {

		}

		return results;

	}

	/**
	 * This method reads all the words inside a level from a wordlist
	 * 
	 * Original code by David
	 * 
	 * @param file The wordlist containing all the levels and words
	 * @param level The level from which words are to be found
	 * @return An ArrayList containing all the words in a level
	 * @throws WordlistFormatException 
	 */
	private Level readLevel(int level) throws WordlistFormatException {

		ArrayList<Word> results = new ArrayList<Word>();

		int next = level + 1;

		String levelString = "%Level " + level;
		String nextLevel = "%Level " + next;

		try {

			FileReader fr = new FileReader(wordlist);
			BufferedReader br = new BufferedReader(fr);

			String line;
			// Do nothing while the level header is not found
			while ((line = br.readLine()) != null && !line.equals(levelString)) {
			}

			line = br.readLine();

			// Add the words to the ArrayList once the level is found, up to when the header for the next level is found
			// Or when the list ends
			while (line != null) {
				if (line.equals(nextLevel)) {
					break;
				}
				results.add(new Word(line));
				line = br.readLine();
			}

			br.close();
			fr.close();

		} catch (IOException e1) {

		}

		if (results.isEmpty()) {
			throw new WordlistFormatException("Level " + level + " in " + getName() + " is empty.");
		}

		return new Level(results);

	}

	/**
	 * This method scans all of the levels inside a wordlist and also saves the maximum level inside the
	 * wordlist
	 * 
	 * Original code by David
	 * 
	 * @param wordlist The wordlist to scan the levels from
	 * @return An ArrayList of levels inside the wordlist
	 * @throws WordlistFormatException 
	 * @throws Exception 
	 */
	public ArrayList<String> scanLevels() throws WordlistFormatException{

		if (wordlist.length() > 10000000) {
			throw new WordlistFormatException("The wordlist must be smaller than 10MB, please select another file");
		}

		maxLevel = 0;
		ArrayList<String> all = readList();
		ArrayList<String> levels = new ArrayList<String>();
		int previousLevel = 0;

		for(String content: all){
			if(content.startsWith("%Level ")){
				int level;
				try {
					level = Integer.parseInt(content.split(" ")[1]);
				} catch (NumberFormatException e) {
					throw new WordlistFormatException(content.split(" ")[1] + " is not a valid level name");
				} catch (ArrayIndexOutOfBoundsException e) {
					throw new WordlistFormatException("The input wordlist must have \"%Level \" and a number to represent each level, followed by words in that level");
				}
				if (level != previousLevel + 1) {
					throw new WordlistFormatException("The input wordlist must the levels stored in ascending order, starting from level 1");
				}
				levels.add("Level " + level);
				wordLevels.add(readLevel(level));
				previousLevel = level;
				if (level > maxLevel) {
					maxLevel = Integer.parseInt(content.split(" ")[1]);
				}
			}
		}	
		if (levels.isEmpty()) {
			throw new WordlistFormatException("The input wordlist must have \"%Level \" and a number to represent each level, followed by words in that level");
		}

		return levels;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public String getName() {
		return wordlist.getName();
	}

	public Level getLevel(int i) {
		return wordLevels.get(i-1);
	}

	public String getPath() {
		return wordlist.getPath();
	}
}
