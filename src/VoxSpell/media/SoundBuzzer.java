package VoxSpell.media;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundBuzzer {
	
	private Clip clip;
	
	public SoundBuzzer() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void playCorrect() {
		playClip("resources/Sounds/Correct.wav");
	}
	
	public void playIncorrect() {
		playClip("resources/Sounds/Incorrect.wav");
	}
	
	private void playClip(String file) {
		
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			clip.close();
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
