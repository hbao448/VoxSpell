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
import VoxSpell.words.ScoreKeeper;
import VoxSpell.words.Wordlist;

import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class MainMenu extends AbstractScreen {

	private JButton quiz = new JButton("New Spelling Quiz");
	private JButton hiscores = new JButton("View Hiscores");
	private JButton statistics = new JButton("View Statistics");
	private JButton clear = new JButton("Clear Statistics");
	private MainFrame _spelling_Aid;
	private JButton exit = new JButton("Exit");
	private JButton settings = new JButton("Settings");
	private final JLabel logo = new JLabel();
	private final JPanel options = new JPanel();
	private JComboBox comboBox;
	private int _level = 1;

	public MainMenu(MainFrame spelling_aid) {
		_spelling_Aid = spelling_aid;
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 150, 800, 350);
		add(panel);
		panel.setLayout(null);


		panel.add(quiz);
		panel.add(hiscores);
		panel.add(statistics);
		panel.add(clear);

		hiscores.setToolTipText("View the current high scores");
		//hiscores.setBackground(new Color(255, 255, 0));
		hiscores.setBounds(400, 0, 400, 175);
		hiscores.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				_spelling_Aid.setScreen(new Hiscores(_spelling_Aid));

			}

		});

		quiz.setToolTipText("Starts a new spelling quiz");
		//quiz.setBackground(new Color(255, 255, 0));
		quiz.setBounds(120, 0, 280, 175);

		quiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Quiz quizScreen = new Quiz(_spelling_Aid, _level);
				_spelling_Aid.setScreen(quizScreen);
				quizScreen.setSubmitAsDefault();
				quizScreen.startQuiz();
			}

		});

		statistics.setToolTipText("View the pass rates for each level");
		//statistics.setBackground(new Color(255, 255, 0));
		statistics.setBounds(0, 175, 400, 175);
		statistics.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Shows the statistics and hides the main menu
				Statistics statsScreen = new Statistics(_spelling_Aid);
				_spelling_Aid.setScreen(statsScreen);
			}

		});
		clear.setToolTipText("Clear all saved statistics and scores");
		//clear.setBackground(new Color(255, 255, 0));
		clear.setBounds(400, 175, 400, 175);
		try {
			comboBox = new JComboBox(_spelling_Aid.getSettings().getWordlist().scanLevels().toArray());
		} catch (Exception e1) {
			comboBox = new JComboBox();
			e1.printStackTrace();
		}
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox lv = (JComboBox) e.getSource();
				String selectedlv = (String) lv.getSelectedItem();
				if (selectedlv != null) {
					String[] level = selectedlv.split(" ");
					_level = Integer.parseInt(level[1]);
				}
			}
		});
		comboBox.setBounds(0, 0, 120, 175);

		panel.add(comboBox);
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
						"Clear statistics", JOptionPane.YES_NO_OPTION);

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
				_spelling_Aid.setScreen(new Settings(_spelling_Aid));
			}

		});
		options.setLayout(null);

		options.add(exit);
		options.add(settings);
	}
}
