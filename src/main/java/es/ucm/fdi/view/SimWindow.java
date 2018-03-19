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

		addToolbar();
		addCenterPanel();

		setSize(1000, 1000);
		setVisible(true);
	}

	public void addToolbar() {
		SimulatorAction action = new SimulatorAction("Exit", "exit.png", "Exit the program", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		JToolBar bar = new JToolBar();
		bar.add(action);
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