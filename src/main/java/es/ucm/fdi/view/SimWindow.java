package es.ucm.fdi.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;
import javax.swing.text.StyleConstants;

import es.ucm.fdi.control.SimulatorAction;

public class SimWindow extends JFrame{
	public SimWindow() {
		super("Traffic Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addToolbar();
		addCenterPanel();

		setSize(1000, 1000);
		setVisible(true);
	}

	public void addToolbar() {
		SimulatorAction load = new SimulatorAction("Load", "open.png", "Loads an input file",
						KeyEvent.VK_L, "control shift L", ()->{
							//doStuff
				}),
				save = new SimulatorAction("Save", "save.png", "Saves the event data in a file",
						KeyEvent.VK_S, "control shift S", ()->{
							//doStuff
				}),
				clear = new SimulatorAction("Clear", "clear.png", "Clears the current event data",
						KeyEvent.VK_C, "control shift C", ()->{
							//doStuff
				}),
				insert = new SimulatorAction("Insert", "events.png", "Adds the event data to the event queue",
						KeyEvent.VK_I, "control shift I", ()->{
							//doStuff
						}),
				execute = new SimulatorAction("Insert", "play.png", "Executes the indicated steps",
						KeyEvent.VK_X, "control shift X", ()->{
							//doStuff
						}),
				reset = new SimulatorAction("Reset", "reset.png", "Resets the simulation to its initial point",
						KeyEvent.VK_R, "control shift R", ()->{
							//doStuff
						}),
				exit = new SimulatorAction("Exit", "exit.png", "Exit the program",
						KeyEvent.VK_E, "control shift E", ()->System.exit(0));
				
		JLabel stepsLabel = new JLabel(" Steps: "), timeLabel = new JLabel(" Time: ");
		
		JSpinner steps = new JSpinner();
		((SpinnerNumberModel) steps.getModel()).setMinimum(0);
		steps.setPreferredSize(new Dimension(100,10));
		
		JTextField time = new JTextField();
		time.setPreferredSize(new Dimension(100,10));
		time.setText("0"); //initial
		time.setEditable(false);
		
		//Non-elegant solution for 
		
		JToolBar bar = new JToolBar();
		bar.add(load);
		bar.add(save);
		bar.add(clear);
		bar.add(insert);
		bar.add(execute);
		bar.add(reset);
		
		//Here goes the spinner
		bar.add(stepsLabel);
		bar.add(steps);
		
		//Here goes the JTextPanes
		bar.add(timeLabel);
		bar.add(time);
		
		bar.add(exit);
		add(bar, BorderLayout.NORTH);
	}

	public void addCenterPanel() {
		JPanel northPanel = createNorthPanel(), southPanel = new JPanel();
		southPanel.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createSouthWestPanel(), createSouthEastPanel()), BorderLayout.CENTER);
		JSplitPane centerPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, northPanel, southPanel);
		add(centerPanel, BorderLayout.CENTER);
	}

	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel(), leftPanel = new JPanel(), centerPanel = new JPanel(), rightPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		leftPanel.setBackground(Color.blue);
		centerPanel.setBackground(Color.green);
		rightPanel.setBackground(Color.red);
		northPanel.add(leftPanel);
		northPanel.add(centerPanel);
		northPanel.add(rightPanel);
		return northPanel;
	}

	public JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel(), northPanel = new JPanel(), centerPanel = new JPanel(), southPanel = new JPanel();
		southWestPanel.setLayout(new BoxLayout(southWestPanel, BoxLayout.Y_AXIS));
		northPanel.setBackground(Color.pink);
		centerPanel.setBackground(Color.yellow);
		southPanel.setBackground(Color.cyan);
		southWestPanel.add(northPanel);
		southWestPanel.add(centerPanel);
		southWestPanel.add(southPanel);
		return southWestPanel;
	}

	public JPanel createSouthEastPanel() {
		return new JPanel();
	}
}