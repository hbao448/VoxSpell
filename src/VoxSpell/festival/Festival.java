package VoxSpell.festival;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import VoxSpell.gui.Quiz;

/**
 * This class will be used to interact with festival through the command line
 * and also change the settings of the text to speech, such as speech voice and speed
 * @author Hunter
 *
 */
public class Festival {

	private String _voice;
	private double _selectedSpeed;
	private String _defaultSpeed = "(Parameter.set 'Duration_Stretch 1.0)";
	private Quiz _quiz;

	/**
	 * Returns a festival object with default values for speed and voice
	 */
	public Festival() {
		setSpeed(1.0);
		setVoice("kal_diphone");
	}

	/**
	 * Sets the voice to use when speaking
	 * @param voice voice to use
	 */
	public void setVoice(String voice) {
		_voice = voice;
	}
	
	/**
	 * Returns the name of the currently selected voice
	 * @return name of current voice
	 */
	public String getVoice() {
		return _voice;
	}

	/**
	 * Sets the speed that Festival should talk at
	 * @param speed speed for Festival to speak at
	 */
	public void setSpeed(double speed) {
		_selectedSpeed = speed;
	}
	
	/**
	 * Returns the currently selected speech speed
	 * @return current speed of speech
	 */
	public double getSpeed() {
		return _selectedSpeed;
	}

	/**
	 * Saves a reference to the quiz that Festival is used in
	 * @param quiz
	 */
	public void setQuiz(Quiz quiz) {
		_quiz = quiz;
	}

	/**
	 * Returns the string that the Festival command line will accept in order to
	 * change voices in Festival
	 * @return Festival-friendly voice string
	 */
	private String voiceString() {
		return "(voice_" + _voice + ")";
	}

	/**
	 * Returns the string that the Festival command line will accept in order to
	 * change voice speed in Festival
	 * @return Festival-friendly speed string
	 */
	private String speedString() {
		return "(Parameter.set 'Duration_Stretch " + 1/_selectedSpeed +")";
	}

	/**
	 * Calls Festival on the command line to give an example sentence using
	 * the currently selected settings
	 * @param voiceName The name of the voice
	 */
	public void test(String voiceName) {
		ArrayList<String> texts = new ArrayList<String>();
		texts.add("Hello, I am the " + voiceName);
		textToSpeech(texts);
	}
	
	/**
	 * This method turns a list of words to be spoken into speech by calling
	 * festival using bash
	 * 
	 * @param texts The list of strings to be spoken in order
	 */
	public void textToSpeech(ArrayList<String> texts) {
		
		BufferedWriter bw = null;

		try {

			// Creates a new scm file to write the tts speech on
			bw = new BufferedWriter(new FileWriter(".text.scm", false));

			// If the user has selected a voice, then write the voice into the scm file

			bw.write(voiceString());
			bw.newLine();


			// For every line, specify the speed for the tts to speak at. Which is 1x for "Correct" and "Incorrect"
			// text. Otherwise it is at the speed defined by the user
			for (String text : texts) {

				if ((text.equals("Please spell, ") || text.equals("Incorrect, please try again"))) {
					bw.write(_defaultSpeed);
					bw.newLine();

				} else  {
					bw.write(speedString());
					bw.newLine();

				}

				bw.write("(SayText \"" + text + "\")");
				bw.newLine();


			}


		} catch (IOException e) {

		} finally {
			try {
				bw.close();
			} catch (IOException e) {

			}
		}

		// Starts a new worker instance and executes it
		Speaker worker = new Speaker(_quiz);
		worker.execute();

	}

}
