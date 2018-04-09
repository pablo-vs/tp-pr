package es.ucm.fdi.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import es.ucm.fdi.control.SimulatorAction;

public class SimWindow extends JFrame{
	public SimWindow() {
		super("test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		addToolbar();
		addCenterPanel();
		System.err.println("Hello world\n" + this);
		
	}

	public void addToolbar() {
		SimulatorAction action = new SimulatorAction("Exit", "exit.png", "Exit the program", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		JToolBar bar = new JToolBar();
		bar.add(action);
		add(bar, BorderLayout.NORTH);
	}

	public void addCenterPanel() {
		JSplitPane eastWestSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				createSouthWestPanel(), 
				createSouthEastPanel());		
		JSplitPane northSouthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
			createNorthPanel(), 
			eastWestSplit);
		add(northSouthSplit, BorderLayout.CENTER);
		setVisible(true);
		northSouthSplit.setDividerLocation(.5);
		eastWestSplit.setDividerLocation(.5);
	}

	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel rightPanel = new JPanel();
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
		JPanel southWestPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
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