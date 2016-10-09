package VoxSpell;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Hiscores {

	private JFrame frame;
	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private Spelling_Aid _spelling_Aid;

	/**
	 * Create the frame.
	 */
	public Hiscores(Spelling_Aid spelling_Aid) {
		_spelling_Aid = spelling_Aid;
		frame = new JFrame("Hiscores");

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(600,400);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(100, 149, 237));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {
			}

			@Override
			public void windowClosed(WindowEvent e) {
				_spelling_Aid.setVisible(true);
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

		});

		JButton mainMenu = new JButton("Main Menu");
		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_spelling_Aid.setVisible(true);
				frame.dispose();
			}
		});
		mainMenu.setBounds(0, 370, 600, 30);
		contentPane.add(mainMenu);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(79, 11, 495, 277);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setBackground(new Color(255, 255, 0));
		scrollPane.setViewportView(table);
		table.setRowHeight(50);

		{
			JLabel lblNewLabel = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/1st.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 

			lblNewLabel.setIcon(icon);
			lblNewLabel.setBounds(29, 43, 40, 40);
			contentPane.add(lblNewLabel);
		}
		{
			JLabel label = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/2nd.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 

			label.setIcon(icon);
			label.setBounds(29, 94, 40, 40);
			contentPane.add(label);
		}
		{
			JLabel label_1 = new JLabel("");

			ImageIcon icon = new ImageIcon("resources/3rd.png");
			Image img = icon.getImage();
			Image resized = img.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH);
			icon = new ImageIcon(resized); 

			label_1.setIcon(icon);
			label_1.setBounds(29, 145, 40, 40);
			contentPane.add(label_1);
		}

	}

	public void viewScores() {
		ArrayList<String> results = _spelling_Aid.readList(new File("resources/Default Scores.txt"));
		ArrayList<String> extraResults = _spelling_Aid.readList(new File(".scores"));
		ArrayList<Integer> resultScores = new ArrayList<Integer>();

		HashMap<Integer, ArrayList<Object[]>> scores = new HashMap<Integer, ArrayList<Object[]>>();

		results.addAll(extraResults);

		for (String result : results) {
			String[] split = result.split("\t");

			int score = Integer.parseInt(split[2]);

			if (!scores.containsKey(score)) {
				scores.put(score, new ArrayList<Object[]>());
				resultScores.add(score);
			}

			Object[] data = new Object[3];

			data[0] = Integer.parseInt(split[1]);
			data[1] = split[0];
			data[2] = split[3];

			scores.get(score).add(data);

		}

		Collections.sort(resultScores);

		int added = 0;

		Object[][] model = new Object[5][4];

		while (added < 5) {

			for (int i = resultScores.size()-1 ; i >= 0 && added < 5; i--) {
				ArrayList<Object[]> values = scores.get(resultScores.get(i));

				for (Object[] data : values) {
					if (added >= 5) {
						break;
					}
					model[added][0] = resultScores.get(i);
					model[added][1] = data[0];
					model[added][2] = data[1];
					model[added][3] = data[2];
					added++;
				}
			}
		}

		table.setModel(new DefaultTableModel(
				model,
				new String[] {
						"Score", "Maximum Level", "Player", "Wordlist"
				}
				));
		
		table.getColumnModel().getColumn(1).setPreferredWidth(105);
		table.getColumnModel().getColumn(3).setPreferredWidth(128);
		frame.setVisible(true);
	}
}
