package VoxSpell.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import VoxSpell.words.Wordlist;

public class Statistics extends AbstractScreen{

	private JButton close = new JButton("Main Menu");
	private JButton changeView = new JButton("Table View");
	private MainFrame _mainFrame;
	private JTable table;
	private JScrollPane scroll;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	int row = 1;
	private DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	private static DecimalFormat df = new DecimalFormat("#.#");
	private final static String[] columns = {"Level", "Passed", "Failed", "Average Score", "Total Attempts", "Wordlist"};

	public Statistics(MainFrame mainFrame) throws EmptyStatsException {
		setLayout(null);
		//close.setBackground(new Color(255, 255, 0));
		_mainFrame = mainFrame;
		showStats();
	}

	/**
	 * This method displays the user's statistics in a JFrame with a JTable storing the data
	 * It is reused code from A2
	 * @throws EmptyStatsException 
	 */
	public void showStats() throws EmptyStatsException {
		calculateStats();
		createChart();

		if (table != null) {

			setLayout(null);
			// Adds the JTable to a JScrollPane to allow for scrolling and for headers to show up
			scroll = new JScrollPane(table);
			scroll.setBounds(50, 120, 700, 420);
			add(scroll);
			table.setOpaque(false);
			scroll.setOpaque(false);
			scroll.getViewport().setOpaque(false);
			scroll.setBorder(BorderFactory.createEmptyBorder());
			scroll.setVisible(false);

			// Disposes the JFrame and unhides the main menu once the "Main Menu" button is pressed
			close.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					_mainFrame.setScreen(new MainMenu(_mainFrame));
				}

			});


			//scroll.getViewport().setBackground(new Color(100, 149, 237));

			//table.setBackground(new Color(255, 255, 0));
			//scroll.setBackground(new Color(100, 149, 237));
			// Finally displays the JFrame containing the statistics
			close.setBounds(200, 560, 100, 25);
			add(close);

			changeView.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (changeView.getText().equals("Table View")) {
						changeView.setText("Graph View");
						scroll.setVisible(true);
						chartPanel.setVisible(false);
					} else {
						changeView.setText("Table View");
						scroll.setVisible(false);
						chartPanel.setVisible(true);
					}

				}

			});
			changeView.setBounds(500, 560, 100, 25);
			add(changeView);

			chartPanel = new ChartPanel(chart);
			chartPanel.setPopupMenu(null);
			chartPanel.setDomainZoomable(false);
			chartPanel.setRangeZoomable(false);
			chartPanel.setBackground(new Color(0xFF, 0xFF, 0xFF, 0));
			chartPanel.setOpaque(false);
			chartPanel.setBounds(50, 120, 700, 420);
			add(chartPanel);

			JLabel logo = new JLabel("");
			logo.setHorizontalAlignment(SwingConstants.CENTER);
			logo.setBounds(0, 0, 800, 120);

			ImageIcon image = new ImageIcon("resources/Statistics.png");

			logo.setIcon(image);
			add(logo);

		}
	}

	/*
	 * Modified A2 code
	 */
	private void calculateStats() throws EmptyStatsException {
		// Reads the current results
		ArrayList<String> results = new Wordlist(new File(".results")).readList();

		// Displays an error message if there are no statistics to be shown
		if (results.size() == 0) {
			JOptionPane.showMessageDialog(new JFrame(), "Error, no results saved", "Error",
					JOptionPane.ERROR_MESSAGE);
			throw new EmptyStatsException();
		} else {
			// Stores the results for every level as a HashMap with a 3 element array representing passed, failed and total score
			HashMap<String, HashMap<Integer, Integer[]>> stats1 = new HashMap<String, HashMap<Integer, Integer[]>>();
			HashMap<String, ArrayList<Integer>> levelMap = new HashMap<String, ArrayList<Integer>>();
			ArrayList<String> fileNames = new ArrayList<String>();

			for (int i = 0; i < results.size(); i++) {
				String[] split = results.get(i).split("\t");

				if (!stats1.containsKey(split[2])) {
					stats1.put(split[2], new HashMap<Integer, Integer[]>());
					levelMap.put(split[2], new ArrayList<Integer>());
					fileNames.add(split[2]);
				}

				HashMap<Integer, Integer[]> stats = stats1.get(split[2]);
				ArrayList<Integer> levels = levelMap.get(split[2]);

				int levelKey = Integer.parseInt(split[0].substring(5));
				// If the HashMap does not contain the current level, add it along with a [0,0,0] array
				if (!stats.containsKey(levelKey)) {
					levels.add(levelKey);
					Integer[] blank = new Integer[3];

					for (int j = 0; j < 3; j++) {
						blank[j] = 0;
					}

					stats.put(levelKey, blank);
				}

				int score = Integer.parseInt(split[1]);

				if(split[3].equals("Pass")){
					stats.get(levelKey)[0]++;
				} else {
					stats.get(levelKey)[1]++;
				}

				stats.get(levelKey)[2] = stats.get(levelKey)[2] + score;

				if (i + 11 > results.size()) {
					double value = Double.parseDouble(split[4]);
					dataset.addValue(value, "Accuracy", row++ + "");
				}
			}

			int rows = 0;
			// Sorts the levels in ascending order
			for (String file : fileNames) {
				Collections.sort(levelMap.get(file));
				rows += levelMap.get(file).size();
			}

			Collections.sort(fileNames);

			Object[][] data = new Object[rows][6];

			int row = 0;

			for (String file : fileNames) {
				// Creates the data array used for the JTable
				for (Integer level : levelMap.get(file)) {

					Integer[] subtotals = stats1.get(file).get(level);
					int total = subtotals[0] + subtotals[1];

					data[row][0] = level;
					data[row][1] = subtotals[0];
					data[row][2] = subtotals[1];

					if(total > 0){
						data[row][3] = df.format((double) subtotals[2] / total);
					} else {
						data[row][3] = 0;
					}

					data[row][4] = total;
					data[row][5] = file;

					row++;
				}
			}

			// Makes the JTable and disallows editing and resizing
			table = new JTable(data, columns);
			table.getTableHeader().setReorderingAllowed(false);
			table.getTableHeader().setResizingAllowed(false);
			table.getColumnModel().getColumn(0).setPreferredWidth(40);
			table.getColumnModel().getColumn(1).setPreferredWidth(40);
			table.getColumnModel().getColumn(2).setPreferredWidth(40);
			table.getColumnModel().getColumn(5).setPreferredWidth(200);
			table.setEnabled(false);
		}

	}

	public void createChart() {
		chart = ChartFactory.createLineChart("Accuracy Rates For Recent Quizzes", "", "% Correct", dataset, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(null);
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		plot.setBackgroundPaint(null);
        plot.setRangePannable(false);
        plot.setRangeGridlinesVisible(false);
        ValueAxis yAxis = plot.getRangeAxis();
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        yAxis.setRange(-5,105);
        yAxis.setAxisLinePaint(Color.BLACK);
        yAxis.setLabelPaint(Color.BLACK);
        yAxis.setTickLabelPaint(Color.BLACK);
        yAxis.setTickMarkPaint(Color.BLACK);
        CategoryAxis xAxis = plot.getDomainAxis();
        xAxis.setTickLabelPaint(Color.BLACK);
        xAxis.setAxisLinePaint(Color.BLACK);
        xAxis.setTickMarkPaint(Color.BLACK);
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.BLACK);
        renderer.setBaseShapesVisible(true);
        renderer.setDrawOutlines(true);
        renderer.setUseFillPaint(true);
        renderer.setBaseFillPaint((Paint)Color.white);
        renderer.setSeriesStroke(0, (Stroke)new BasicStroke(3.0f));
        renderer.setSeriesOutlineStroke(0, (Stroke)new BasicStroke(2.0f));
        renderer.setSeriesShape(0, (Shape)new Ellipse2D.Double(-5.0, -5.0, 10.0, 10.0));
	}

	class EmptyStatsException extends Exception {
	}
}
