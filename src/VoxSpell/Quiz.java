package VoxSpell;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class Quiz {

	private Spelling_Aid _spelling_Aid;
	private JTextField input = new JTextField();
	private JButton restart = new JButton("Restart");
	protected JButton submit = new JButton("Submit");
	private JButton close = new JButton("Main menu");
	private JButton nextLevel = new JButton("Next level");
	private JButton videoReward = new JButton("Video");
	private String currentWord = "";
	protected int attempts;
	private ArrayList<String> words = new ArrayList<String>();
	private ArrayList<String> previousWords;
	private ArrayList<String> incorrectWords = new ArrayList<String>();
	private int size;
	private int testNum;
	protected JFrame frame;
	private int numberCorrect;
	private ArrayList<String> previousCorrect = new ArrayList<String>();
	protected JButton repeat = new JButton("Repeat");
	protected boolean repeated;
	private int score;
	private int multiplier = 1;
	private QuizInternal internal;
	private int _level;
	protected boolean correct;
	protected int repeats = 10;
	private SongWorker songWorker;
	private JButton song = new JButton("Music");

	public Quiz(Spelling_Aid spelling_Aid, int level) {

		_spelling_Aid = spelling_Aid;
		_level = level;

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

		Spelling_Aid.bashCommand("rm -f .text.scm");

		if (frame == null) {
			setUp();
		}

		words = _spelling_Aid.readLevel(_spelling_Aid.wordlist, _level);


		// Displays an error message if the file is empty
		if (words.isEmpty()) {

			JOptionPane.showMessageDialog(new JFrame(), "Error, no words in level " + _level, "Error",
					JOptionPane.ERROR_MESSAGE);
			_spelling_Aid.setVisible(true);
			frame.dispose();


		} 

		frame.setVisible(true);

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
			updateInternals();
			correct = false;
			attempts = 0;
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
			previousCorrect.add(currentWord);
			_spelling_Aid.textToSpeech(previousCorrect);
			previousCorrect.clear();

		} else {
			// Once the quiz is done, then the restart button is enabled
			_spelling_Aid.textToSpeech(previousCorrect);
			previousCorrect.clear();
			testNum = size;
			updateInternals();

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

				_spelling_Aid.appendList(_level, numberCorrect);

			} else {

				_spelling_Aid.appendList(_level, numberCorrect);

				// If they pass the level then the user can move on to the next level or play a video reward
				// If they are on the last level, then they are able to play the bonus video reward
				videoReward.setEnabled(true);
				song.setEnabled(true);
				if (_level < _spelling_Aid.maxLevel) {
					JOptionPane.showMessageDialog(new JFrame(),
							"You have gotten " + numberCorrect
							+ " words correct out of 10, you may choose to play a video reward, or proceed directly to the next level",
							"Pass", JOptionPane.INFORMATION_MESSAGE);
					nextLevel.setEnabled(true);

				} else {
					JOptionPane.showMessageDialog(new JFrame(),
							"You have gotten " + numberCorrect
							+ " words correct out of 10, you may choose to play the bonus video reward! You have passed the final level, congratulations!",
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


		frame = new JFrame("Quiz");


		frame.setSize(500, 450);
		frame.setLocationRelativeTo(null);

		// The quiz JFrame is disposed and the main menu is unhidden once the
		// user
		// chooses to go back
		close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to return to the main menu?",
						"Exit quiz", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {

					String Name = "";

					while (Name != null && (Name.length() < 1 || Name.length() > 15)) {
						Name = JOptionPane.showInputDialog("Please enter the name to save the score under (1 to 15 characters long), or Cancel to exit without saving");
					}

					if (Name != null) {
						_spelling_Aid.saveScore(Name, _level, score);
					}

					frame.dispose();
					_spelling_Aid.setVisible(true);
				}
			}

		});

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				submit.setEnabled(false);
				repeat.setEnabled(false);
				// Checks that the user's input in the JTextField is spelled
				// correctly
				correct = spellcheck(input.getText().toLowerCase());

				if (correct) {
					previousCorrect.add("Correct");

					/* _spelling_Aid.appendList(currentWord, attempts, true); */
				} else {
					if (attempts == 1) {
						// If they have one failed attempt, then they are
						// allowed to spell the word again
						ArrayList<String> text = new ArrayList<String>();
						text.add("Incorrect, please try again");

						text.add(currentWord);
						text.add(currentWord);
						_spelling_Aid.textToSpeech(text);

					} else {
						// Once they fail two times, the word is considered
						// failed
						previousCorrect.add("Incorrect");


						_spelling_Aid.appendFailed(currentWord, _level);

					}
				}

				// If the user correctly spells a word, it is removed from their
				// failed list
				if (correct) {
					_spelling_Aid.removeWord(currentWord + "\t" + _level);
					numberCorrect++;
				}

				// Clears the JTextField
				input.setText("");

				// Goes to the next word once the user gets the current word
				// correct
				// or fails twice
				if (correct || attempts == 2) {
					testNum++;
					if (!correct) {
						incorrectWords.add(currentWord);
					}
					test();
				}

			}

		});

		restart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Starts a new quiz and disables the restart button

				if (repeats < 5) {
					repeats += 5;
				} else {
					repeats = 10;
				}

				incorrectWords.clear();
				startQuiz();
				nextLevel.setEnabled(false);
				videoReward.setEnabled(false);
				restart.setEnabled(false);
				song.setEnabled(false);
			}

		});

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// unhides the main menu when the x button is pressed
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to return to the main menu?",
						"Exit quiz", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {

					String Name = "";

					while (Name != null && (Name.length() < 1 || Name.length() > 15)) {
						Name = JOptionPane.showInputDialog("Please enter the name to save the score under (1 to 15 characters long), or Cancel to exit without saving");
					}

					_spelling_Aid.saveScore(Name, _level, score);

					frame.dispose();
					_spelling_Aid.setVisible(true);
				}

			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});

		// Sets up the listeners for reward and next level buttons if the quiz type is quiz

		videoReward.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				videoReward.setEnabled(false);
				if (_level < _spelling_Aid.maxLevel) {
					@SuppressWarnings("unused")
					VideoPlayer video = new VideoPlayer(Quiz.this, "resources/big_buck_bunny_1_minute.avi");
				} else {
					// The bonus video is played if the level is the final level
					@SuppressWarnings("unused")
					VideoPlayer video = new VideoPlayer(Quiz.this, "resources/bonus_reward.avi");
				}
				frame.setVisible(false);
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
				song.setEnabled(false);

				incorrectWords.clear();
				_level++;

				if (repeats < 5) {
					repeats += 5;
				} else {
					repeats = 10;
				}
				startQuiz();
			}

		});


		frame.setResizable(false);

		JPanel panel = new JPanel(new BorderLayout());
		JPanel options = new JPanel();

		// Disables editing of the JTextArea

		input.setPreferredSize(new Dimension(250, 30));

		submit.setEnabled(false);

		JPanel quizOptions = new JPanel();

		quizOptions.add(submit);

		// Plays the word once more once clicked
		repeat.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				repeats--;
				updateInternals();
				repeated = true;
				repeat.setEnabled(false);
				submit.setEnabled(false);
				ArrayList<String> text = new ArrayList<String>();
				text.add(currentWord);
				_spelling_Aid.textToSpeech(text);
			}

		});

		repeat.setEnabled(false);
		
		song.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				song.setEnabled(false);
				playSong();
			}
			
		});
		
		song.setEnabled(false);

		quizOptions.add(repeat);
		quizOptions.setBackground(new Color(100, 149, 237));

		panel.add(input, BorderLayout.CENTER);
		panel.add(quizOptions, BorderLayout.EAST);
		panel.setBackground(new Color(100, 149, 237));

		options.add(close);
		options.add(restart);
		options.setBackground(new Color(100, 149, 237));

		// If the quiz type is quiz, then the options for the quiz include a next level button and
		// video reward button
		// by Hunter

		options.add(nextLevel);
		options.add(videoReward);
		options.add(song);

		nextLevel.setEnabled(false);
		videoReward.setEnabled(false);
		restart.setEnabled(false);

		internal = new QuizInternal();

		frame.add(panel, BorderLayout.NORTH);
		frame.add(internal, BorderLayout.CENTER);
		frame.add(options, BorderLayout.SOUTH);

		submit.setBackground(new Color(255, 255, 0));
		repeat.setBackground(new Color(255, 255, 0));
		close.setBackground(new Color(255, 255, 0));
		restart.setBackground(new Color(255, 255, 0));
		nextLevel.setBackground(new Color(255, 255, 0));
		videoReward.setBackground(new Color(255, 255, 0));
		song.setBackground(new Color(255, 255, 0));
		
		// Sets the submit button as the default one so that the enter button can be used to submit
		frame.getRootPane().setDefaultButton(submit);

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
		attempts++;

		boolean spelled = text.toLowerCase().equals(currentWord.toLowerCase());

		if (spelled) {
			playCorrect();
			score += _level*multiplier;
			multiplier++;
		} else {
			playIncorrect();
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
		internal.setRepeats(repeats);
	}

	class Worker extends SwingWorker<Void,Void> {
		
		private String _command;
		
		public Worker(String command) {
			_command = command;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			Spelling_Aid.bashCommand(_command);
			return null;
		}
	}
	
	class SongWorker extends SwingWorker<Void,Void> {
		
		@Override
		protected Void doInBackground() throws Exception {
			Spelling_Aid.bashCommand("ffplay -i resources/music.mp3 -nodisp -autoexit -t 10");
			return null;
		}
	}
	
	private void playCorrect() {
		Worker worker = new Worker("ffplay -i resources/correct.wav -nodisp -autoexit");
		worker.execute();
	}

	private void playIncorrect() {
		
		Worker worker = new Worker("ffplay -i resources/incorrect.wav -nodisp -autoexit");
		worker.execute();
	}
	
	private void playSong() {
		
		songWorker = new SongWorker();
		songWorker.execute();

	}
}
