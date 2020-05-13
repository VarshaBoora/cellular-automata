package edu.neu.csye6200.ca;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

public abstract class CAApp implements ActionListener, WindowListener {
	protected JFrame frame = null;
	

	
	public CAApp() {
		initGUI();
	}
    public void initGUI() {
    	frame = new JFrame();
		frame.setTitle("2D Cellular Automata Crystal Growth");

		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //JFrame.DISPOSE_ON_CLOSE)
		
		// Permit the app to hear about the window opening
		frame.addWindowListener(this); 
		
		frame.setLayout(new BorderLayout());
		frame.add(getMainPanel(), BorderLayout.CENTER);

    }
    
	public abstract JPanel getMainPanel();

}
