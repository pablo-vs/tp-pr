package es.ucm.fdi.control;

import java.util.Map;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.events.NewVehicleEvent;
import es.ucm.fdi.sim.events.NewRoadEvent;
import es.ucm.fdi.sim.events.NewJunctionEvent;
import es.ucm.fdi.sim.events.MakeVehicleFaultyEvent;
import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.exceptions.InvalidEventException;

/**
 * Controller for the simulator, acts as an interface.
 *
 * @version 10.03.2018
 */

public class Controller {
    
    private static final Event.EventBuilder [] AVAILABLE_EVENTS = {
	new NewVehicleEvent.Builder(),
	new NewRoadEvent.Builder(),
	new NewJunctionEvent.Builder(),
	new MakeVehicleFaultyEvent.Builder()
    };

    private Simulator sim;
    private InputStream input;
    private OutputStream output;

    /**
     * Empty constructor, sets IO to stdin and stdout.
     */
    public Controller() throws IOException {
	sim = new Simulator();
	input = System.in;
	output = System.out;
	readEvents();
    }

    /**
     * Constructor with streams, allows IO to/from resources other than files.
     *
     * @param input An <code>InputStream</code> to read the events from.
     * @param output An <code>OutputStream</code> to send the reports.
     */
    public Controller(InputStream input, OutputStream output) throws IOException {
	sim = new Simulator();
	this.input = input;
	this.output = output;
	readEvents();
    }

    /**
     * Constructor with input file path and output to stdout.
     *
     * @param inputPath The path of the input file.
     */
    public Controller(String inputPath) throws IOException {
	sim = new Simulator();
	input = new FileInputStream(inputPath);
	readEvents();
    }
    
    /**
     * Constructor with file paths.
     *
     * @param inputPath The path of the input file.
     * @param outputPath The path of the output file.
     */
    public Controller(String inputPath, String outputPath) throws IOException {
	sim = new Simulator();
	input = new FileInputStream(inputPath);
	output = new FileOutputStream(outputPath);
	readEvents();
    }

    /*
      Reads the events from the input stream and inserts them into the simulator queue.
     */
    private void readEvents() throws IOException {
	Ini events = new Ini(input);
	for(IniSection sec : events.getSections()) {
	    Event e = parseEvent(sec);
	    if(e == null) {
		throw new InvalidEventException("The tag " + sec.getTag()
						+ " does not correspond to a valid event");
	    } else {
		sim.insertEvent(e);
	    }
	}
    }

    /**
     * Runs the simulation for the given number of steps.
     *
     * @param steps The number of ticks to simulate.
     */
    public void run(int steps) throws IOException {
	sim.execute(steps, output);
    }

    /*
      Attempts to create an Event from que given IniSection.
      Returns null if the section tag is not known.
     */
    private Event parseEvent(IniSection section) {
	Event result = null;
	Event.EventBuilder builder;
	int i = 0;
	boolean found = false;
	while(!found && i < AVAILABLE_EVENTS.length) {
	    builder = AVAILABLE_EVENTS[i];
	    Event e = builder.build(section);
	    if(e != null) {
		found = true;
		result = e;
	    }
	    ++i;
	}
	return result;
    }
    
}
