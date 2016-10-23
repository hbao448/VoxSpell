package VoxSpell.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class QuizInternal extends JPanel {

	private JProgressBar progressBar;
	private JLabel repeatsLeftLabel;
	private JLabel multiplierLabel;
	private JLabel ScoreLabel;
	private JLabel multiplierNumberLabel;
	private JLabel scoreNumberLabel;
	private JLabel levelLabel;
	private JLabel wordLabel;
	private JLabel levelNumberLabel;
	private JLabel wordNumberLabel;
	private JLabel newHighscoreLabel;
	private JLabel highscoreLabel;
	private JLabel highscoreNumberLabel;
	private JLabel hintLabel;
	private JLabel hintTextLabel;

	/**
	 * Create the panel.
	 */
	public QuizInternal() {
		//setBackground(new Color(100, 149, 237));
		setOpaque(false);
		setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(500, 200, 200, 40);
		add(progressBar);

		repeatsLeftLabel = new JLabel("");
		repeatsLeftLabel.setHorizontalAlignment(SwingConstants.CENTER);
		repeatsLeftLabel.setIcon(new ImageIcon("resources/Icons/Repeats.png"));
		repeatsLeftLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		repeatsLeftLabel.setBounds(150, 200, 300, 40);
		add(repeatsLeftLabel);

		multiplierLabel = new JLabel("");
		multiplierLabel.setHorizontalAlignment(SwingConstants.CENTER);
		multiplierLabel.setIcon(new ImageIcon("resources/Icons/Multiplier.png"));
		multiplierLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		multiplierLabel.setBounds(10, 400, 360, 40);
		add(multiplierLabel);

		ScoreLabel = new JLabel("");
		ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ScoreLabel.setIcon(new ImageIcon("resources/Icons/Score.png"));
		ScoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		ScoreLabel.setBounds(10, 450, 140, 40);
		add(ScoreLabel);

		multiplierNumberLabel = new JLabel("1 x");
		multiplierNumberLabel.setForeground(Color.BLACK);
		multiplierNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		multiplierNumberLabel.setBounds(380, 400, 100, 40);
		add(multiplierNumberLabel);

		scoreNumberLabel = new JLabel("100");
		scoreNumberLabel.setForeground(Color.BLACK);
		scoreNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		scoreNumberLabel.setBounds(260, 451, 150, 40);
		add(scoreNumberLabel);

		levelLabel = new JLabel("");
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelLabel.setIcon(new ImageIcon("resources/Icons/Level.png"));
		levelLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		levelLabel.setBounds(10, 110, 130, 40);
		add(levelLabel);

		wordLabel = new JLabel("");
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setIcon(new ImageIcon("resources/Icons/Word.png"));
		wordLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		wordLabel.setBounds(360, 110, 130, 40);
		add(wordLabel);

		levelNumberLabel = new JLabel("1");
		levelNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		levelNumberLabel.setBounds(150, 110, 146, 40);
		add(levelNumberLabel);

		wordNumberLabel = new JLabel("X of X");
		wordNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		wordNumberLabel.setBounds(500, 110, 144, 40);
		add(wordNumberLabel);

		newHighscoreLabel = new JLabel("");
		newHighscoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newHighscoreLabel.setIcon(new ImageIcon("resources/Icons/New Highscore.png"));
		newHighscoreLabel.setBounds(420, 451, 350, 40);
		add(newHighscoreLabel);
		newHighscoreLabel.setVisible(false);

		highscoreLabel = new JLabel("");
		highscoreLabel.setIcon(new ImageIcon("resources/Icons/Highscore.png"));
		highscoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		highscoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		highscoreLabel.setBounds(10, 500, 220, 40);
		add(highscoreLabel);

		highscoreNumberLabel = new JLabel("100");
		highscoreNumberLabel.setForeground(Color.BLACK);
		highscoreNumberLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
		highscoreNumberLabel.setBounds(260, 501, 150, 40);
		add(highscoreNumberLabel);

		hintLabel = new JLabel("");
		hintLabel.setIcon(new ImageIcon("resources/Icons/Hint.png"));
		hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hintLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		hintLabel.setBounds(120, 330, 140, 40);
		add(hintLabel);

		hintTextLabel = new JLabel();
		hintTextLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		hintTextLabel.setBounds(270, 330, 430, 40);
		add(hintTextLabel);

		disableHint();

	}

	public void setWord(int wordNumber, int total) {
		wordNumberLabel.setText(wordNumber + " of " + total);
	}

	public void setLevel(int level) {
		levelNumberLabel.setText(level+"");
	}

	public void setScore(int score) {
		scoreNumberLabel.setText(score+"");
	}

	public void setMultiplier(int multiplier) {
		multiplierNumberLabel.setText(multiplier + " x");
	}

	public void setRepeats(int repeats) {
		progressBar.setValue(repeats*10);
		progressBar.setString(repeats+"");
	}

	public void showHighscore() {
		newHighscoreLabel.setVisible(true);
	}

	public void setHighscore(int score) {
		highscoreNumberLabel.setText(score+"");
	}

	public void showHint(String word) {
		hintLabel.setVisible(true);
		hintTextLabel.setText(getHint(word));
		hintTextLabel.setVisible(true);
	}

	private String getHint(String word) {
		String hint = "";

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ('\'')|| word.charAt(i) == ' ') {
				hint += word.charAt(i) + " ";
			} else {
				hint += "_ ";
			}
		}
		
		return hint;
	}

	public void disableHint() {
		hintLabel.setVisible(false);
		hintTextLabel.setVisible(false);
	}
}