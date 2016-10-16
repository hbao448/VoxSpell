package VoxSpell.gui;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class VideoPlayer extends AbstractScreen {

	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private Quiz _quiz;
	private MainFrame _spelling_Aid;
	private Timer timer;
	private EmbeddedMediaPlayer video;
	private String _fileName;

	public VideoPlayer(Quiz quiz, String filename, MainFrame spelling_Aid) {

		_spelling_Aid = spelling_Aid;
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

		JButton pause = new JButton("Pause");
		pause.setBounds(193, 31, 214, 33);
		panel.add(pause);
		pause.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (pause.getText().equals("Pause")) {
					video.setPause(true);
					pause.setText("Resume");
				} else {
					video.setPause(false);
					pause.setText("Pause");
				}
			}

		});

		JButton exit = new JButton("Exit");
		exit.setBounds(417, 31, 239, 33);
		panel.add(exit);

		JProgressBar progress = new JProgressBar();
		progress.setBounds(0, 0, 800, 25);
		panel.add(progress);
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
				_spelling_Aid.setScreen(_quiz);
				_quiz.setSubmitAsDefault();
				_spelling_Aid.toggleSoundButton(true);
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
}
