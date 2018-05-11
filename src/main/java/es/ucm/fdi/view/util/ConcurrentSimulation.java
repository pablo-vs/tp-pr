package es.ucm.fdi.view.util;

import es.ucm.fdi.control.Controller;

public class ConcurrentSimulation implements Runnable {

	Controller control;
	long delay;
	int steps;
	
	public ConcurrentSimulation(Controller control, long delay, int steps) {
		this.control = control;
		this.delay = delay;
		this.steps = steps;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < steps; ++i) {
			try {
				control.run(1);
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				break;
			}
		}

	}

}
