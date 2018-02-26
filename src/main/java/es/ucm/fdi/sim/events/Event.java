package es.ucm.fdi.sim.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.InvalidIdException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
*	Abstrac parent of all the Events
*/
public abstract class Event {

	private String id;
	private int time;
	
	public Event() {}
	public Event(int  t, String id){
		this.id = id;
	}
	
	public String getID(){
		return id;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setTime(int t){
		time = t;
	}

	/**
	*	Abstract parent of all the EventBuilders
	*/
	abstract class EventBuilder {
		
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
