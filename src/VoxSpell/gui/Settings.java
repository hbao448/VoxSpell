package VoxSpell.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import VoxSpell.words.Wordlist;
import VoxSpell.words.WordlistFormatException;

@SuppressWarnings("serial")
public class Settings extends AbstractBackgroundScreen{

	private MainFrame _mainFrame;
	private JTextField textField;
	private SettingsData settingsData;
	private ArrayList<String> _availableVoices = new ArrayList<String>();
	private ArrayList<String> _voiceNames = new ArrayList<String>();
	private ArrayList<Double> _speeds = new ArrayList<Double>();
	@SuppressWarnings("rawtypes")
	private JComboBox selectVoices;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Settings(MainFrame mainFrame) {

		_mainFrame = mainFrame;
		settingsData = _mainFrame.getSettings();

		setLayout(null);
		//Adds a main menu button that returns to the menu when pressed
		JButton mainMenu = new JButton("Main Menu");
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mainFrame.setScreen(new MainMenu(_mainFrame));
			}
		});
		mainMenu.setBounds(350, 560, 100, 25);
		add(mainMenu);

		//Adds a text field that displays the path to the currently selected wordlst
		textField = new JTextField(settingsData.getWordlist().getPath());
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setEditable(false);
		textField.setBounds(220, 206, 350, 40);
		add(textField);
		textField.setColumns(10);

		//Adds a label to show wordlist
		JLabel wordlistLabel = new JLabel("Wordlist :");
		wordlistLabel.setIcon(new ImageIcon("resources/Icons/Wordlist.png"));
		wordlistLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordlistLabel.setBounds(10, 206, 200, 40);
		add(wordlistLabel);

		//Adds a button that is used to choose a wordlist
		JButton btnNewButton = new JButton("Change Wordlist");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				//Only allow .txt files
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(filter);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				disableTextEditing(fileChooser.getComponents());
				int result = fileChooser.showOpenDialog(Settings.this);
				//If the user presses ok then check if the wordlist is valid, if it is then change to it, otherwise show an error message to the user
				if (result == JFileChooser.APPROVE_OPTION) {

					File wordlist = fileChooser.getSelectedFile();
					Wordlist words = new Wordlist(wordlist);

					try {
						words.scanLevels();
						textField.setText(wordlist.getPath());
						settingsData.setWordlist(wordlist);

					} catch (WordlistFormatException e1) {
						JOptionPane.showMessageDialog(new JFrame(), e1.getMessage(), "Incorrect File Format",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
		btnNewButton.setBounds(590, 192, 180, 33);
		add(btnNewButton);

		//Adds a button that resets the wordlist back to the default when pressed
		JButton btnResetWordlist = new JButton("Reset Wordlist");
		btnResetWordlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("resources/Data/Default Wordlist.txt");
				settingsData.setWordlist(new File("resources/Data/Default Wordlist.txt"));
			}
		});
		btnResetWordlist.setBounds(590, 225, 180, 33);
		add(btnResetWordlist);

		//Sets up a JComboBox that contains the voices in user-friendly names
		_availableVoices.add("American Voice");
		_availableVoices.add("British Voice");
		_availableVoices.add("New Zealand Voice");

		//Creates an array that stores the actual names of the voices
		_voiceNames.add("kal_diphone");
		_voiceNames.add("rab_diphone");
		_voiceNames.add("akl_nz_jdt_diphone");

		selectVoices = new JComboBox(_availableVoices.toArray());
		selectVoices.setLocation(280, 340);
		selectVoices.setSize(300, 40);

		// Adds an ActionListener to the JComboBox to save the selected voice into the festival object
		selectVoices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JComboBox voice = (JComboBox) evt.getSource();
				int index = voice.getSelectedIndex();
				settingsData.getFestival().setVoice(_voiceNames.get(index));
			}
		});

		selectVoices.setSelectedIndex(_voiceNames.indexOf(settingsData.getFestival().getVoice()));

		add(selectVoices);

		//Adds labels and a JComboBox to allow the user to select custom voice speeds
		JLabel lblVoice = new JLabel("");
		lblVoice.setIcon(new ImageIcon("resources/Icons/Voice.png"));
		lblVoice.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoice.setBounds(10, 340, 120, 40);
		add(lblVoice);

		for (double speed = 0.5; speed <= 2.0; speed += 0.25) {
			_speeds.add(speed);
		}

		JComboBox comboBox = new JComboBox(_speeds.toArray());
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox speed = (JComboBox) e.getSource();
				int index = speed.getSelectedIndex();
				settingsData.getFestival().setSpeed(_speeds.get(index));
			}
		});
		comboBox.setSelectedIndex(_speeds.indexOf(settingsData.getFestival().getSpeed()));
		comboBox.setBounds(280, 440, 300, 40);
		add(comboBox);

		JLabel lblVoiceSpeed = new JLabel();
		lblVoiceSpeed.setIcon(new ImageIcon("resources/Icons/Voice Speed.png"));
		lblVoiceSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoiceSpeed.setBounds(10, 440, 260, 40);
		add(lblVoiceSpeed);

		JLabel logo = new JLabel();
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setBounds(0, 0, 800, 150);

		ImageIcon image = new ImageIcon("resources/Icons/Settings.png");

		logo.setIcon(image);
		add(logo);

		//Adds a button that allows the user to hear a sample sentence spoken using the current settings
		JButton testVoice = new JButton("Test Voice");
		testVoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsData.getFestival().test((String)selectVoices.getSelectedItem());
			}
		});
		//Only allow the button to be pressed once per second
		testVoice.setMultiClickThreshhold(1000);
		testVoice.setBounds(590, 375, 162, 70);
		add(testVoice);

	}

	/**
	 * This is a helper method used to disable the text input field of a JFileChooser
	 * @param components
	 */
	private void disableTextEditing(Component[] components) {
		for (Component comp : components) {
			if (comp instanceof JPanel) {
				disableTextEditing(((JPanel) comp).getComponents());
			} else if (comp instanceof JTextField) {
				((JTextField) comp).setEditable(false);
			}
		}
	}
}
