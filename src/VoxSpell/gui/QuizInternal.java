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
	private JLabel highscoreLabel;

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
		repeatsLeftLabel.setIcon(new ImageIcon("resources/Repeats.png"));
		repeatsLeftLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		repeatsLeftLabel.setBounds(150, 200, 300, 40);
		add(repeatsLeftLabel);
		
		multiplierLabel = new JLabel("");
		multiplierLabel.setHorizontalAlignment(SwingConstants.CENTER);
		multiplierLabel.setIcon(new ImageIcon("resources/Multiplier.png"));
		multiplierLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		multiplierLabel.setBounds(10, 400, 360, 40);
		add(multiplierLabel);
		
		ScoreLabel = new JLabel("");
		ScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ScoreLabel.setIcon(new ImageIcon("resources/Score.png"));
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
		scoreNumberLabel.setBounds(160, 450, 150, 40);
		add(scoreNumberLabel);
		
		levelLabel = new JLabel("");
		levelLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelLabel.setIcon(new ImageIcon("resources/Level.png"));
		levelLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		levelLabel.setBounds(10, 110, 130, 40);
		add(levelLabel);
		
		wordLabel = new JLabel("");
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setIcon(new ImageIcon("resources/Word.png"));
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
		
		highscoreLabel = new JLabel("");
		highscoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
		highscoreLabel.setIcon(new ImageIcon("resources/Highscore.png"));
		highscoreLabel.setBounds(320, 450, 350, 40);
		add(highscoreLabel);

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
}