package VoxSpell.words;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Wordlist {

	private int maxLevel;
	private File wordlist;
	
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
	 */
	public ArrayList<String> readLevel(int level) {

		ArrayList<String> results = new ArrayList<String>();

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
				results.add(line);
				line = br.readLine();
			}

			br.close();
			fr.close();

		} catch (IOException e1) {

		}

		return results;

	}
	
	/**
	 * This method scans all of the levels inside a wordlist and also saves the maximum level inside the
	 * wordlist
	 * 
	 * Original code by David
	 * 
	 * @param wordlist The wordlist to scan the levels from
	 * @return An ArrayList of levels inside the wordlist
	 * @throws Exception 
	 */
	public ArrayList<String> scanLevels() throws Exception{
		maxLevel = 0;
		ArrayList<String> all = readList();
		ArrayList<String> levels = new ArrayList<String>();
		int previousLevel = 0;

		for(String content: all){
			if(content.startsWith("%Level")){
				int level = Integer.parseInt(content.split(" ")[1]);
				if (level != previousLevel + 1) {
					throw new Exception();
				}
				levels.add("Level " + level);
				previousLevel = level;
				if (level > maxLevel) {
					maxLevel = Integer.parseInt(content.split(" ")[1]);
				}
			}
		}

		return levels;
	}

	public File getWordlistFile() {
		return wordlist;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}

	public String getName() {
		return wordlist.getName();
	}
}
