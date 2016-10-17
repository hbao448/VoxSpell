package VoxSpell.words;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import VoxSpell.gui.SettingsData;

public class ScoreKeeper {

	private SettingsData settings;
	
	public ScoreKeeper(SettingsData settings) {
		this.settings = settings;
	}

	public void saveScore(String name, int _level2, int score) {

		BufferedWriter bw = null;

		try {
			// Opens the .results file for appending
			bw = new BufferedWriter(new FileWriter(".scores", true));

			// Writes the level and score to the results file
			bw.write(name + "\t" + _level2 + "\t" + score + "\t" + settings.getWordlist().getWordlistFile().getName());
			bw.newLine();

		} catch (IOException e) {

		} finally {
			try {
				bw.close();
			} catch (IOException e) {

			}
		}

	}
	
	/**
	 * This method deletes the existing results and failed files and then
	 * creates new ones that are empty
	 * 
	 * Reused code from A2
	 */
	public static void clearStatistics() {

		File file = new File(".results");

		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {

		}

		file = new File(".failed");

		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {

		}

		file = new File(".scores");

		file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {

		}

	}
	
	/**
	 * This method appends a level and the score for that level to the results file
	 * 
	 * Modified code from A2
	 * 
	 * @param level
	 * @param score
	 */
	public void appendList(int level, int score) {

		BufferedWriter bw = null;

		try {
			// Opens the .results file for appending
			bw = new BufferedWriter(new FileWriter(".results", true));

			// Writes the level and score to the results file
			bw.write("Level" + level + "\t" + score + "\t" + settings.getWordlist().getWordlistFile().getName());
			bw.newLine();

		} catch (IOException e) {

		} finally {
			try {
				bw.close();
			} catch (IOException e) {

			}
		}

	}
	
	/**
	 * This method appends a word to the failed file if the word is not already
	 * on the failed file
	 * 
	 * Modified code from A2
	 * 
	 * @param currentWord the word to append to the failed file
	 */
	public void appendFailed(String currentWord, int level) {
		Wordlist failedList = new Wordlist(new File(".failed"));
		ArrayList<String> failed = failedList.readList();

		//The current word contains both the word as well as the level it is from
		currentWord = currentWord + "	" + level;
		// If the failed list does not contain the word to be added, then it is
		// added
		if (!failed.contains(currentWord)) {

			BufferedWriter bw = null;

			try {
				bw = new BufferedWriter(new FileWriter(".failed", true));
				bw.write(currentWord);
				bw.newLine();
				bw.close();
			} catch (IOException e) {

			}

		}

	}
	/**
	 * This method removes a word from the failed list if it exists there, it is
	 * intended to be called once the user correctly spells a word
	 * 
	 * Reused A2 code
	 * 
	 * @param currentWord The word to remove from the failed file
	 */
	public void removeWord(String currentWord) {
		
		Wordlist failedList = new Wordlist(new File(".failed"));
		ArrayList<String> failed = failedList.readList();

		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(".failed"));

			for (String word : failed) {
				if (!word.equals(currentWord)) {
					bw.write(word);
					bw.newLine();
				}
			}

			bw.close();
		} catch (IOException e) {

		}

	}
	
	public int getHighscore(String player, String wordlist) {
		
		int highscore = 0;
		
		ArrayList<String> results = new Wordlist(new File(".scores")).readList();
		
		for (String result : results) {
			String[] split = result.split("\t");
			if (split[0].equals(player) && split[3].equals(wordlist)) {
				int score = Integer.parseInt(split[2]);
				if (score > highscore) {
					highscore = score;
				}
			}
		}

		return highscore;
	}
	
}
