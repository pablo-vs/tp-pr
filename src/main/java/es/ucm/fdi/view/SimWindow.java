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

		setSize(1000, 1000);
		setVisible(true);
	}

	public void addToolbar() {
		SimulatorAction action = new SimulatorAction("Exit", "exit.png", "Exit the program", KeyEvent.VK_S, "control shift S", ()->System.exit(0));
		JToolBar bar = new JToolBar();
		bar.add(action);
		add(bar, BorderLayout.NORTH);
	}
}