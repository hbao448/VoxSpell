package VoxSpell.media;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundBuzzer {
	
	/**
	 * Plays a correct sound
	 */
	public void playCorrect() {
		playClip("resources/Sounds/Correct.wav");
	}
	
	/**
	 * Plays an incorrect buzzing noise
	 */
	public void playIncorrect() {
		playClip("resources/Sounds/Incorrect.wav");
	}
	
	/**
	 * Plays a sound clip
	 * @param file the sound clip to play
	 */
	private void playClip(String file) {
		
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
