package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.NumberFormatException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import java.lang.IllegalArgumentException;

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
	 *	Abstract parent of all the <code>EventBuilders</code>
	 */
	public static abstract class EventBuilder {		
		private static final Pattern checkID = Pattern.compile("[\\w]+");
	
		/**
		 * Checks the validity of object IDs from the <Code>Ini</code> file using a 
		 * <code>Pattern</code>.
		 *
		 * @param id The ID to check.
		 */
		public static void checkIDValidity(String id) {
			Matcher m = checkID.matcher(id);
			if(!m.matches()) {
				throw new IllegalArgumentException("Not a valid id: " + id);
			}
		}
	
		/**
		 * Build the event from a given <code>IniSection</code>, returns null if the section tag
		 * does not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		public abstract Event build(IniSection section) throws IllegalArgumentException;


		
		
		public static String parseID(IniSection sec, String key) {
			String id = sec.getValue(key);
			if(id == null) {
				throw new IllegalArgumentException("Error: " + key + " not found");
			}
			checkIDValidity(id);
			return id;
		}

		public static int parseTime(IniSection sec) {
			return parseInt(sec, "time", 0);
		}

		public static boolean isCorrectType(IniSection sec, String expected) {
			String type = sec.getValue("type");
			return type != null && expected.equals(type);
		}

		public static int parseInt(IniSection sec, String key, int threshold) {
			String str = sec.getValue(key);
			if(str == null) {
				throw new IllegalArgumentException("Error: + " + key
									   + " not found");
			}
			int result;
			try {
				result = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Error: could not parse integer "
								   + str, e);
			}
			if(result < threshold) {
				throw new IllegalArgumentException("Error: "
								   + key + " must be positive");
			}
			return result;
		}
		
		public static int parsePositiveInt(IniSection sec, String key) {
			return parseInt(sec, key, 1);
		}

		public static long parseSeedOrMillis(IniSection sec) {
			String str = sec.getValue("seed");
			long result;
			try {
				result = Long.parseLong(str);
			} catch (NumberFormatException | NullPointerException e) {
				result = System.currentTimeMillis();
			}
			return result;
		}

		public static List<String> parseIDList(IniSection sec, String key) {
			String str = sec.getValue(key);
			if(str == null) {
				throw new IllegalArgumentException("Error: + " + key
									   + " not found");
			}
			ArrayList<String> result = new ArrayList(Arrays.asList(str.split(",")));
			result.forEach((o) -> checkIDValidity(o));
			return result;
		}
		
		public static double parseDouble(IniSection sec, String key) {
			String str = sec.getValue(key);
			if(str == null) {
				throw new IllegalArgumentException("Error: + " + key
									   + " not found");
			}
			double result;
			try {
				result = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Error: could not parse integer "
								   + str, e);
			}
			return result;
		}
	}
}
