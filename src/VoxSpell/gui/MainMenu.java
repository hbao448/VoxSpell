package VoxSpell.gui;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import VoxSpell.festival.Bash;
import VoxSpell.gui.Statistics.EmptyStatsException;
import VoxSpell.words.ScoreKeeper;

@SuppressWarnings("serial")
public class MainMenu extends AbstractBackgroundScreen {

	private JButton quiz = new JButton("New Spelling Quiz");
	private JButton hiscores = new JButton("View Hiscores");
	private JButton statistics = new JButton("View Statistics");
	private JButton clear = new JButton("Clear Results");
	private MainFrame _mainFrame;
	private JButton exit = new JButton("Exit");
	private JButton settings = new JButton("");
	private final JLabel logo = new JLabel();
	private final JPanel options = new JPanel();
	@SuppressWarnings("rawtypes")
	private JComboBox levelSelect;
	private int _level = 1;
	private JTextField nameInput;
	private final JLabel invalidNameLabel = new JLabel("<html>Please enter a name between<br>1 and 15 letters long!</html>");
	private JButton help;

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

		//Adds a hiscores button that opens the hiscores when pressed
		hiscores.setToolTipText("View the current high scores");
		hiscores.setBounds(275, 180, 250, 50);
		hiscores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				_mainFrame.setScreen(new Hiscores(_mainFrame));

			}

		});

		//Adds a quiz button that opens a quiz when pressed
		quiz.setToolTipText("Starts a new spelling quiz");
		quiz.setBounds(275, 120, 250, 50);
		quiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// If the name is invalid then tell the user
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
		statistics.setBounds(275, 240, 250, 50);
		statistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Shows the statistics
				Statistics statsScreen;
				try {
					statsScreen = new Statistics(_mainFrame);
					_mainFrame.setScreen(statsScreen);
				} catch (EmptyStatsException e1) {
				}			
			}

		});
		
		//
		clear.setToolTipText("Clear all saved statistics and scores");
		clear.setBounds(275, 300, 250, 50);
		
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Prompts the user if they are sure that they want to clear
				// their statistics
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you wish to clear all statistics?",
						"Clear Results", JOptionPane.YES_NO_OPTION);

				if (choice == JOptionPane.YES_OPTION) {
					// If they choose yes then their statistics are cleared
					ScoreKeeper.clearStatistics();
				}
			}
		});
		
		//Adds a button that exits the program
		exit.setBounds(500, 25, 100, 50);
		exit.setToolTipText("Quit the program");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//Adds a settings button with an icon that opens the settings when pressed
		{
		ImageIcon settingsIcon = new ImageIcon("resources/Icons/Settings Icon.png");
		Image img = settingsIcon.getImage();
		Image resized = img.getScaledInstance(50, 50,  java.awt.Image.SCALE_SMOOTH);
		settingsIcon = new ImageIcon(resized);
		settings.setIcon(settingsIcon);
		}
		settings.setBounds(250, 25, 50, 50);

		settings.setToolTipText("Change voice and speaker speed");

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
		
		//Scans the wordlist and has the levels in the level select box
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

		//Adds a field for the user to enter their name in
		nameInput = new JTextField();
		nameInput.setFont(new Font("Tahoma", Font.PLAIN, 20));
		nameInput.setBounds(140, 59, 250, 50);
		panel.add(nameInput);
		nameInput.setColumns(10);

		//Adds some labels
		JLabel levelLabel = new JLabel();
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelLabel.setBounds(410, 10, 250, 38);
		panel.add(levelLabel);
		ImageIcon level = new ImageIcon("resources/Icons/Level.png");
		levelLabel.setIcon(level);

		JLabel nameLabel = new JLabel();
		nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nameLabel.setBounds(140, 10, 250, 38);
		panel.add(nameLabel);
		ImageIcon name = new ImageIcon("resources/Icons/Name.png");
		nameLabel.setIcon(name);
		invalidNameLabel.setBounds(62, 120, 179, 50);
		invalidNameLabel.setVisible(false);
		panel.add(invalidNameLabel);


		logo.setHorizontalAlignment(SwingConstants.CENTER);
		//logo.setOpaque(true);
		logo.setForeground(new Color(100, 149, 237));
		//logo.setBackground(new Color(100, 149, 237));
		logo.setBounds(0, 0, 800, 150);
		ImageIcon image = new ImageIcon("resources/Icons/Logo.png");
		logo.setIcon(image);

		add(logo);
		options.setBounds(0, 500, 800, 100);

		add(options);
		
		//Adds a help button that opens the user manual when pressed
		help = new JButton("");
		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().open(new File("resources/Data/User Manual.pdf"));
				} catch (IOException | IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Please put the user manual file back in resources/Data", "Can't find user manual",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		{
		ImageIcon icon = new ImageIcon("resources/Icons/Help.png");
		Image img = icon.getImage();
		Image resized = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resized); 
		help.setIcon(icon);
		}
		help.setBounds(740, 70, 50, 50);
		help.setFocusable(false);
		add(help);
		
		//Removes all created video rewards
		Bash.bashCommand("rm -f resources/Videos/Reward*");
		
	}
}
