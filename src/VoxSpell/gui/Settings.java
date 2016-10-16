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

public class Settings extends AbstractScreen{

	private MainFrame _spelling_Aid;
	private JTextField textField;
	private SettingsData settingsData;
	private ArrayList<String> _availableVoices = new ArrayList<String>();
	private ArrayList<String> _voiceNames = new ArrayList<String>();
	private ArrayList<Double> _speeds = new ArrayList<Double>();
	private JComboBox selectVoices;
	

	public Settings(MainFrame spelling_aid) {

		_spelling_Aid = spelling_aid;
		settingsData = _spelling_Aid.getSettings();
		
		setLayout(null);
		JButton mainMenu = new JButton("Main Menu");
		//mainMenu.setBackground(new Color(255, 255, 0));
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_spelling_Aid.setScreen(new MainMenu(_spelling_Aid));
			}
		});
		mainMenu.setBounds(0, 550, 800, 50);
		add(mainMenu);

		textField = new JTextField(settingsData.getWordlist().getWordlistFile().getPath());
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField.setEditable(false);
		textField.setBounds(188, 206, 350, 40);
		add(textField);
		textField.setColumns(10);

		JLabel wordlistLabel = new JLabel("Wordlist :");
		wordlistLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordlistLabel.setBounds(75, 194, 133, 64);
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

					if (wordlist.length() > 10000000) {
						JOptionPane.showMessageDialog(new JFrame(), "The wordlist must be smaller than 10MB, please select another file", "File too large",
								JOptionPane.INFORMATION_MESSAGE);
					} else
						try {
							if (words.scanLevels().isEmpty()){
								JOptionPane.showMessageDialog(new JFrame(), "The input wordlist must have \"%Level \" and a number to represent each level, followed by words in that level", "Incorrect File Format",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								textField.setText(wordlist.getPath());
								settingsData.setWordlist(wordlist);
							}
						} catch (Exception e1) {
							JOptionPane.showMessageDialog(new JFrame(), "The input wordlist must the levels stored in ascending order, starting from level 1", "Incorrect File Format",
									JOptionPane.INFORMATION_MESSAGE);
						}
				}
			}
		});
		btnNewButton.setBounds(573, 192, 180, 33);
		add(btnNewButton);

		JButton btnResetWordlist = new JButton("Reset Wordlist");
		btnResetWordlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText("resources\\Default Wordlist.txt");
				settingsData.setWordlist(new File("resources/Default Wordlist.txt"));
			}
		});
		btnResetWordlist.setBounds(573, 225, 180, 33);
		add(btnResetWordlist);
		
		_availableVoices.add("American Voice");
		_availableVoices.add("British Voice");
		_availableVoices.add("New Zealand Voice");

		_voiceNames.add("kal_diphone");
		_voiceNames.add("rab_diphone");
		_voiceNames.add("akl_nz_jdt_diphone");
		
		selectVoices = new JComboBox(_availableVoices.toArray());
		selectVoices.setLocation(188, 308);
		selectVoices.setSize(300, 60);

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
		
		JLabel lblVoice = new JLabel("Voice :");
		lblVoice.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoice.setBounds(75, 308, 133, 64);
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
		comboBox.setBounds(188, 398, 300, 60);
		add(comboBox);
		
		JLabel lblVoiceSpeed = new JLabel("Voice Speed :");
		lblVoiceSpeed.setHorizontalAlignment(SwingConstants.CENTER);
		lblVoiceSpeed.setBounds(75, 398, 133, 64);
		add(lblVoiceSpeed);
		
		JLabel logo = new JLabel("");
		logo.setHorizontalAlignment(SwingConstants.CENTER);
		logo.setBounds(0, 0, 800, 150);
		
		ImageIcon image = new ImageIcon("resources/Settings.png");

		logo.setIcon(image);
		add(logo);
		
		JButton testVoice = new JButton("Test Voice");
		testVoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingsData.getFestival().test((String)selectVoices.getSelectedItem());
			}
		});
		testVoice.setMultiClickThreshhold(1000);
		testVoice.setBounds(573, 343, 162, 76);
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
