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
	private Quiz quiz;

	public MusicPlayer(boolean reward) {
		if (reward) {
			playReward();
		} else {
			playSong();
		}
	}
	
	private void playSong() {

		String soundName = "resources/bgm.wav";
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-25);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
		}
	}

	public void togglePlayback() {

		if (clip.isActive()){
			currentLength = clip.getMicrosecondPosition();
			clip.stop();
		} else {
			clip.setMicrosecondPosition(currentLength);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public boolean isPlaying() {
		return clip.isActive();
	}
	
	private void playReward() {
		
		String soundName = "resources/music reward.wav";
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-25);
			clip.start();
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
		
		clip.addLineListener(new LineListener(){

			@Override
			public void update(LineEvent event) {
				if (event.getType() == LineEvent.Type.STOP) {
					quiz.toggleSongText(false);
		        }
			}
		});
	}
}
