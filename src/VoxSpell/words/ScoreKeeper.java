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
			bw.write(name + "\t" + _level2 + "\t" + score + "\t" + settings.getWordlist().getName());
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
	 * @param size 
	 */
	public void appendList(int level, int score, int size) {

		BufferedWriter bw = null;

		try {
			// Opens the .results file for appending
			bw = new BufferedWriter(new FileWriter(".results", true));

			String passed;
			
			if ((size - score) > 1 || score == 0) {
				passed = "Fail";
			} else {
				passed = "Pass";
			}
			
			// Writes the level and score to the results file
			bw.write("Level" + level + "\t" + score + "\t" + settings.getWordlist().getName() + "\t" + passed + "\t" + ((double)score*100)/size);
			bw.newLine();

		} catch (IOException e) {

		} finally {
			try {
				bw.close();
			} catch (IOException e) {

			}
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
