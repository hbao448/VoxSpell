package VoxSpell.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import VoxSpell.festival.Festival;
import VoxSpell.gui.Statistics.EmptyStatsException;
import VoxSpell.words.ScoreKeeper;
import VoxSpell.words.Wordlist;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.Font;

@SuppressWarnings("serial")
public class MainMenu extends AbstractBackgroundScreen {

	private JButton quiz = new JButton("New Spelling Quiz");
	private JButton hiscores = new JButton("View Hiscores");
	private JButton statistics = new JButton("View Statistics");
	private JButton clear = new JButton("Clear Results");
	private MainFrame _mainFrame;
	private JButton exit = new JButton("Exit");
	private JButton settings = new JButton("Settings");
	private final JLabel logo = new JLabel();
	private final JPanel options = new JPanel();
	private JComboBox levelSelect;
	private int _level = 1;
	private JTextField nameInput;
	private final JLabel invalidNameLabel = new JLabel("<html>Please enter a name between<br>1 and 15 letters long!</html>");

	public MainMenu(MainFrame mainFrame) {
		_mainFrame = mainFrame;
		setLayout(null);
	
		JPanel panel = new JPanel();
		panel.setBounds(0, 150, 800, 350);
		add(panel);
		panel.setLayout(null);
		panel.setOpaque(false);

		panel.add(quiz);
		panel.add(hiscores);
		panel.add(statistics);
		panel.add(clear);

		hiscores.setToolTipText("View the current high scores");
		//hiscores.setBackground(new Color(255, 255, 0));
		hiscores.setBounds(275, 180, 250, 50);
		hiscores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				_mainFrame.setScreen(new Hiscores(_mainFrame));

			}

		});

		quiz.setToolTipText("Starts a new spelling quiz");
		//quiz.setBackground(new Color(255, 255, 0));
		quiz.setBounds(275, 120, 250, 50);

		quiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameInput.getText().length() < 1 || nameInput.getText().length() > 15) {
					invalidNameLabel.setVisible(true);
				} else {
					Quiz quizScreen = new Quiz(_mainFrame, _level, nameInput.getText());
					_mainFrame.setScreen(quizScreen);
					quizScreen.setSubmitAsDefault();
					quizScreen.startQuiz();
				}
			}

		});
		_mainFrame.getRootPane().setDefaultButton(quiz);
		
		statistics.setToolTipText("View the pass rates for each level");
		//statistics.setBackground(new Color(255, 255, 0));
		statistics.setBounds(275, 240, 250, 50);
		statistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Shows the statistics and hides the main menu
				Statistics statsScreen;
				try {
					statsScreen = new Statistics(_mainFrame);
					_mainFrame.setScreen(statsScreen);
				} catch (EmptyStatsException e1) {
				}			
			}

		});
		clear.setToolTipText("Clear all saved statistics and scores");
		//clear.setBackground(new Color(255, 255, 0));
		clear.setBounds(275, 300, 250, 50);
		try {
			levelSelect = new JComboBox(_mainFrame.getSettings().getWordlist().scanLevels().toArray());
			levelSelect.setToolTipText("Select the level to start the spelling quiz from");
		} catch (Exception e1) {
			levelSelect = new JComboBox();
			e1.printStackTrace();
		}
		levelSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox lv = (JComboBox) e.getSource();
				String selectedlv = (String) lv.getSelectedItem();
				if (selectedlv != null) {
					String[] level = selectedlv.split(" ");
					_level = Integer.parseInt(level[1]);
				}
			}
		});
		levelSelect.setBounds(410, 59, 250, 50);

		panel.add(levelSelect);

		nameInput = new JTextField();
		nameInput.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameInput.setBounds(140, 59, 250, 50);
		panel.add(nameInput);
		nameInput.setColumns(10);

		JLabel levelLabel = new JLabel();
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelLabel.setBounds(410, 10, 250, 38);
		panel.add(levelLabel);
		ImageIcon level = new ImageIcon("resources/Level.png");
		levelLabel.setIcon(level);

		JLabel nameLabel = new JLabel();
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(140, 10, 250, 38);
		panel.add(nameLabel);
		ImageIcon name = new ImageIcon("resources/Name.png");
		nameLabel.setIcon(name);
		invalidNameLabel.setBounds(62, 120, 179, 50);
		invalidNameLabel.setVisible(false);
		
		panel.add(invalidNameLabel);


		logo.setHorizontalAlignment(SwingConstants.CENTER);
		//logo.setOpaque(true);
		logo.setForeground(new Color(100, 149, 237));
		//logo.setBackground(new Color(100, 149, 237));
		logo.setBounds(0, 0, 800, 150);
		ImageIcon image = new ImageIcon("resources/Logo.png");
		logo.setIcon(image);

		add(logo);
		options.setBounds(0, 500, 800, 100);

		add(options);
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Prompts the user if they are sure that they want to clear
				// their statistics
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you wish to clear all statistics?",
						"Clear Results", JOptionPane.YES_NO_OPTION);

				if (choice == JOptionPane.YES_OPTION) {
					// If they choose yes then their statistics are cleared and
					// a message is displayed telling them so
					ScoreKeeper.clearStatistics();
				}
			}
		});
		exit.setBounds(500, 25, 100, 50);

		exit.setToolTipText("Quit the program");
		//exit.setBackground(new Color(255, 255, 0));


		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		settings.setBounds(200, 25, 100, 50);

		settings.setToolTipText("Change voice and speaker speed");
		//settings.setBackground(new Color(255, 255, 0));

		// Adds an ActionListener to the change voice button to display a JOptionPane message allowing the user to change voice or speed
		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_mainFrame.setScreen(new Settings(_mainFrame));
			}

		});
		options.setLayout(null);
		options.setOpaque(false);

		options.add(exit);
		options.add(settings);
	}
}
