package VoxSpell.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
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

public class Settings extends AbstractBackgroundScreen{

	private MainFrame _mainFrame;
	private JTextField textField;
	private SettingsData settingsData;
	private ArrayList<String> _availableVoices = new ArrayList<String>();
	private ArrayList<String> _voiceNames = new ArrayList<String>();
	private ArrayList<Double> _speeds = new ArrayList<Double>();
	private JComboBox selectVoices;


	public Settings(MainFrame mainFrame) {

		_mainFrame = mainFrame;
		settingsData = _mainFrame.getSettings();

		setLayout(null);
		JButton mainMenu = new JButton("Main Menu");
		//mainMenu.setBackground(new Color(255, 255, 0));
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mainFrame.setScreen(new MainMenu(_mainFrame));
			}
		});
		mainMenu.setBounds(350, 560, 100, 25);
		add(mainMenu);

		textField = new JTextField(settingsData.getWordlist().getPath());
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setEditable(false);
		textField.setBounds(220, 206, 350, 40);
		add(textField);
		textField.setColumns(10);

		JLabel wordlistLabel = new JLabel("Wordlist :");
		wordlistLabel.setIcon(new ImageIcon("resources/Icons/Wordlist.png"));
		wordlistLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordlistLabel.setBounds(10, 206, 200, 40);
		add(wordlistLabel);

		JButton btnNewButton = new JButton("Change Wordlist");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");

				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(filter);
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				disableTextEditing(fileChooser.getComponents());
				int result = fileChooser.showOpenDialog(Settings.this);
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

		JButton btnResetWordlist = new JButton("Reset Wordlist");
		btnResetWordlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("resources/Data/Default Wordlist.txt");
				settingsData.setWordlist(new File("resources/Data/Default Wordlist.txt"));
			}
		});
		btnResetWordlist.setBounds(590, 225, 180, 33);
		add(btnResetWordlist);

		_availableVoices.add("American Voice");
		_availableVoices.add("British Voice");
		_availableVoices.add("New Zealand Voice");

		_voiceNames.add("kal_diphone");
		_voiceNames.add("rab_diphone");
		_voiceNames.add("akl_nz_jdt_diphone");

		selectVoices = new JComboBox(_availableVoices.toArray());
		selectVoices.setLocation(280, 340);
		selectVoices.setSize(300, 40);

		// Adds an ActionListener to the JComboBox to save the selected voice into the _selectedVoice field
		selectVoices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				JComboBox voice = (JComboBox) evt.getSource();
				int index = voice.getSelectedIndex();
				settingsData.getFestival().setVoice(_voiceNames.get(index));
			}
		});

		selectVoices.setSelectedIndex(_voiceNames.indexOf(settingsData.getFestival().getVoice()));

		add(selectVoices);

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

		JLabel lblVoiceSpeed = new JLabel("");
		lblVoiceSpeed.setIcon(new ImageIcon("resources/Icons/Voice Speed.png"));
		lblVoiceSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoiceSpeed.setBounds(10, 440, 260, 40);
		add(lblVoiceSpeed);

		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setBounds(0, 0, 800, 150);

		ImageIcon image = new ImageIcon("resources/Icons/Settings.png");

		logo.setIcon(image);
		add(logo);

		JButton testVoice = new JButton("Test Voice");
		testVoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsData.getFestival().test((String)selectVoices.getSelectedItem());
			}
		});
		testVoice.setMultiClickThreshhold(1000);
		testVoice.setBounds(590, 375, 162, 70);
		add(testVoice);

	}

	public void disableTextEditing(Component[] components) {
		for (Component comp : components) {
			if (comp instanceof JPanel) {
				disableTextEditing(((JPanel) comp).getComponents());
			} else if (comp instanceof JTextField) {
				((JTextField) comp).setEditable(false);
			}
		}
	}
}
