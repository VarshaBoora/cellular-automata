
package edu.neu.csye6200.ca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class WolfApp extends CAApp {

	private static Logger log = Logger.getLogger(WolfApp.class.getName());

	protected static JPanel northPanel = null, mainPanel=null,nConfig, nPlay, statusPanel;
	protected static JButton startButton, pauseButton, rewindButton, stopButton, createButton;
    private CACrystalSet crystalset;
    protected static JComboBox<Integer> comboRows = null;
	protected static JComboBox<Integer> comboCol = null;
	protected static JComboBox<RuleNames> comboRules = null;
	protected static JComboBox<Integer> comboSleepTime = null;
	protected static JComboBox<Integer> comboGenLimit = null;
	protected JLabel lblRows = null;
	protected JLabel lblCols = null;
	protected JLabel lblRules = null;
	protected JLabel lblSleepTime = null;
	protected JLabel lblGenLimit = null;
	protected JLabel lblGenCount = null;
	protected static JLabel genCount = null;
	protected static JLabel lblStatus = null;
	
	private static final int FRAME_WIDTH = 900;
	private static final int FRAME_HEIGHT = 600;
	private static final int BUTTONS_HEIGHT = 80;
	
    /**
     * Sample app constructor
     */
    public WolfApp() {
    	frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setTitle("WolfApp");    	
    	//mainPanel.add(new CACanvas(), BorderLayout.CENTER); // Add to the center of our frame
		frame.setVisible(true); // The UI is built, so display it
		lblStatus.setText("Welcome to Mobile Automata Simulation World ... !!! ");
    }
   
	public JPanel getMainPanel() {

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);
		/*
		 * call to initialize and get the north Panel which contains all the user
		 * interaction items
		 */
		mainPanel.add(BorderLayout.NORTH, getNorthPanel());
		mainPanel.add(BorderLayout.SOUTH, getNPlayPlanel());
		mainPanel.setVisible(true);
		return mainPanel;
	}
    
    public JPanel getNorthPanel() {
    	northPanel = new JPanel();
    	northPanel.setLayout(new FlowLayout());    	
    	
    	nConfig = new JPanel();
		nConfig.setLayout(new FlowLayout());
		nConfig.setBackground(Color.WHITE);
    	
    	lblRows = new JLabel("Rows");
		nConfig.add(lblRows);
		final Integer rows[] = { 50, 70, 100, 120, 200, 300 };
		comboRows = new JComboBox<Integer>(rows);
		comboRows.setMaximumRowCount(5);
		comboRows.setEditable(false);
		comboRows.addActionListener(this);
		nConfig.add(comboRows);

		lblCols = new JLabel("Columns");
		nConfig.add(lblCols);
		final Integer cols[] = { 50, 70,100, 120, 200, 300 };
		comboCol = new JComboBox<Integer>(cols);
		comboCol.setMaximumRowCount(5);
		comboCol.setEditable(false);
		comboCol.addActionListener(this);
		nConfig.add(comboCol);

		lblRules = new JLabel("Rules");
		nConfig.add(lblRules);
		final RuleNames rulesNames[] = { RuleNames.STASIS};
		comboRules = new JComboBox<RuleNames>(rulesNames);
		comboRules.setMaximumRowCount(5);
		comboRules.setEditable(false);
		comboRules.addActionListener(this);
		nConfig.add(comboRules);

		// Text Boxes
		lblSleepTime = new JLabel("Sleep Time");
		nConfig.add(lblSleepTime);
		final Integer slTimes[] = {10, 50, 100, 500, 800, 1000 };
		comboSleepTime = new JComboBox<Integer>(slTimes);
		comboSleepTime.setMaximumRowCount(5);
		comboSleepTime.setEditable(false);
		comboSleepTime.addActionListener(this);
		nConfig.add(comboSleepTime);

		lblGenLimit = new JLabel("Generation Limit");
		nConfig.add(lblGenLimit);
		final Integer genLimits[] = { 10, 20, 30, 50, 70, 100};
		comboGenLimit = new JComboBox<Integer>(genLimits);
		comboGenLimit.setMaximumRowCount(5);
		comboGenLimit.setEditable(false);
		comboGenLimit.addActionListener(this);
		nConfig.add(comboGenLimit);		
		
		// Create Button
		createButton = new JButton("Create");
		createButton.addActionListener(this);
		createButton.setFocusPainted(false);
		nConfig.add(createButton);
			

		// Status Panel
		statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		statusPanel.setBackground(Color.WHITE);
		mainPanel.add(BorderLayout.SOUTH, statusPanel);

		// Labels
		lblStatus = new JLabel("", 10);
		statusPanel.add(lblStatus);
		northPanel.add(nConfig);

		return northPanel;
    }
    public JPanel getNPlayPlanel() {
    	nPlay = new JPanel();
		nPlay.setLayout(new FlowLayout());
		nPlay.setBackground(Color.WHITE);

		// Buttons
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		startButton.setFocusPainted(false);
		nPlay.add(startButton);

		pauseButton = new JButton("Pause"); // Allow the app to hear about button pushes
		pauseButton.addActionListener(this);
		nPlay.add(pauseButton);

		rewindButton = new JButton("Rewind"); // Allow the app to hear about button
		rewindButton.addActionListener(this);
		nPlay.add(rewindButton);

		stopButton = new JButton("Stop"); // Allow the app to hear about button
		stopButton.addActionListener(this);
		nPlay.add(stopButton);

		// Labels
		lblGenCount = new JLabel("Generations : ", 4); // Allow the app to hear Input button pushes
		nPlay.add(lblGenCount);

		genCount = new JLabel("0", 4);
		nPlay.add(genCount);
		// Initially disabling till user creates a simulation
				for (Component component : nPlay.getComponents()) {
					component.setEnabled(false);
				}
		return nPlay;
    }
    
	@Override
	public void actionPerformed(ActionEvent ae) {
		if ("Create".equals(ae.getActionCommand())) { // Create event handler

			if (Arrays.asList(mainPanel.getComponents()).contains(crystalset))
				mainPanel.remove(crystalset);

			CACell.setCellCount(0);

			int rows = (int) comboRows.getSelectedItem();
			int cols = (int) comboCol.getSelectedItem();
			RuleNames rule = (RuleNames) comboRules.getSelectedItem();
			int maximumGen = (int) comboGenLimit.getSelectedItem();
			int sleepTime = (int) comboSleepTime.getSelectedItem();

			
			int middleCell = Math.round((rows * cols) / 2);
			int initAliveCell = middleCell - Math.round(cols / 2);

			CACrystal crystal = new CACrystal(rows, cols, rule, initAliveCell);
			crystalset = new CACrystalSet(crystal, maximumGen, sleepTime);

			// maRegionSet.setLayout(new BoxLayout(maRegionSet, BoxLayout.Y_AXIS));
			crystalset.setLayout(new BorderLayout());
			// Setting preferred Sizes
			crystalset.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));
			crystalset.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT - BUTTONS_HEIGHT));

			// Disabling nConfig till user is done with the simulation
			for (Component component : nConfig.getComponents()) {
				component.setEnabled(false);
			}

			// Enabling nPlay till user is done with the simulation
			for (Component component : nPlay.getComponents()) {
				component.setEnabled(true);
			}

			// Initially there is no need to rewind or pause as the simulation is not yet
			// started
			pauseButton.setEnabled(false);
			rewindButton.setEnabled(false);

			crystalset.setBackground(Color.WHITE);
			mainPanel.add(BorderLayout.EAST, crystalset);
			frame.setVisible(true);
			lblStatus.setText("Simulation Region Created successfully...");

		} else if ("Start".equals(ae.getActionCommand())) { // Start button event

			lblStatus.setText("CACrystal Automata Started ... !!! ");
			log.info("Starting the mobile simulation ... !!! ");

			// In start mode, you can pause/stop the simulation
			startButton.setEnabled(false);
			rewindButton.setEnabled(false);

			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Getting the next Region
			crystalset.nextCrystal();

		} else if ("Pause".equals(ae.getActionCommand())) { // Pause button event

			// In pause mode you can rewind,start,stop the simulation
			pauseButton.setEnabled(false);

			rewindButton.setEnabled(true);
			startButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Inform the thread to pause the simulation
			crystalset.pauseThread();

		} else if ("Rewind".equals(ae.getActionCommand())) {

			lblStatus.setText("Mobile Automata is rewinding ...");
			log.info("Simulation will now rewind...");

			rewindButton.setEnabled(false);
			startButton.setEnabled(false);
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);

			// Inform the thread to rewind the simulation
			crystalset.rewindCrystal();

		} else if ("Stop".equals(ae.getActionCommand())) { // Stop button event
			lblStatus.setText("Simulation stopped . Thank you for using Mobile Automata ... !!");
			log.info("Simulation stopped . Thank you for using Mobile Automata ... !!");

			genCount.setText("0");
			crystalset.stopThread();
			crystalset.setVisible(false);

			// Enabling nConfig as the user is done/stopped the simulation
			for (Component component : nConfig.getComponents()) {
				component.setEnabled(true);
			}

			// Disabling nPlay as user is done with the simulation
			for (Component component : nPlay.getComponents()) {
				component.setEnabled(false);
			}

		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		log.info("Window opened");
	}



	@Override
	public void windowClosing(WindowEvent e) {	
	}



	@Override
	public void windowClosed(WindowEvent e) {
	}



	@Override
	public void windowIconified(WindowEvent e) {
		log.info("Window iconified");
	}



	@Override
	public void windowDeiconified(WindowEvent e) {	
		log.info("Window deiconified");
	}



	@Override
	public void windowActivated(WindowEvent e) {
		log.info("Window activated");
	}



	@Override
	public void windowDeactivated(WindowEvent e) {	
		log.info("Window deactivated");
	}
	
	/**
	 * Sample Wolf application starting point
	 * @param args
	 */
	public static void main(String[] args) {
		new WolfApp();
		log.info("WolfApp started");
	}
}
