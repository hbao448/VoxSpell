package VoxSpell.gui;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import VoxSpell.media.MusicPlayer;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private SettingsData settings;
	private MusicPlayer player;
	private JButton bgmPlayback;
	private JSlider volumeControl;

	public static void main(String[] Args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame frame = new MainFrame();
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
				// Creates the frame and displays it
				frame.setVisible(true);
			}
		});
	}


	public MainFrame() {
		super("VoxSpell");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		//Creates a new settings object
		settings = new SettingsData();

		//Creates a button that toggles the playback of the background music
		//The button also toggles its icon based on whether or not the music is playing
		bgmPlayback = new JButton();
		bgmPlayback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.togglePlayback();
				toggleButton(player.isPlaying());
			}
		});
		bgmPlayback.setBounds(740, 10, 50, 50);
		bgmPlayback.setFocusable(false);
		
		//Creates a new vertical slider that is used to control the background music volume
		volumeControl = new JSlider(JSlider.VERTICAL);
		volumeControl.setBounds(720,10,20,50);
		volumeControl.setMaximum(100);
		volumeControl.setMinimum(0);
		volumeControl.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				 JSlider slider = (JSlider) e.getSource();
				 player.setVolume(slider.getValue());
			}
		});
		
		//Initially displays the main menu
		setScreen(new MainMenu(this));
		setLocationRelativeTo(null);
		player = new MusicPlayer(false);
		toggleButton(true);
	}

	/**
	 * Sets the input screen as the content pane of the frame and repaints the frame
	 * @param screen the screen to set as the content pane
	 */
	public void setScreen(AbstractScreen screen) {
		setContentPane(screen);
		getContentPane().add(bgmPlayback);
		getContentPane().add(volumeControl);
		pack();
		revalidate();
		repaint();
	}

	/**
	 * Returns the object containing all of the settings
	 * @return
	 */
	public SettingsData getSettings() {
		return settings;
	}

	/**
	 * Returns the player that plays the background music
	 * @return
	 */
	public MusicPlayer getPlayer() {
		return player;
	}

	/**
	 * Toggles the icon displayed on the background music button from muted to unmuted and vice versa
	 * @param playing True if the music is playing, otherwise false.
	 */
	public void toggleButton(boolean playing) {

		ImageIcon icon;

		if (playing) {
			icon = new ImageIcon("resources/Icons/Mute.png");
		} else {
			icon = new ImageIcon("resources/Icons/Unmute.png");
		}

		Image img = icon.getImage();
		Image resized = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resized); 
		bgmPlayback.setIcon(icon);

	}
	
	/**
	 * Adds an action listener to the background music button
	 * @param al
	 */
	public void addSoundListener(ActionListener al) {
		bgmPlayback.addActionListener(al);
	}
	
	/**
	 * Removes an action listener from the background music button
	 * @param al
	 */
	public void removeSoundListener(ActionListener al) {
		bgmPlayback.removeActionListener(al);
	}
}
