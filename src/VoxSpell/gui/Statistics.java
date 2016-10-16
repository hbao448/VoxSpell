package VoxSpell.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import VoxSpell.words.Wordlist;

import java.awt.Color;

public class Statistics extends AbstractScreen{

	private JButton close = new JButton("Main Menu");
	private MainFrame _spelling_Aid;
	private JTable table;
	private static DecimalFormat df = new DecimalFormat("#.#");
	private final static String[] columns = {"Level", "Passed", "Failed", "Average Score", "Total Attempts", "Wordlist"};

	public Statistics(MainFrame spelling_Aid) {
		setLayout(null);
		//close.setBackground(new Color(255, 255, 0));
		_spelling_Aid = spelling_Aid;
		showStats();
	}

	/**
	 * This method displays the user's statistics in a JFrame with a JTable storing the data
	 * It is reused code from A2
	 */
	public void showStats() {
		calculateStats();

		if (table != null) {

			setLayout(null);
			// Adds the JTable to a JScrollPane to allow for scrolling and for headers to show up
			JScrollPane scroll = new JScrollPane(table);
			scroll.setBounds(50, 120, 700, 400);
			add(scroll);

			// Disposes the JFrame and unhides the main menu once the "Main Menu" button is pressed
			close.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					_spelling_Aid.setScreen(new MainMenu(_spelling_Aid));
				}

			});

			table.setOpaque(true);
			scroll.setOpaque(true);
			//scroll.getViewport().setBackground(new Color(100, 149, 237));
			
			//table.setBackground(new Color(255, 255, 0));
			//scroll.setBackground(new Color(100, 149, 237));
			// Finally displays the JFrame containing the statistics
			close.setBounds(0, 550, 800, 50);
			add(close);
			
			JLabel logo = new JLabel("");
			logo.setHorizontalAlignment(SwingConstants.CENTER);
			logo.setBounds(0, 0, 800, 120);
			
			ImageIcon image = new ImageIcon("resources/Statistics.png");

			logo.setIcon(image);
			add(logo);
		
		}
	}

	/*
	 * Modified A2 code
	 */
	private void calculateStats() {
		// Reads the current results
		ArrayList<String> results = new Wordlist(new File(".results")).readList();

		// Displays an error message if there are no statistics to be shown
		if (results.size() == 0) {
			JOptionPane.showMessageDialog(new JFrame(), "Error, no results saved", "Error",
					JOptionPane.ERROR_MESSAGE);
			_spelling_Aid.setVisible(true);
		} else {
			// Stores the results for every level as a HashMap with a 3 element array representing passed, failed and total score
			HashMap<String, HashMap<Integer, Integer[]>> stats1 = new HashMap<String, HashMap<Integer, Integer[]>>();
			HashMap<String, ArrayList<Integer>> levelMap = new HashMap<String, ArrayList<Integer>>();
			ArrayList<String> fileNames = new ArrayList<String>();
			
			for (String result : results) {
				String[] split = result.split("\t");
				
					if (!stats1.containsKey(split[2])) {
						stats1.put(split[2], new HashMap<Integer, Integer[]>());
						levelMap.put(split[2], new ArrayList<Integer>());
						fileNames.add(split[2]);
					}
					
					HashMap<Integer, Integer[]> stats = stats1.get(split[2]);
					ArrayList<Integer> levels = levelMap.get(split[2]);
						
					int levelKey = Integer.parseInt(split[0].substring(5));
					// If the HashMap does not contain the current level, add it along with a [0,0,0] array
					if (!stats.containsKey(levelKey)) {
						levels.add(levelKey);
						Integer[] blank = new Integer[3];

						for (int i = 0; i < 3; i++) {
							blank[i] = 0;
						}

						stats.put(levelKey, blank);
					}
					
					int score = Integer.parseInt(split[1]);

					if(score >= 9){
						stats.get(levelKey)[0]++;
					} else {
						stats.get(levelKey)[1]++;
					}
					
					stats.get(levelKey)[2] = stats.get(levelKey)[2] + score;
				
			}

			int rows = 0;
			// Sorts the levels in ascending order
			for (String file : fileNames) {
				Collections.sort(levelMap.get(file));
				rows += levelMap.get(file).size();
			}
			
			Collections.sort(fileNames);

			Object[][] data = new Object[rows][6];

			int row = 0;

			for (String file : fileNames) {
			// Creates the data array used for the JTable
			for (Integer level : levelMap.get(file)) {

				Integer[] subtotals = stats1.get(file).get(level);
				int total = subtotals[0] + subtotals[1];

				data[row][0] = level;
				data[row][1] = subtotals[0];
				data[row][2] = subtotals[1];
				
				if(total > 0){
					data[row][3] = df.format((double) subtotals[2] / total);
				} else {
					data[row][3] = 0;
				}
				
				data[row][4] = total;
				data[row][5] = file;
				
				row++;
			}
			}

			// Makes the JTable and disallows editing and resizing
			table = new JTable(data, columns);
			table.getTableHeader().setReorderingAllowed(false);
			table.getTableHeader().setResizingAllowed(false);
			table.getColumnModel().getColumn(0).setPreferredWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(40);
			table.getColumnModel().getColumn(5).setPreferredWidth(200);
			table.setEnabled(false);
		}
	}
}
