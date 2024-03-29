package es.ucm.fdi.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.events.NewVehicleEvent;
import es.ucm.fdi.sim.events.advanced.NewCarEvent;
import es.ucm.fdi.sim.events.advanced.NewBicycleEvent;
import es.ucm.fdi.sim.events.NewRoadEvent;
import es.ucm.fdi.sim.events.advanced.NewHighwayEvent;
import es.ucm.fdi.sim.events.advanced.NewDirtRoadEvent;
import es.ucm.fdi.sim.events.NewJunctionEvent;
import es.ucm.fdi.sim.events.advanced.NewRoundRobinEvent;
import es.ucm.fdi.sim.events.advanced.NewMostCrowedEvent;
import es.ucm.fdi.sim.events.MakeVehicleFaultyEvent;
import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.ini.IniError;
import es.ucm.fdi.exceptions.SimulatorException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.exceptions.UnreachableJunctionException;

import java.lang.IllegalArgumentException;

/**
 * Controller for the simulator, acts as an interface.
 *
 * @version 06.05.2018
 */
public class Controller {
	
	private static final String INVALID_INI_MSG = 
			"Error while reading events: Invalid Ini file.\n";
	private static final String GENERIC_INI_ERROR_MSG = 
			"Error while reading events";
	private static final String SIMULATOR_ERROR_MSG = 
			"Error while running simulation at time ";
	
	private static final Event.EventBuilder[] AVAILABLE_EVENTS = {
			//First advanced
			new NewCarEvent.Builder(), 
			new NewBicycleEvent.Builder(),
			
			new NewHighwayEvent.Builder(),
			new NewDirtRoadEvent.Builder(),
			
			new NewRoundRobinEvent.Builder(), 
			new NewMostCrowedEvent.Builder(),
			//Then the rest
			new NewRoadEvent.Builder(),
			new NewVehicleEvent.Builder(),
			new NewJunctionEvent.Builder(),
			new MakeVehicleFaultyEvent.Builder()};

	private Simulator sim = new Simulator();
	private OutputStream output = null;

	/**
	 * Default constructor
	 */
	public Controller(){}
	
	/**
	 * Constructor with streams, allows IO to/from resources other than files.
	 *
	 * @param input
	 *            An <code>InputStream</code> to read the events from.
	 * @param output
	 *            An <code>OutputStream</code> to send the reports.
	 */
	public Controller(InputStream input, OutputStream output)
			throws IOException {
		Logger.getLogger(Controller.class.getName()).info(
				"Initiating Controller...");
		sim = new Simulator();
		this.output = output;
		readEvents(input);
	}

	/**
	 * Reads the events from the input stream and inserts them into the
	 * simulator queue.
	 *
	 * @param in
	 *            InputStream from which to read events.
	 */
	public void readEvents(InputStream in) throws IOException {
		try {
			Ini events = new Ini(in);
			for (IniSection sec : events.getSections()) {
				Event e = parseEvent(sec);
				if (e == null) {
					throw new IllegalArgumentException("The tag "
							+ sec.getTag()
							+ " does not correspond to a valid event");
				} else {
					sim.insertEvent(e);
				}
			}
		} catch (IniError e) {
			Logger.getLogger(Controller.class.getName()).log(Level.WARNING,
					INVALID_INI_MSG, e);
			sim.fail(new IllegalArgumentException
				       (INVALID_INI_MSG + e.getMessage(), e));
		} catch (IllegalArgumentException | IOException e) {
			Logger.getLogger(Controller.class.getName()).log(Level.WARNING,
					GENERIC_INI_ERROR_MSG, e);
			sim.fail(e);
		}
	}

	public void redirectOutput(OutputStream os) {
		if (output != os) {
			output = os;
		} else {
			output = null;
		}
	}
    
	/**
	 * Resets the simulator.
	 */
	public void reset() {
		sim.reset();
	}

	/**
	 * Adds a Listener to the sim.
	 */
	public void addListener(Simulator.Listener l) {
		sim.addSimulatorListener(l);
	}

	/**
	 * Runs the simulation for the given number of steps.
	 *
	 * @param steps
	 *            The number of ticks to simulate.
	 */
	public void run(int steps) throws SimulatorException {
		try {
			sim.execute(steps, output);
		} catch (IllegalArgumentException | IOException
				| ObjectNotFoundException | UnreachableJunctionException e) {

			Logger.getLogger(Controller.class.getName()).log(Level.WARNING,
					SIMULATOR_ERROR_MSG + sim.getTimer(), e);
		        
			sim.fail(new SimulatorException(
					SIMULATOR_ERROR_MSG + sim.getTimer() + ".\n" 
					+ e.getMessage(), e));
		}
	}

	public void dumpOutput(OutputStream os) throws IOException {
		sim.dumpOutput(os);
	}

	/**
	 * @return Current simulator.
	 */
	public Simulator getSimulator() {
		return sim;
	}

	/**
	 * Attempts to create an <code>Event</code> from the given IniSection.
	 * Returns null if the section tag is unknown.
	 */
	private Event parseEvent(IniSection section) {
		Event result = null;
		Event.EventBuilder builder;
		int i = 0;
		boolean found = false;
		while (!found && i < AVAILABLE_EVENTS.length) {
			builder = AVAILABLE_EVENTS[i];
			Event e = builder.build(section);
			if (e != null) {
				found = true;
				result = e;
			}
			++i;
		}
		return result;
	}

}
