package es.ucm.fdi.sim.events;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.exceptions.InvalidIDException;
import es.ucm.fdi.exceptions.InvalidEventException;

/**
 * Abstract parent of all the Events
 *
 * @version 10.03.2018
 */
public abstract class Event {

    private int time;

    /**
     * Empty constructor.
     */
    public Event() {}

    /**
     * Constructor with time.
     *
     * @param t The time of the event in the simulation.
     */
    public Event(int  t){
	time = t;
    }

    /**
     * Accesor method for time.
     *
     * @return The time of the event.
     */
    public int getTime(){
	return time;
    }

    /**
     * Execute the event.
     *
     * @param The <code>RoadMap</code> of the simulation, necessary to add entities.
     */
    public abstract void execute(RoadMap r);

    /**
     *	Abstract parent of all the EventBuilders
     */
    public static abstract class EventBuilder {
		
	private Pattern checkID = Pattern.compile("[\\w]+");

	/**
	 * Checks the validity of object IDs from the INI file using a Pattern.
	 *
	 * @param id The ID to check.
	 */
	public void checkIDValidity(String id) {
	    Matcher m = checkID.matcher(id);
	    if(!m.matches()) {
		  throw new InvalidIDException("Not a valid id: " + id);
	    }
	}

	/**
	 * Build the event from a given INI section, returns null if the section tag does
	 * not match the event tag.
	 *
	 * @param section The <code>IniSection</code> from which to parse the event.
	 */
	public abstract Event build(IniSection section) throws InvalidEventException;
    }
}
