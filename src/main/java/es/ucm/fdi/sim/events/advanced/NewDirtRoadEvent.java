package es.ucm.fdi.sim.events.advanced;

import java.lang.IllegalArgumentException;
import java.util.Map;
import java.util.logging.Logger;

import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.advanced.DirtRoad;
import es.ucm.fdi.sim.events.NewRoadEvent;

/**
 * Represents the New Dirt Road Event.
 *
 * @version 06.05.2018
 */
public class NewDirtRoadEvent extends NewRoadEvent {

	/**
	 * Empty constructor.
	 */
	public NewDirtRoadEvent() {
	}

	/**
	 * Full constructor.
	 *
	 * @param t
	 *            Time of the event.
	 * @param id
	 *            ID of the DirtRoad.
	 * @param ini
	 *            Initial junction.
	 * @param end
	 *            Final junction.
	 * @param v
	 *            Maximum speed.
	 * @param l
	 *            Length of the road.
	 */
	public NewDirtRoadEvent(int t, String id, String ini, String end, int v,
			int l) {
		super(t, id, ini, end, v, l);
	}

	/**
	 * Constructor from NewRoadEvent.
	 */
	public NewDirtRoadEvent(NewRoadEvent e) {
		super(e);
	}

	/**
	 * Instantiates a new road, given the parameters are valid.
	 *
	 * @param r
	 *            The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) {
		Logger log = Logger.getLogger(NewDirtRoadEvent.class.getName());
		log.info("Attempting to execute NewDirtRoadEvent...");
		r.addRoad(createDirtRoad(r));
		log.info("Event executed");
	}

	/**
	 * Creates a new DirtRoad with the event parameters, if possible.
	 *
	 * @return The corresponding DirtRoad.
	 */
	public DirtRoad createDirtRoad(RoadMap r) {
		DirtRoad newDirtRoad;
		try {
			newDirtRoad = new DirtRoad(roadID, length, maxVel, verifyJunction(
					r, ini), verifyJunction(r, end));

		} catch (IllegalArgumentException | ObjectNotFoundException e) {
			throw new IllegalArgumentException(
					"Error: Could not create DirtRoad " + roadID + " at time "
							+ getTime() + ".\n" + e.getMessage(), e);
		}
		return newDirtRoad;
	}

	/**
	 * Return a description of the event.
	 *
	 * @param out
	 *            A <code>Map<String, String></code> which will contain the
	 *            representation of the event.
	 */
	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Type", "New DirtRoad " + roadID);
	}

	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewRoadEvent.Builder {
		public static final String TYPE = "dirt";

		/**
		 * Build the event from a given INI section, returns null if the section
		 * tag does not match the event tag.
		 *
		 * @param section
		 *            The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewDirtRoadEvent build(IniSection ini) {
			NewDirtRoadEvent event = null;

			if (TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try {
					Logger log = Logger.getLogger(NewDirtRoadEvent.class
							.getName());
					log.info("Attempting to parse NewDirtRoadEvent...");

					NewRoadEvent ev = super.build(ini);
					event = new NewDirtRoadEvent(ev);
					log.info("Event parsed");
				} catch (Exception e) {
					throw new IllegalArgumentException(
							EVENT_PARSE_ERROR_MSG + e.getMessage(), e);
				}
			}

			return event;
		}
	}
}
