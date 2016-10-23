package VoxSpell.gui;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class VideoPlayer extends AbstractScreen {

	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private Quiz _quiz;
	private MainFrame _mainFrame;
	private Timer timer;
	private EmbeddedMediaPlayer video;
	private String _fileName;
	private JButton pause;

	public VideoPlayer(Quiz quiz, String filename, MainFrame mainFrame) {

		_mainFrame = mainFrame;
		_quiz = quiz;
		_fileName = filename;

		setLayout(null);

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		mediaPlayerComponent.setBounds(0, 0, 800, 525);

		video = mediaPlayerComponent.getMediaPlayer();
		setLayout(null);

		add(mediaPlayerComponent);

		JPanel panel = new JPanel();
		panel.setBounds(0, 525, 800, 75);
		add(panel);
		panel.setLayout(null);

		pause = new JButton("");
		togglePlay(true);
		pause.setBounds(380, 30, 40, 40);
		panel.add(pause);
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (video.isPlaying()) {
					video.setPause(true);
					togglePlay(false);
				} else {
					video.setPause(false);
					togglePlay(true);
				}
			}
		});

		JButton exit = new JButton("");
		{
			ImageIcon icon = new ImageIcon("resources/Icons/Stop.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			exit.setIcon(icon);
		}
		exit.setBounds(470, 35, 30, 30);
		panel.add(exit);

		JProgressBar progress = new JProgressBar();
		progress.setBounds(0, 0, 800, 25);
		panel.add(progress);

		JButton rewind = new JButton("");
		rewind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				video.skip(-5000);
			}
		});
		rewind.setBounds(340, 35, 30, 30);
		{
			ImageIcon icon = new ImageIcon("resources/Icons/Back.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			rewind.setIcon(icon);
		}
		panel.add(rewind);

		JButton ffwd = new JButton("");
		ffwd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				video.skip(5000);
			}
		});
		ffwd.setBounds(430, 35, 30, 30);
		{
			ImageIcon icon = new ImageIcon("resources/Icons/Forward.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 
			ffwd.setIcon(icon);
		}
		panel.add(ffwd);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
				_mainFrame.setScreen(_quiz);
				_quiz.setSubmitAsDefault();
				_mainFrame.toggleSoundButton(true);
			}

		});

		timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = video.getTime();
				long totalTime = video.getLength();
				if (totalTime != 0) {
					if (time == totalTime) {
						pause.setEnabled(false);
						togglePlay(false);
						ffwd.setEnabled(false);
						rewind.setEnabled(false);
					}
					progress.setValue((int) ((time*100)/totalTime));
				}
			}
		});

	}

	public void play() {
		video.playMedia(_fileName);
		timer.start();
	}

	public void togglePlay(boolean playing) {
		ImageIcon icon;

		if (playing) {
			icon = new ImageIcon("resources/Icons/Pause.png");
		} else {
			icon = new ImageIcon("resources/Icons/Play.png");
		}

		Image img = icon.getImage();
		Image resized = img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resized); 
		pause.setIcon(icon);
	}
}
