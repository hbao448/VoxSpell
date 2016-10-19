package VoxSpell.festival;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import VoxSpell.gui.Quiz;

public class Festival {

	private String _voice;
	private double _selectedSpeed;
	private String _defaultSpeed = "(Parameter.set 'Duration_Stretch 1.0)";
	private Quiz _quiz;

	public Festival() {
		setSpeed(1.0);
		setVoice("kal_diphone");
	}

	public void setVoice(String voice) {
		_voice = voice;
	}
	
	public String getVoice() {
		return _voice;
	}

	public void setSpeed(double speed) {
		_selectedSpeed = speed;
	}
	
	public double getSpeed() {
		return _selectedSpeed;
	}

	public void setQuiz(Quiz quiz) {
		_quiz = quiz;
	}

	private String voiceString() {
		return "(voice_" + _voice + ")";
	}

	private String speedString() {
		return "(Parameter.set 'Duration_Stretch " + 1/_selectedSpeed +")";
	}

	public void test(String voiceName) {
		ArrayList<String> texts = new ArrayList<String>();
		texts.add("Hello, I am the " + voiceName);
		textToSpeech(texts);
	}
	
	/**
	 * This method turns a list of words to be spoken into speech by calling
	 * festival using bash
	 * 
	 * Modified A2 code
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
			// Original code by Hunter
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
