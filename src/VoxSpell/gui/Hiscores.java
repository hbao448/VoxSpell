package VoxSpell.gui;

import java.awt.Color;
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

public class Hiscores extends AbstractScreen{

	private JTable table;
	private JScrollPane scrollPane;
	private MainFrame _spelling_Aid;

	/**
	 * Create the frame.
	 */
	public Hiscores(MainFrame spelling_Aid) {
		
		_spelling_Aid = spelling_Aid;
		
		//setBackground(new Color(100, 149, 237));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);


		JButton mainMenu = new JButton("Main Menu");
		//mainMenu.setBackground(new Color(255, 255, 0));
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_spelling_Aid.setScreen(new MainMenu(_spelling_Aid));
			}
		});
		mainMenu.setBounds(0, 550, 800, 50);
		add(mainMenu);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(160, 120, 600, 402);
		add(scrollPane);

		table = new JTable();
		//table.setBackground(new Color(255, 255, 0));
		scrollPane.setViewportView(table);
		table.setRowHeight(75);

		{
			JLabel firstLabel = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/1st.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 

			firstLabel.setIcon(icon);
			firstLabel.setBounds(50, 150, 60, 60);
			add(firstLabel);
		}
		{
			JLabel secondLabel = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/2nd.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 

			secondLabel.setIcon(icon);
			secondLabel.setBounds(50, 225, 60, 60);
			add(secondLabel);
		}
		{
			JLabel thirdLabel = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/3rd.png");
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
			ImageIcon image = new ImageIcon("resources/Hiscores.png");

			logo.setIcon(image);
		}

		viewScores();
	}

	public void viewScores() {
		
		ArrayList<String> results = new Wordlist(new File("resources/Default Scores.txt")).readList();
		ArrayList<String> extraResults = new Wordlist(new File(".scores")).readList();
		ArrayList<Integer> resultScores = new ArrayList<Integer>();

		HashMap<Integer, ArrayList<Object[]>> scores = new HashMap<Integer, ArrayList<Object[]>>();

		results.addAll(extraResults);

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

		Collections.sort(resultScores);

		int added = 0;

		Object[][] model = new Object[5][4];

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

		table.setModel(new DefaultTableModel(
				model,
				new String[] {
						"Score", "Highest Level", "Player", "Wordlist"
				}
				));
		
		table.getColumnModel().getColumn(1).setPreferredWidth(105);
		table.getColumnModel().getColumn(3).setPreferredWidth(128);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setEnabled(false);
	}
}
