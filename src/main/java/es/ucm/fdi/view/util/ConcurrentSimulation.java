package es.ucm.fdi.view.util;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.view.SimWindow;

public class ConcurrentSimulation implements Runnable {

	String[] actions;
	Controller control;
	SimWindow simWindow;
	int delay;
	int steps;

	public ConcurrentSimulation(Controller control, int delay, int steps,
			SimWindow simWindow, String[] actions) {
		this.control = control;
		this.delay = delay;
		this.steps = steps;
		this.simWindow = simWindow;
		this.actions = actions;
	}

	@Override
	public void run() {
		simWindow.lockActions(actions);
		for (int i = 0; i < steps; ++i) {
			try {
				SwingUtilities.invokeLater(() -> control.run(1));
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				return;
			}
		}
		simWindow.unlockActions(actions);
		Logger.getLogger(ConcurrentSimulation.class.getName()).info(
				"Starting simulation thread");

	}

}
