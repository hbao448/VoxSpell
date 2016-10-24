package VoxSpell.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import VoxSpell.words.Wordlist;

@SuppressWarnings("serial")
public class Hiscores extends AbstractBackgroundScreen{

	private JTable table;
	private JScrollPane scrollPane;
	private MainFrame _mainFrame;

	/**
	 * Create the frame.
	 */
	public Hiscores(MainFrame mainFrame) {

		_mainFrame = mainFrame;

		//Set the layout of the panel
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);

		//Configure the main menu button to return to the main menu and then add it to the panel
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mainFrame.setScreen(new MainMenu(_mainFrame));
			}
		});
		mainMenu.setBounds(350, 560, 100, 25);
		add(mainMenu);

		//Create a new scroll pane, set its dimensions and then add it to the panel
		scrollPane = new JScrollPane();
		scrollPane.setBounds(160, 120, 600, 402);
		add(scrollPane);

		//Create a new table for the data to be stored in and configure the scroll pane to use the table
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setRowHeight(75);

		//Create labels that display icons for first, second and third place medals as well as the heading and then scale them and add them to the panel
		{
			JLabel firstLabel = new JLabel("");
			ImageIcon icon = new ImageIcon("resources/Icons/1st.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			firstLabel.setIcon(icon);
			firstLabel.setBounds(50, 150, 60, 60);
			add(firstLabel);
		}
		{
			JLabel secondLabel = new JLabel("");
			ImageIcon icon = new ImageIcon("resources/Icons/2nd.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			secondLabel.setIcon(icon);
			secondLabel.setBounds(50, 225, 60, 60);
			add(secondLabel);
		}
		{
			JLabel thirdLabel = new JLabel("");
			ImageIcon icon = new ImageIcon("resources/Icons/3rd.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			thirdLabel.setIcon(icon);
			thirdLabel.setBounds(50, 300, 60, 60);
			add(thirdLabel);
		}
		{
			JLabel logo = new JLabel();
			logo.setHorizontalAlignment(SwingConstants.CENTER);
			logo.setBounds(0, 0, 800, 120);
			add(logo);
			ImageIcon image = new ImageIcon("resources/Icons/Hiscores.png");
			logo.setIcon(image);
		}
		//Calls a helper method to populate the table
		viewScores();
	}

	/**
	 * This method reads all of the user scores and the default scores and populates the existing table to show the
	 * top five scores in descending order
	 */
	private void viewScores() {

		//Reads the user scores and default scores and joins them
		ArrayList<String> results = new Wordlist(new File("resources/Data/Default Scores.txt")).readList();
		ArrayList<String> extraResults = new Wordlist(new File(".scores")).readList();
		ArrayList<Integer> resultScores = new ArrayList<Integer>();

		HashMap<Integer, ArrayList<Object[]>> scores = new HashMap<Integer, ArrayList<Object[]>>();

		results.addAll(extraResults);

		//Parses the result strings and stores them in a hash table
		for (String result : results) {
			String[] split = result.split("\t");

			int score = Integer.parseInt(split[2]);

			if (!scores.containsKey(score)) {
				scores.put(score, new ArrayList<Object[]>());
				resultScores.add(score);
			}

			Object[] data = new Object[3];

			data[0] = Integer.parseInt(split[1]);
			data[1] = split[0];
			data[2] = split[3];

			scores.get(score).add(data);

		}

		//Sorts the scores in ascending order
		Collections.sort(resultScores);

		int added = 0;

		Object[][] model = new Object[5][4];

		//Adds data to the table until 5 results have been added
		while (added < 5) {
			for (int i = resultScores.size()-1 ; i >= 0 && added < 5; i--) {
				ArrayList<Object[]> values = scores.get(resultScores.get(i));

				for (Object[] data : values) {
					if (added >= 5) {
						break;
					}
					model[added][0] = resultScores.get(i);
					model[added][1] = data[0];
					model[added][2] = data[1];
					model[added][3] = data[2];
					added++;
				}
			}
		}

		//Sets up the headings for the table
		table.setModel(new DefaultTableModel(
				model,
				new String[] {
						"Score", "Highest Level", "Player", "Wordlist"
				}
				));

		//Disables user interaction with the table
		table.getColumnModel().getColumn(1).setPreferredWidth(105);
		table.getColumnModel().getColumn(3).setPreferredWidth(128);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setEnabled(false);
	}
}
