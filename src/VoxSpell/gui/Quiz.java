package VoxSpell.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import VoxSpell.festival.Bash;
import VoxSpell.festival.Festival;
import VoxSpell.media.MusicPlayer;
import VoxSpell.media.SoundBuzzer;
import VoxSpell.words.Level;
import VoxSpell.words.ScoreKeeper;
import VoxSpell.words.Word;
import VoxSpell.words.Wordlist;
import VoxSpell.media.VideoMaker;

@SuppressWarnings("serial")
public class Quiz extends AbstractBackgroundScreen{

	private MainFrame _mainFrame;
	private JTextField input = new JTextField();
	private JButton restart = new JButton("Restart");
	protected JButton submit = new JButton("Submit");
	private JButton close = new JButton("Main menu");
	private JButton nextLevel = new JButton("Next level");
	private JButton videoReward = new JButton("Video");
	private Word currentWord;
	private Level words;
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
	private VideoMaker videoMaker;

	public Quiz(MainFrame mainFrame, int level, String name) {

		_name = name;
		_mainFrame = mainFrame;
		_level = level;
		_festival = _mainFrame.getSettings().getFestival();
		_festival.setQuiz(this);
		scorer = new ScoreKeeper(_mainFrame.getSettings());
		wordlist = _mainFrame.getSettings().getWordlist();
		words = wordlist.getLevel(_level);
		setUp();

	}

	/**
	 * This method starts the quiz and also begins the generation of a custom video
	 */
	public void startQuiz() {

		Bash.bashCommand("rm -f .text.scm");

		// Determines the number of words to be quizzed, which is either
		// 10 or the number of words in the list, if the list has less than 10
		// words
		size = words.size() < 10 ? words.size() : 10;

		words.reset();
		numberCorrect = 0;
		testNum = 1;
		videoMaker = new VideoMaker(_level, _name);
		videoMaker.execute();
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
			repeated = false;

			currentWord = words.nextWord();

			// Speaks the word selected
			previousCorrect.add("Please spell, ");
			previousCorrect.add(currentWord.getWord());
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
				// If they are on the last level, then they are only able to play the video reward
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
							+ " words correct out of " + size + ", you may choose to play a video reward! You have passed the final level, congratulations!",
							"Pass", JOptionPane.INFORMATION_MESSAGE);

				}
			}

			restart.setEnabled(true);

		}
	}

	/**
	 * This method creates the Panel to display the quiz on and also sets up
	 * the buttons with their ActionListeners
	 * 
	 */
	private void setUp() {

		buzzer = new SoundBuzzer();

		// Prompts the user if they want to sve their score and then returns to the main menu
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to save your score?",
						"Exit quiz", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {

					scorer.saveScore(_name, _level, score);
				}

				//If the music reward is playing then stop it
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
				// Adds 5 to the number of repeats that the user has
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

				//Stop the music before playing the video
				if (player.isPlaying()) {
					player.togglePlayback();
					_mainFrame.toggleButton(false);
				}

				if (rewardPlayer != null && rewardPlayer.isPlaying()) {
					rewardPlayer.togglePlayback();
					toggleSongText(false);
				}

				VideoPlayer video = new VideoPlayer(Quiz.this, "resources/Videos/Reward" + _level + ".avi", _mainFrame);
				_mainFrame.setScreen(video);
				video.play();

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
				words = wordlist.getLevel(_level);

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
				input.requestFocus();
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

		//Creates a new action listener that stops the music and changes the text on the button if the background music is started
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

		//Sets up the options panel
		song.setEnabled(false);
		options.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		options.setOpaque(false);

		options.add(close);
		options.add(restart);
		options.add(nextLevel);
		options.add(videoReward);
		options.add(song);

		nextLevel.setEnabled(false);
		videoReward.setEnabled(false);
		restart.setEnabled(false);
		close.setEnabled(false);

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
				close.setEnabled(false);
				submit.setEnabled(false);
				repeat.setEnabled(false);
				spellcheck(input.getText());
				// Checks that the user's input in the JTextField is spelled
				// correctly

				if (currentWord.isCorrect()) {
				} else {
					if (currentWord.getAttempts() == 1) {
						// If they have one failed attempt, then they are
						// allowed to spell the word again
						ArrayList<String> text = new ArrayList<String>();
						text.add("Incorrect, please try again");

						text.add(currentWord.getWord());
						text.add(currentWord.getWord());
						_festival.textToSpeech(text);

					}
				}

				//Updates the number correct
				if (currentWord.isCorrect()) {
					numberCorrect++;
				}

				// Clears the JTextField
				input.setText("");

				// Goes to the next word once the user gets the current word
				// correct or fails twice
				if (currentWord.isCorrect() || currentWord.getAttempts() == 2) {
					testNum++;
					if (!currentWord.isCorrect()) {
						incorrectWords.add(currentWord.getWord());
					}
					test();
				}

			}

		});

		JPanel panel = new JPanel();
		panel.setBounds(130, 260, 600, 33);
		internal.add(panel);
		input.setBounds(0, 0, 353, 33);

		// Disables editing of the JTextArea

		input.setPreferredSize(new Dimension(250, 30));

		submit.setEnabled(false);

		JPanel quizOptions = new JPanel();
		quizOptions.setBounds(353, -4, 271, 33);
		quizOptions.setOpaque(false);
		quizOptions.add(submit);

		// Plays the word once more once clicked
		repeat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setRepeats(getRepeats() - 1);
				updateInternals();
				repeated = true;
				close.setEnabled(false);
				repeat.setEnabled(false);
				submit.setEnabled(false);
				ArrayList<String> text = new ArrayList<String>();
				text.add(currentWord.getWord());
				_festival.textToSpeech(text);
				input.requestFocus();
			}

		});

		repeat.setEnabled(false);

		quizOptions.add(repeat);

		//Shows a hint on how to spell the word when pressed
		hint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				input.requestFocus();
				hint.setEnabled(false);
				multiplier = 1;
				updateInternals();
				internal.showHint(currentWord.getWord());
			}

		});

		quizOptions.add(hint);

		panel.setLayout(null);
		panel.setOpaque(false);

		//Sets up the icons and labels
		panel.add(input);
		panel.add(quizOptions);
		playerLabel.setIcon(new ImageIcon("resources/Icons/Player.png"));
		playerLabel.setBounds(10, 10, 160, 40);

		internal.add(playerLabel);
		playerName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		playerName.setBounds(180, 10, 380, 40);
		playerName.setText(_name);

		internal.add(playerName);
		wordlistLabel.setIcon(new ImageIcon("resources/Icons/Wordlist.png"));
		wordlistLabel.setBounds(10, 60, 200, 40);

		internal.add(wordlistLabel);
		wordlistName.setFont(new Font("Tahoma", Font.PLAIN, 30));
		wordlistName.setBounds(220, 60, 400, 40);
		wordlistName.setText(wordlist.getName());

		internal.add(wordlistName);
		add(options);
	}

	/**
	 * This method spellchecks the word attempt and plays a sound based on if they got it correct
	 * @param text
	 */
	private void spellcheck(String text) {

		boolean spelled = currentWord.spellcheck(text);

		if (spelled) {
			buzzer.playCorrect();
			score += _level*multiplier;
			multiplier++;
		} else {
			buzzer.playIncorrect();
			multiplier = 1;
			updateInternals();
		}

	}

	/**
	 * Updates the numbers on the interface
	 */
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

	/**
	 * Returns the number of repeats left
	 * @return
	 */
	public int getRepeats() {
		return repeats;
	}

	/**
	 * Sets the available number of repeats
	 * @param repeats
	 */
	public void setRepeats(int repeats) {
		this.repeats = repeats;
	}

	/**
	 * Toggles the submit button
	 * @param enabled
	 */
	public void toggleSubmit(boolean enabled) {
		submit.setEnabled(enabled);
	}

	/**
	 * Toggles the repeat button
	 * @param enabled
	 */
	public void toggleRepeat(boolean enabled) {
		repeat.setEnabled(enabled);
	}

	/**
	 * Sets the submit button to work with enter presses
	 */
	public void setSubmitAsDefault() {
		_mainFrame.getRootPane().setDefaultButton(submit);
	}

	/**
	 * Toggles the text shown in the music button
	 * @param playing
	 */
	public void toggleSongText(boolean playing) {
		if (playing) {
			song.setText("Stop");
		} else {
			song.setText("Music");
			song.setEnabled(false);
		}
	}

	/**
	 * Returns the current word
	 * @return
	 */
	public Word getWord() {
		return currentWord;
	}

	/**
	 * This method enables the main menu button
	 */
	public void enableMenu() {
		close.setEnabled(true);
	}
}
