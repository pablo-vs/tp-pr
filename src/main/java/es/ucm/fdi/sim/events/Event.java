package es.ucm.fdi.sim.events;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.exceptions.InvalidIdException;
import es.ucm.fdi.exceptions.InvalidEventException;

/**
*	Abstrac parent of all the Events
*/
public abstract class Event {

	private int time;

	
	public Event() {}
	public Event(int  t){
		time = t;
	}
	
	public int getTime(){
		return time;
	}


	public abstract void execute(RoadMap r);

	/**
	*	Abstract parent of all the EventBuilders
	*/
	public static abstract class EventBuilder {
		
		private Pattern checkID = Pattern.compile("[\\w_]");

		/**
		*	Checks the validity of object IDs from the INI file using a Pattern.
		*/
		public void checkIDValidity(String id) {
			Matcher m = checkID.matcher(id);
			if(!m.matches()) {
				throw new InvalidIdException(id);
			}
		}

		/**
		*	Build the event from a given INI section, returns null if the section tag does
		*	not match the event tag.
		*/
		public abstract Event build(IniSection section) throws InvalidEventException;
	}
}
