package VoxSpell.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import VoxSpell.media.MusicPlayer;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private AbstractScreen _screen;
	@SuppressWarnings("rawtypes")
	private JComboBox selectLV;
	@SuppressWarnings("rawtypes")

	private SettingsData settings;
	private MusicPlayer player;
	private JButton bgmPlayback;

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/*
	 * Reused A2 code
	 */
	public MainFrame() {
		super("VoxSpell");
		//setBackground(new Color(100, 149, 237));
		//setSize(800, 600);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setResizable(false);

		settings = new SettingsData();

		bgmPlayback = new JButton("");
		bgmPlayback.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.togglePlayback();
				toggleButton(player.isPlaying());
			}
		});
		
		bgmPlayback.setBounds(740, 10, 50, 50);
		bgmPlayback.setFocusable(false);

		setScreen(new MainMenu(this));
		setLocationRelativeTo(null);
		player = new MusicPlayer(false);
		toggleButton(true);
	}

	public void setScreen(AbstractScreen screen) {
		setContentPane(screen);
		getContentPane().add(bgmPlayback);
		pack();
		revalidate();
		repaint();
	}

	public SettingsData getSettings() {
		return settings;
	}

	public MusicPlayer getPlayer() {
		return player;
	}

	public void toggleButton(boolean playing) {

		ImageIcon icon;

		if (playing) {
			icon = new ImageIcon("resources/Mute.png");
		} else {
			icon = new ImageIcon("resources/Unmute.png");
		}

		Image img = icon.getImage();
		Image resized = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resized); 
		bgmPlayback.setIcon(icon);

	}
	
	public void addSoundListener(ActionListener al) {
		bgmPlayback.addActionListener(al);
	}
	
	public void removeSoundListener(ActionListener al) {
		bgmPlayback.removeActionListener(al);
	}
	
	public void toggleSoundButton(boolean enabled) {
		bgmPlayback.setVisible(enabled);
	}
}
