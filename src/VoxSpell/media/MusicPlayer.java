package VoxSpell.media;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import VoxSpell.gui.Quiz;

public class MusicPlayer{

	private Clip clip;
	private long currentLength;

	/**
	 * Create a music player object that plays either the background music or the music reward
	 * @param reward
	 */
	public MusicPlayer(boolean reward) {
		if (reward) {
			playReward();
		} else {
			playSong();
		}
	}
	
	/**
	 * Plays the background music in an infinite loop
	 */
	private void playSong() {

		String soundName = "resources/Sounds/BGM.wav";
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float)-27.5);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
		}
	}

	/**
	 * Toggles the playback of the song
	 */
	public void togglePlayback() {
		if (clip.isActive()){
			currentLength = clip.getMicrosecondPosition();
			clip.stop();
		} else {
			clip.setMicrosecondPosition(currentLength);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	/**
	 * Returns whether or not the song is playing
	 * @return
	 */
	public boolean isPlaying() {
		return clip.isActive();
	}
	
	/**
	 * Plays the music reward
	 */
	private void playReward() {
		
		String soundName = "resources/Sounds/Music Reward.wav";
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) -20);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a listener to the clip to change the quiz button's text if the song ends
	 * @param quiz
	 */
	public void setQuiz(Quiz quiz) {
		clip.addLineListener(new LineListener(){
			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					quiz.toggleSongText(false);
		        }
			}
		});
	}
	
	/**
	 * Changes the volume of the song
	 * @param vol
	 */
	public void setVolume(int vol) {
		FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float ratio = (float)vol/100;
		volume.setValue(-45+(ratio)*35);
	}
}
