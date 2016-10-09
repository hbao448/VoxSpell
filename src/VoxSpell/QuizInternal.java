package VoxSpell;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;

public class QuizInternal extends JPanel {

	private JProgressBar progressBar;
	private JLabel lblRepeatsLeft;
	private JLabel lblMultiplier;
	private JLabel lblScore;
	private JLabel lblMultiplierNumber;
	private JLabel lblScoreNumber;
	private JLabel lblLevel;
	private JLabel lblWord;
	private JLabel lblLevelnumber;
	private JLabel lblWordnumber;

	/**
	 * Create the panel.
	 */
	public QuizInternal() {
		setBackground(new Color(100, 149, 237));
		setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setValue(34);
		progressBar.setStringPainted(true);
		progressBar.setBounds(50, 75, 146, 22);
		add(progressBar);
		
		lblRepeatsLeft = new JLabel("Repeats Left");
		lblRepeatsLeft.setBounds(52, 50, 93, 14);
		add(lblRepeatsLeft);
		
		lblMultiplier = new JLabel("Score Multiplier");
		lblMultiplier.setBounds(50, 136, 132, 14);
		add(lblMultiplier);
		
		lblScore = new JLabel("Score");
		lblScore.setBounds(50, 226, 46, 14);
		add(lblScore);
		
		lblMultiplierNumber = new JLabel("2 x");
		lblMultiplierNumber.setForeground(new Color(255, 255, 0));
		lblMultiplierNumber.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblMultiplierNumber.setBounds(238, 120, 93, 50);
		add(lblMultiplierNumber);
		
		lblScoreNumber = new JLabel("20000");
		lblScoreNumber.setForeground(new Color(255, 255, 0));
		lblScoreNumber.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblScoreNumber.setBounds(238, 212, 138, 50);
		add(lblScoreNumber);
		
		lblLevel = new JLabel("Level");
		lblLevel.setBounds(238, 50, 46, 14);
		add(lblLevel);
		
		lblWord = new JLabel("Word");
		lblWord.setBounds(238, 83, 46, 14);
		add(lblWord);
		
		lblLevelnumber = new JLabel("LevelNumber");
		lblLevelnumber.setBounds(294, 50, 82, 14);
		add(lblLevelnumber);
		
		lblWordnumber = new JLabel("WordNumber");
		lblWordnumber.setBounds(294, 83, 82, 14);
		add(lblWordnumber);

	}
	
	public void setWord(int wordNumber, int total) {
		lblWordnumber.setText(wordNumber + " of " + total);
	}
	
	public void setLevel(int level) {
		lblLevelnumber.setText(level+"");
	}
	
	public void setScore(int score) {
		lblScoreNumber.setText(score+"");
	}
	
	public void setMultiplier(int multiplier) {
		lblMultiplierNumber.setText(multiplier + " x");
	}
	
	public void setRepeats(int repeats) {
		progressBar.setValue(repeats*10);
		progressBar.setString(repeats+"");
	}
}
