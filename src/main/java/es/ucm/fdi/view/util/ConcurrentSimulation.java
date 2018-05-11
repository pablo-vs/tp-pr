package es.ucm.fdi.view.util;

import java.util.logging.Logger;

import es.ucm.fdi.control.Controller;

public class ConcurrentSimulation implements Runnable {

	Controller control;
	int delay;
	int steps;
	
	public ConcurrentSimulation(Controller control, int delay, int steps) {
		this.control = control;
		this.delay = delay;
		this.steps = steps;
	}
	
	@Override
	public void run() {
		Logger.getLogger(ConcurrentSimulation.class.getName()).info("Starting simulation thread");
		for(int i = 0; i < steps; ++i) {
			try {
				control.run(1);
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				return;
			}
		}

	}

}
