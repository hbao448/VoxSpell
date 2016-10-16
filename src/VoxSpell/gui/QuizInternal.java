package VoxSpell.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

@SuppressWarnings("serial")
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
		//setBackground(new Color(100, 149, 237));
		setLayout(null);
		
		progressBar = new JProgressBar();
		progressBar.setValue(34);
		progressBar.setStringPainted(true);
		progressBar.setBounds(215, 169, 146, 22);
		add(progressBar);
		
		lblRepeatsLeft = new JLabel("Repeats Left");
		lblRepeatsLeft.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblRepeatsLeft.setBounds(217, 133, 144, 25);
		add(lblRepeatsLeft);
		
		lblMultiplier = new JLabel("Score Multiplier");
		lblMultiplier.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMultiplier.setBounds(215, 358, 162, 25);
		add(lblMultiplier);
		
		lblScore = new JLabel("Score");
		lblScore.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblScore.setBounds(215, 434, 110, 22);
		add(lblScore);
		
		lblMultiplierNumber = new JLabel("2 x");
		lblMultiplierNumber.setForeground(Color.BLACK);
		lblMultiplierNumber.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblMultiplierNumber.setBounds(403, 342, 93, 50);
		add(lblMultiplierNumber);
		
		lblScoreNumber = new JLabel("20000");
		lblScoreNumber.setForeground(Color.BLACK);
		lblScoreNumber.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblScoreNumber.setBounds(403, 420, 138, 50);
		add(lblScoreNumber);
		
		lblLevel = new JLabel("Level");
		lblLevel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLevel.setBounds(223, 85, 59, 26);
		add(lblLevel);
		
		lblWord = new JLabel("Word");
		lblWord.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWord.setBounds(443, 169, 78, 22);
		add(lblWord);
		
		lblLevelnumber = new JLabel("LevelNumber");
		lblLevelnumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLevelnumber.setBounds(292, 80, 146, 37);
		add(lblLevelnumber);
		
		lblWordnumber = new JLabel("WordNumber");
		lblWordnumber.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblWordnumber.setBounds(531, 167, 144, 24);
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
