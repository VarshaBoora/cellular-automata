package edu.neu.csye6200.ca;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.JPanel;


public class CACrystalSet extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	private Map<Integer, CACrystal> caCrystalRecord; // To store all the Regions. Will be helpful in retrieving data back
	private int generationCount; // To keep track of region generations.

	/*
	 * User defined limit until which the Regions are generated and stored in the
	 * MAP and simulation stops when the limit is reached
	 */
	private int genLimit;

	private int sleepTime; // User defined sleep Time between generations.
	private CACrystal previousCrystal; // Previous region in the RegionSet
	private Thread cellTh; // Thread which executes once the user Starts the Simulation

	/*
	 * Indicator to alert if the automata is complete. Using which we can show some
	 * custom message to the user.
	 */
	private volatile boolean completeFlag;
	private volatile boolean paused; // Helps in determining if the generation is paused
	private volatile boolean stopped;// Helps in stopping the simulation.
	private volatile boolean rewind; // Helps in determining if rewind is called

	// For Logging application process to the console.
	private static Logger log = Logger.getLogger(WolfApp.class.getName());

	// Constructor to initialize the MARegionSet
	public CACrystalSet(CACrystal crystal, int genLimit, int sleepTime) {

		// Initializing the properties of a RegionSet
		initializeRegionSet();
		this.previousCrystal = crystal;
		this.genLimit = genLimit;
		this.sleepTime = sleepTime;

		// Adding the Initial Region to the Map.
		addRegionToMap(generationCount, previousCrystal);
		log.info("MARegion Set created successfully...");

	}

	/*
	 * Routine to initialize all the properties of a Region Set. Must be called
	 * before using the properties.
	 */
	private void initializeRegionSet() {

		/*
		 * Using Tree Map as we can allow user to retrieve any region based on
		 * generation and not just the previous generation. Also the search using Tree
		 * Map is faster.
		 */

		this.caCrystalRecord = new TreeMap<Integer, CACrystal>();
		this.generationCount = 0;
		this.completeFlag = false;
		setOpaque(false); // This is necessary to make painting work
	}

	/*
	 * Function which gets triggered when the thread is executed to process the
	 * Simulation.
	 */

	@Override
	public void run() {

		try {

			// Multiple checks are made before new region is created.
			while (!paused && generationCount != genLimit && !completeFlag && !stopped) {

				CACrystal nextCrystal;

				// To check if user is rewinding the simulation
				if (rewind && generationCount > 0) {
					previousCrystal = getMaRegionRecord().get(generationCount - 1);
					removeRegionFromMap(generationCount);
					generationCount--;
					repaint(); // Paints the new state of the region using paintComponent.

				} else if (rewind && generationCount == 0) { // To pause the simulation when user goes to initial state.
					paused = true;

				} else { // Forward simulation
					nextCrystal = previousCrystal.createNextCrystal();
					generationCount++;
					addRegionToMap(generationCount, nextCrystal); // Once done, the region is added to the MAP
					previousCrystal = nextCrystal;
					repaint(); // Paints the new state of the region using paintComponent.
				}

				WolfApp.genCount.setText(generationCount + "");
				simulationCheck(); // helper method to check if the simulation is completed

				try {
					Thread.sleep(this.sleepTime); // customized sleep time
				} catch (InterruptedException e) {
					log.severe("The thread execution was interrupted. Details : " + e.toString());
					break;
				}
			} // Custom messages for the user both in console and UI to help user for
				// identifying simulation state.
			if (stopped) {
				stopped = false;
			} else if (generationCount < genLimit && paused) {
				if (rewind && generationCount == 0) {
					rewind = false;
					WolfApp.lblStatus.setText("Simulation paused as user went back to the initial state...");
					log.info("Simulation paused as user went back to the initial state...");
					WolfApp.pauseButton.setEnabled(false);
					WolfApp.startButton.setEnabled(true);

				} else if (rewind) {
					WolfApp.lblStatus.setText("Simulation paused while user was rewinding...");
					log.info("Simulation paused while user was rewinding...");
					WolfApp.startButton.setEnabled(true);
					WolfApp.rewindButton.setEnabled(true);
				} else {
					WolfApp.lblStatus.setText("Simulation Paused...");
					log.info("Simulation Paused...");
				}

			} else if (completeFlag) {

				if (previousCrystal.isLocked()) {
					WolfApp.lblStatus.setText("OOPS!! You are locked... Simulation completed Successfully...");
					log.info("OOPS!! You are locked... Simulation completed Successfully...");
				} else {
					WolfApp.lblStatus.setText("Simulation completed Successfully...");
					log.info("Simulation completed Successfully...");
				}
				WolfApp.pauseButton.setEnabled(false);
				WolfApp.startButton.setEnabled(false);

			} else if (generationCount == genLimit) {
				WolfApp.lblStatus.setText("Simulation reached maximum generation Limit...");
				log.info("Simulation reached maximum generation Limit...");

				WolfApp.pauseButton.setEnabled(false);
				WolfApp.startButton.setEnabled(false);
			}
		} catch (Exception e) {
			log.severe("OOPS!! Some issue occured while simulation was in progress. Details : " + e.toString());
		}

	}

	// Helper method to check if the simulation is completed even before generation
	// Limit.
	private void simulationCheck() {
		int bSim = 0;
		for (int row = 0; row < previousCrystal.getCrystalRows(); row++) {
			for (int col = 0; col < previousCrystal.getCrystalCols(); col++) {
				if (previousCrystal.getCell(row, col).getCaCellState() == CACellState.DEAD) {
					bSim++;
				}
			}
		}
		if (bSim == previousCrystal.getCrystalRows() * previousCrystal.getCrystalCols()) {
			completeFlag = true;
		}
	}

	// Start point for generating next Regions.
	public void nextCrystal() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = false;
		cellTh.start(); // Calls run method of the thread
	}

	// Start point for retrieving previous Regions.
	public void rewindCrystal() {
		cellTh = new Thread(this, "automataThread"); // Starts a new Thread
		this.paused = false;
		this.rewind = true;
		cellTh.start(); // Calls run method of the thread
	}	
	
	// Routine to update the colors or paint the state of the cell.
	public void paintComponent(Graphics g) {

		try {		         
	         
			/*for (int row = 0; row < previousCrystal.getCrystalRows(); row++) {
				for (int col = 0; col < previousCrystal.getCrystalCols(); col++) {					
					if (previousCrystal.getCell(row, col).getCaCellState() == CACellState.ALIVE) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillPolygon(previousCrystal.getCell(row, col).getPoly());
					} else if((previousCrystal.getCell(row, col).getCaCellState() == CACellState.DEAD)){
						g.setColor(Color.DARK_GRAY);
						g.fillPolygon(previousCrystal.getCell(row, col).getPoly());
					}
					g.setColor(Color.BLACK);
					g.drawPolygon(previousCrystal.getCell(row, col).getPoly());
				}
			}*/
			
			for(CACell ca: previousCrystal.getList()) {
				if(ca.getCaCellState() == CACellState.ALIVE) {
					//System.out.println("alive");
					g.setColor(Color.RED);
					g.fillPolygon(ca.getPoly());
				}else if(ca.getCaCellState() == CACellState.DEAD) {
					g.setColor(Color.WHITE);
					g.fillPolygon(ca.getPoly());
					//System.out.println("dead");
				}
				g.setColor(Color.BLACK);
				g.drawPolygon(ca.getPoly());
				//System.out.println(ca.getCellID());
			}
			
		} catch (Exception e) {
			log.severe("Whoa!! Some exception occurred while setting up graphics. Details : " + e.toString());
		}

	}

	// Pausing the thread when User insists. Called by MAutomataDriver
	public void pauseThread() {
		paused = true;
	}

	// Going to the previous generation. Called by MAutomataDriver
	public void rewindThread() {
		rewind = true;
	}

	// Terminating the simulation. Called by MAutomataDriver
	public void stopThread() {
		stopped = true;
	}

	// Helper Method to add Regions to the Map
	public void addRegionToMap(int currentGen, CACrystal currentRegion) {
		caCrystalRecord.put(currentGen, currentRegion);
	}

	// Helper Method to add Regions to the Map
	public void removeRegionFromMap(int currentGen) {
		caCrystalRecord.remove(currentGen);
	}

	// Getters and Setters Section

	/**
	 * @return the maRegionRecord
	 */
	public Map<Integer, CACrystal> getMaRegionRecord() {
		return caCrystalRecord;
	}

	/**
	 * @param maRegionRecord the maRegionRecord to set
	 */
	public void setMaRegionRecord(Map<Integer, CACrystal> caCrystalRecord) {
		this.caCrystalRecord = caCrystalRecord;
	}

	/**
	 * @return the generationCount
	 */
	public int getGenerationCount() {
		return generationCount;
	}

	/**
	 * @param generationCount the generationCount to set
	 */
	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
	}

	/**
	 * @return the comboGenLimit
	 */
	public int getGenLimit() {
		return genLimit;
	}

	/**
	 * @param comboGenLimit the comboGenLimit to set
	 */
	public void setGenLimit(int genLimit) {
		this.genLimit = genLimit;
	}

	/**
	 * @return the completeFlag
	 */
	public boolean isCompleteFlag() {
		return completeFlag;
	}

	/**
	 * @param completeFlag the completeFlag to set
	 */
	public void setCompleteFlag(boolean completeFlag) {
		this.completeFlag = completeFlag;
	}

	/**
	 * @return the current Region
	 */
	public CACrystal getPreviousRegion() {
		return previousCrystal;
	}

	/**
	 * @param previousRegion the previousRegion to set
	 */
	public void setPreviousRegion(CACrystal currentCrystal) {
		this.previousCrystal = currentCrystal;
	}

	/**
	 * @return the paused
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * @param paused the paused to set
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * @return the rewind
	 */
	public boolean isRewind() {
		return rewind;
	}

	/**
	 * @param rewind the rewind to set
	 */
	public void setRewind(boolean rewind) {
		this.rewind = rewind;
	}

}
