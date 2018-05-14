package es.ucm.fdi.view.util;

import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.view.SimWindow;

public class ConcurrentSimulation implements Runnable {

	SimWindow simWindow;
	Controller control;
	int delay;
	int steps;
	
	public ConcurrentSimulation(Controller control, int delay, int steps, SimWindow simWindow) {
		this.control = control;
		this.delay = delay;
		this.steps = steps;
		this.simWindow = simWindow;
	}
	
	@Override
	public void run() {
		Actions[] actions = {Actions.CLEAR_EDITOR,  
							Actions.DELETE_REPORT, 
							Actions.INSERT_EVENT_DATA, 
							Actions.LOAD_EVENT, 
							Actions.PLAY, 
							Actions.REDIRECT_OUTPUT, 
							Actions.REPORT, 
							Actions.REPORT_SELECTED,
							Actions.RESET, 
							Actions.SAVE_EVENT, 
							Actions.SAVE_REPORT}; 
		
		simWindow.lockActions(actions);
		for(int i = 0; i < steps; ++i) {
			try {
				SwingUtilities.invokeLater(()->control.run(1));
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				return;
			}
		}
		simWindow.unlockActions(actions);
		Logger.getLogger(ConcurrentSimulation.class.getName()).info("Starting simulation thread");

	}

}
