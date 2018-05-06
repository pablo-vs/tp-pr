package es.ucm.fdi.sim.events.advanced;

import java.lang.IllegalArgumentException;
import java.util.Map;
import java.util.logging.Logger;

import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.advanced.Highway;
import es.ucm.fdi.sim.events.NewRoadEvent;

/**
 * Represents the New Highway Event.
 *
 * @version 06.05.2018
 */
public class NewHighwayEvent extends NewRoadEvent {
	private int lanes;

	/**
	 * Empty constructor.
	 */
	public NewHighwayEvent() {
	}

	/**
	 * Full constructor.
	 *
	 * @param t
	 *            Time of the event.
	 * @param id
	 *            ID of the highway.
	 * @param ini
	 *            Initial junction.
	 * @param end
	 *            Final junction.
	 * @param v
	 *            Maximum speed.
	 * @param l
	 *            Length of the road.
	 * @param lanes
	 *            Number of lanes.
	 */
	public NewHighwayEvent(int t, String id, String ini, String end, int v,
			int l, int lanes) {
		super(t, id, ini, end, v, l);
		this.lanes = lanes;
	}

	/**
	 * Constructor from NewRoadEvent.
	 */
	public NewHighwayEvent(NewRoadEvent e, int lanes) {
		super(e);
		this.lanes = lanes;
	}

	/**
	 * Instantiates a new road, given the parameters are valid.
	 *
	 * @param r
	 *            The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) {
		Logger log = Logger.getLogger(NewHighwayEvent.class.getName());
		log.info("Attempting to execute NewHighwayEvent...");
		r.addRoad(createHighway(r));
		log.info("Event executed");
	}

	/**
	 * Creates a new Highway with the event parameters, if possible.
	 *
	 * @return The corresponding Highway.
	 */
	public Highway createHighway(RoadMap r) {
		Highway newHighway;
		try {
			newHighway = new Highway(roadID, length, maxVel, verifyJunction(r,
					ini), verifyJunction(r, end), lanes);

		} catch (IllegalArgumentException | ObjectNotFoundException e) {
			throw new IllegalArgumentException(
					"Error: Could not create Highway " + roadID + " at time "
							+ getTime() + ".\n" + e.getMessage(), e);
		}
		return newHighway;
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
		out.put("Type", "New Highway " + roadID);
	}

	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewRoadEvent.Builder {
		public static final String TYPE = "lanes";

		/**
		 * Build the event from a given INI section, returns null if the section
		 * tag does not match the event tag.
		 *
		 * @param section
		 *            The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewHighwayEvent build(IniSection ini) {
			NewHighwayEvent event = null;
			int lanes;

			if (TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try {
					Logger log = Logger.getLogger(NewHighwayEvent.class
							.getName());
					log.info("Attempting to parse NewHighwayEvent...");

					NewRoadEvent ev = super.build(ini);
					lanes = parsePositiveInt(ini, "lanes");
					event = new NewHighwayEvent(ev, lanes);

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
