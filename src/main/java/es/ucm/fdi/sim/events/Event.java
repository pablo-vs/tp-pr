package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.lang.NumberFormatException;

import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.Describable;

import java.lang.IllegalArgumentException;

/**
 * Abstract parent of all the Events
 *
 * @version 10.03.2018
 */
public abstract class Event implements Describable {

	protected static String EVENT_PARSE_ERROR_MSG = "Error while parsing the event:\n";
	private static String ERROR_PREFIX = "Error:";
	private static String INTEGER_PARSE_ERROR_MSG = "could not parse integer.";
	private static String NOT_FOUND_ERROR_MSG = " not found.";
	private static String NEGATIVE_ERROR_MSG = " must be positive.";
	private static String INVALID_ID_MSG = " Not a valid id ";

	private int time;
	
	private static final String[] COLUMNS = {"#", "Time", "Type"};

	/**
	 * Empty constructor.
	 */
	public Event() {
	}

	/**
	 * Constructor with time.
	 *
	 * @param t
	 *            The time of the event in the simulation.
	 */
	public Event(int t) {
		time = t;
	}

	/**
	 * Accesor method for time.
	 *
	 * @return The time of the event.
	 */
	public int getTime() {
		return time;
	}

	/**
	 * Execute the event.
	 *
	 * @param The
	 *            <code>RoadMap</code> of the simulation, necessary to add
	 *            entities.
	 */
	public abstract void execute(RoadMap r);

	/**
	 * Return a description of the event.
	 *
	 * @param out
	 *            A <code>Map<String, String></code> which will contain the
	 *            representation of the event.
	 */
	@Override
	public void describe(Map<String, String> out) {
		out.put("Time", "" + time);
	}

	protected Junction verifyJunction(RoadMap r, String id) {
		Junction j = r.getJunction(id);
		if (j == null) {
			throw new ObjectNotFoundException(ERROR_PREFIX + INVALID_ID_MSG
					+ "'" + id + "'");
		}
		return j;
	}

	public static String[] getColumns() {
		return COLUMNS;
	}
	
	/**
	 * Abstract parent of all the <code>EventBuilders</code>
	 */
	public static abstract class EventBuilder {
		private static final Pattern checkID = Pattern.compile("[\\w]+");

		/**
		 * Checks the validity of object IDs from the <Code>Ini</code> file
		 * using a <code>Pattern</code>.
		 *
		 * @param id
		 *            The ID to check.
		 */
		public static void checkIDValidity(String id) {
			Matcher m = checkID.matcher(id);
			if (!m.matches()) {
				throw new IllegalArgumentException(ERROR_PREFIX
						+ INVALID_ID_MSG + "'" + id + "'");
			}
		}

		/**
		 * Build the event from a given <code>IniSection</code>, returns null if
		 * the section tag does not match the event tag.
		 *
		 * @param section
		 *            The <code>IniSection</code> from which to parse the event.
		 */
		public abstract Event build(IniSection section)
				throws IllegalArgumentException;

		/**
		 * Parses an event ID and checks for its validity.
		 * 
		 * @param sec
		 *            Section from which to parse.
		 * @param key
		 *            Key associated with the id.
		 * @return The wanted ID, if valid.
		 */
		public static String parseID(IniSection sec, String key) {
			String id = sec.getValue(key);
			if (id == null) {
				throw new IllegalArgumentException(ERROR_PREFIX + " " + key
						+ NOT_FOUND_ERROR_MSG);
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
			if (str == null) {
				throw new IllegalArgumentException(ERROR_PREFIX + " " + key
						+ NOT_FOUND_ERROR_MSG);
			}
			int result;
			try {
				result = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERROR_PREFIX
						+ INTEGER_PARSE_ERROR_MSG + str, e);
			}
			if (result < threshold) {
				throw new IllegalArgumentException(ERROR_PREFIX + " " + key
						+ NEGATIVE_ERROR_MSG);
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
			if (str == null) {
				throw new IllegalArgumentException(ERROR_PREFIX + " " + key
						+ NOT_FOUND_ERROR_MSG);
			}
			ArrayList<String> result = new ArrayList<String>(Arrays.asList(str
					.split(",")));
			result.forEach((o) -> checkIDValidity(o));
			return result;
		}

		public static double parseDouble(IniSection sec, String key) {
			String str = sec.getValue(key);
			if (str == null) {
				throw new IllegalArgumentException(ERROR_PREFIX + " " + key
						+ NOT_FOUND_ERROR_MSG);
			}
			double result;
			try {
				result = Double.parseDouble(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERROR_PREFIX
						+ INTEGER_PARSE_ERROR_MSG + str, e);
			}
			return result;
		}
	}
}
