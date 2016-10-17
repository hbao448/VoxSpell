package VoxSpell.gui;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import VoxSpell.festival.Bash;
import VoxSpell.festival.Festival;
import VoxSpell.media.MusicPlayer;
import VoxSpell.media.SoundBuzzer;
import VoxSpell.words.ScoreKeeper;
import VoxSpell.words.Wordlist;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;

public class Quiz extends AbstractScreen{

	private MainFrame _mainFrame;
	private JTextField input = new JTextField();
	private JButton restart = new JButton("Restart");
	protected JButton submit = new JButton("Submit");
	private JButton close = new JButton("Main menu");
	private JButton nextLevel = new JButton("Next level");
	private JButton videoReward = new JButton("Video");
	private String currentWord = "";
	private int attempts;
	private ArrayList<String> words = new ArrayList<String>();
	private ArrayList<String> previousWords;
	private ArrayList<String> incorrectWords = new ArrayList<String>();
	private int size;
	private int testNum;
	private int numberCorrect;
	private ArrayList<String> previousCorrect = new ArrayList<String>();
	protected JButton repeat = new JButton("Repeat");
	protected boolean repeated;
	private int score;
	private int highscore;
	private int multiplier = 1;
	private QuizInternal internal;
	private int _level;
	private boolean correct;
	private int repeats = 10;
	private MusicPlayer player;
	private MusicPlayer rewardPlayer;
	private JButton song = new JButton("Music");
	private Festival _festival;
	private SoundBuzzer buzzer;
	private ScoreKeeper scorer;
	private Wordlist wordlist;
	protected ActionListener al;
	private String _name;
	private final JLabel playerLabel = new JLabel();
	private final JLabel playerName = new JLabel();
	private final JLabel wordlistLabel = new JLabel();
	private final JLabel wordlistName = new JLabel();
	private JButton hint = new JButton("Hint");

	public Quiz(MainFrame mainFrame, int level, String name) {

		_name = name;
		_mainFrame = mainFrame;
		_level = level;
		_festival = _mainFrame.getSettings().getFestival();
		_festival.setQuiz(this);
		scorer = new ScoreKeeper(_mainFrame.getSettings());
		wordlist = _mainFrame.getSettings().getWordlist();
		setUp();

	}

	/**
	 * This method creates the JFrame that contains the quiz GUI if it does not
	 * already exist, prompts the user for a wordlist and then reads that list
	 * and starts the quiz. An error message is shown if the wordlist file is
	 * not called "wordlist" when in quiz mode or if the wordlist files are
	 * empty
	 * 
	 * Modified A2 code
	 */
	public void startQuiz() {

		Bash.bashCommand("rm -f .text.scm");

		words = _mainFrame.getSettings().getWordlist().readLevel(_level);


		// Displays an error message if the file is empty
		if (words.isEmpty()) {

			JOptionPane.showMessageDialog(new JFrame(), "Error, no words in level " + _level, "Error",
					JOptionPane.ERROR_MESSAGE);
			_mainFrame.setScreen(new MainMenu(_mainFrame));


		} 

		// Determines the number of words to be quizzed, which is either
		// 10 or the number of words in the list, if the list has less than 10
		// words
		size = words.size() < 10 ? words.size() : 10;
		previousWords = new ArrayList<String>();

		numberCorrect = 0;
		testNum = 0;
		testNum = 1;
		test();

	}



	/**
	 * This method randomly selects a word from the wordlist that has not
	 * already been tested in the current quiz uses textToSpeech to speak out
	 * the word for the user to spell
	 * 
	 * Reused A2 code
	 */
	private void test() {

		if (testNum <= size) {
			hint.setEnabled(true);
			internal.disableHint();
			updateInternals();
			setCorrect(false);
			setAttempts(0);
			repeated = false;

			Random rand = new Random();
			int wordNumber = (Math.abs(rand.nextInt()) % words.size());

			currentWord = words.get(wordNumber);

			while (previousWords.contains(currentWord)) {
				wordNumber = (Math.abs(rand.nextInt()) % words.size());
				currentWord = words.get(wordNumber);
			}
			// Adds the current word to the list of quizzed words, so that it
			// cannot
			// be selected again
			previousWords.add(currentWord);

			// Speaks the word selected
			previousCorrect.add("Please spell, ");
			previousCorrect.add(currentWord);
			_festival.textToSpeech(previousCorrect);
			previousCorrect.clear();
			input.requestFocus();

		} else {
			// Once the quiz is done, then the restart button is enabled
			_festival.textToSpeech(previousCorrect);
			previousCorrect.clear();
			testNum = size;
			updateInternals();
			hint.setEnabled(false);

			// The user is given an option to see the words that they failed if they do not pass a level
			// Original code by Hunter
			if (numberCorrect < size - 1 || numberCorrect == 0) {
				int response = JOptionPane.showConfirmDialog(new JFrame(),
						"You have gotten " + numberCorrect
						+ " words correct out of " + size + ", would you like to see the words that you spelled incorrectly?",
						"Failure", JOptionPane.YES_NO_OPTION);

				if (response == JOptionPane.YES_OPTION) {

					JTextArea wrongWords = new JTextArea();

					for (String word : incorrectWords) {
						wrongWords.append(word + "\n");
					}

					wrongWords.setEditable(false);

					JScrollPane words = new JScrollPane(wrongWords);
					JOptionPane.showMessageDialog(null, words,
							"Your failed words", JOptionPane.INFORMATION_MESSAGE);
				}

				scorer.appendList(_level, numberCorrect, size);

			} else {

				scorer.appendList(_level, numberCorrect, size);

				// If they pass the level then the user can move on to the next level or play a video reward
				// If they are on the last level, then they are able to play the bonus video reward
				videoReward.setEnabled(true);
				song.setEnabled(true);
				if (_level < wordlist.getMaxLevel()) {
					JOptionPane.showMessageDialog(new JFrame(),
							"You have gotten " + numberCorrect
							+ " words correct out of " + size + ", you may choose to play a video reward, or proceed directly to the next level",
							"Pass", JOptionPane.INFORMATION_MESSAGE);
					nextLevel.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"You have gotten " + numberCorrect
							+ " words correct out of " + size + ", you may choose to play the bonus video reward! You have passed the final level, congratulations!",
							"Pass", JOptionPane.INFORMATION_MESSAGE);

				}
			}


			restart.setEnabled(true);

		}

	}

	/**
	 * This method creates the JFrame to display the quiz on and also sets up
	 * the buttons with their ActionListeners
	 * 
	 * Modified A2 code
	 */
	private void setUp() {

		buzzer = new SoundBuzzer();

		// The quiz JFrame is disposed and the main menu is unhidden once the
		// user
		// chooses to go back
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to save your score?",
						"Exit quiz", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {

					scorer.saveScore(_name, _level, score);
				}

				if (rewardPlayer != null && rewardPlayer.isPlaying()) {
					rewardPlayer.togglePlayback();
					toggleSongText(false);
				}

				_mainFrame.removeSoundListener(al);
				_mainFrame.setScreen(new MainMenu(_mainFrame));

			}


		});

		restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Starts a new quiz and disables the restart button

				if (getRepeats() < 5) {
					setRepeats(getRepeats() + 5);
				} else {
					setRepeats(10);
				}

				incorrectWords.clear();
				startQuiz();
				nextLevel.setEnabled(false);
				videoReward.setEnabled(false);
				restart.setEnabled(false);
				if (song.getText().equals("Music")) {
					song.setEnabled(false);
				}
			}

		});

		// Sets up the listeners for reward and next level buttons if the quiz type is quiz

		videoReward.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				videoReward.setEnabled(false);

				if (player.isPlaying()) {
					player.togglePlayback();
					_mainFrame.toggleButton(false);
					_mainFrame.toggleSoundButton(false);
				}
				if (rewardPlayer != null && rewardPlayer.isPlaying()) {
					rewardPlayer.togglePlayback();
					toggleSongText(false);
				}

				if (_level < wordlist.getMaxLevel()) {
					@SuppressWarnings("unused")
					VideoPlayer video = new VideoPlayer(Quiz.this, "resources/big_buck_bunny_1_minute.avi", _mainFrame);
					_mainFrame.setScreen(video);
					video.play();
				} else {
					// The bonus video is played if the level is the final level
					VideoPlayer video = new VideoPlayer(Quiz.this, "resources/bonus_reward.avi", _mainFrame);
					_mainFrame.setScreen(video);
					video.play();
				}

			}

		});

		// The next level is started
		// Original code by Hunter
		nextLevel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nextLevel.setEnabled(false);
				videoReward.setEnabled(false);
				restart.setEnabled(false);

				if(song.getText().equals("Music")) {
					song.setEnabled(false);
				}

				incorrectWords.clear();
				_level++;

				if (getRepeats() < 5) {
					setRepeats(getRepeats() + 5);
				} else {
					setRepeats(10);
				}
				startQuiz();
			}

		});
		JPanel options = new JPanel();
		options.setBounds(0, 550, 800, 60);

		player = _mainFrame.getPlayer();
		song.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (song.getText().equals("Music")) {
					toggleSongText(true);
					if (player.isPlaying()) {
						_mainFrame.toggleButton(false);
						player.togglePlayback();
					}	
					rewardPlayer = new MusicPlayer(true);
					rewardPlayer.setQuiz(Quiz.this);
				} else {
					toggleSongText(false);
					rewardPlayer.togglePlayback();
				}
			}
		});

		al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (rewardPlayer != null && rewardPlayer.isPlaying()) {
					rewardPlayer.togglePlayback();
					toggleSongText(false);
				}
			}
		};

		_mainFrame.addSoundListener(al);

		//song.setEnabled(false);
		//panel.setBackground(new Color(100, 149, 237));
		options.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		options.setOpaque(false);

		options.add(close);
		options.add(restart);
		//options.setBackground(new Color(100, 149, 237));

		// If the quiz type is quiz, then the options for the quiz include a next level button and
		// video reward button
		// by Hunter

		options.add(nextLevel);
		options.add(videoReward);
		options.add(song);

		nextLevel.setEnabled(false);
		//videoReward.setEnabled(false);
		restart.setEnabled(false);

		internal = new QuizInternal();
		internal.setBounds(0,0, 800, 550);
		setLayout(null);
		add(internal);
		highscore = scorer.getHighscore(_name, wordlist.getName());
		internal.setHighscore(highscore);

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input.requestFocus();
				submit.setEnabled(false);
				repeat.setEnabled(false);
				// Checks that the user's input in the JTextField is spelled
				// correctly
				setCorrect(spellcheck(input.getText().toLowerCase()));

				if (isCorrect()) {
					//previousCorrect.add("Correct");

					/* _mainFrame.appendList(currentWord, attempts, true); */
				} else {
					if (getAttempts() == 1) {
						// If they have one failed attempt, then they are
						// allowed to spell the word again
						ArrayList<String> text = new ArrayList<String>();
						text.add("Incorrect, please try again");

						text.add(currentWord);
						text.add(currentWord);
						_festival.textToSpeech(text);

					} else {
						// Once they fail two times, the word is considered
						// failed
						//previousCorrect.add("Incorrect");


						scorer.appendFailed(currentWord, _level);

					}
				}

				// If the user correctly spells a word, it is removed from their
				// failed list
				if (isCorrect()) {
					scorer.removeWord(currentWord + "\t" + _level);
					numberCorrect++;
				}

				// Clears the JTextField
				input.setText("");

				// Goes to the next word once the user gets the current word
				// correct
				// or fails twice
				if (isCorrect() || getAttempts() == 2) {
					testNum++;
					if (!isCorrect()) {
						incorrectWords.add(currentWord);
					}
					test();
				}

			}

		});

		JPanel panel = new JPanel();
		panel.setBounds(130, 260, 560, 33);
		internal.add(panel);
		input.setBounds(0, 0, 353, 33);

		// Disables editing of the JTextArea

		input.setPreferredSize(new Dimension(250, 30));

		submit.setEnabled(false);

		JPanel quizOptions = new JPanel();
		quizOptions.setBounds(353, -4, 210, 33);
		quizOptions.setOpaque(false);
		quizOptions.add(submit);

		// Plays the word once more once clicked
		repeat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setRepeats(getRepeats() - 1);
				updateInternals();
				repeated = true;
				repeat.setEnabled(false);
				submit.setEnabled(false);
				ArrayList<String> text = new ArrayList<String>();
				text.add(currentWord);
				_festival.textToSpeech(text);
				input.requestFocus();
			}

		});

		repeat.setEnabled(false);

		quizOptions.add(repeat);
		
		hint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input.requestFocus();
				hint.setEnabled(false);
				multiplier = 1;
				updateInternals();
				internal.showHint(currentWord);
			}
			
		});
		
		quizOptions.add(hint);
		
		panel.setLayout(null);
		panel.setOpaque(false);
		//quizOptions.setBackground(new Color(100, 149, 237));

		panel.add(input);
		panel.add(quizOptions);
		playerLabel.setIcon(new ImageIcon("resources/Player.png"));
		playerLabel.setBounds(10, 10, 160, 40);

		internal.add(playerLabel);
		playerName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		playerName.setBounds(180, 10, 380, 40);
		playerName.setText(_name);

		internal.add(playerName);
		wordlistLabel.setIcon(new ImageIcon("resources/Wordlist.png"));
		wordlistLabel.setBounds(10, 60, 200, 40);

		internal.add(wordlistLabel);
		wordlistName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		wordlistName.setBounds(220, 60, 400, 40);
		wordlistName.setText(wordlist.getName());

		internal.add(wordlistName);
		add(options);

		/*submit.setBackground(new Color(255, 255, 0));
		repeat.setBackground(new Color(255, 255, 0));
		close.setBackground(new Color(255, 255, 0));
		restart.setBackground(new Color(255, 255, 0));
		nextLevel.setBackground(new Color(255, 255, 0));
		videoReward.setBackground(new Color(255, 255, 0));
		song.setBackground(new Color(255, 255, 0));*/

		// Sets the submit button as the default one so that the enter button can be used to submit
		//JRootPane rootPane = SwingUtilities.getRootPane(submit); 

	}

	/**
	 * This method returns a boolean that represents if the user correctly
	 * spelled a word, and also increments the number of attempts that the user
	 * has used
	 * 
	 * Reused A2 code
	 * 
	 * @param text The user's attempt at the current word
	 * @return
	 */
	protected boolean spellcheck(String text) {
		setAttempts(getAttempts() + 1);

		boolean spelled = text.toLowerCase().equals(currentWord.toLowerCase());

		if (spelled) {
			buzzer.playCorrect();
			score += _level*multiplier;
			multiplier++;
		} else {
			buzzer.playIncorrect();
			multiplier = 1;
			updateInternals();
		}

		return text.toLowerCase().equals(currentWord.toLowerCase());
	}

	private void updateInternals() {
		internal.setLevel(_level);
		internal.setWord(testNum, size);
		internal.setMultiplier(multiplier);
		internal.setScore(score);
		internal.setRepeats(getRepeats());
		if (score > highscore) {
			internal.setHighscore(score);
			internal.showHighscore();
		}
	}

	public int getRepeats() {
		return repeats;
	}

	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public void toggleSubmit(boolean enabled) {
		submit.setEnabled(enabled);
	}

	public void toggleRepeat(boolean enabled) {
		repeat.setEnabled(enabled);
	}

	public void setSubmitAsDefault() {
		_mainFrame.getRootPane().setDefaultButton(submit);
	}

	public void toggleSongText(boolean playing) {
		if (playing) {
			song.setText("Stop");
		} else {
			song.setText("Music");
			song.setEnabled(false);
		}
	}
}
