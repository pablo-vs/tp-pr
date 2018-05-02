package es.ucm.fdi.sim.events;

import java.lang.IllegalArgumentException;
import java.util.Map;
import java.util.logging.Logger;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
 * Represents the New Road Event.
 *
 * @version 10.03.2018
 */
public class NewRoadEvent extends Event {
	protected String roadID, ini, end;
	protected int length, maxVel;

	/**
	 * Empty constructor.
	 */
	public NewRoadEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param id ID of the road.
	 * @param ini Initial junction of the road.
	 * @param end Final junction of the road.
	 * @param v Maximum speed of the road.
	 * @param l Length of the road.
	 */
	public NewRoadEvent(int t, String id, String ini, String end, int v, int l){
		super(t);
		roadID = id;
		this.ini = ini;
		this.end = end;
		maxVel = v;
		length = l;
	}

	/**
	 * Copy constructor.
	 */
	public NewRoadEvent(NewRoadEvent e) {
		this(e.getTime(), e.roadID, e.ini, e.end, e.maxVel, e.length);
	}
	
	/**
	 * Instantiates a new road, given the parameters are valid.
	 *
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) {
		Logger log = Logger.getLogger(NewRoadEvent.class.getName());
		log.info("Attempting to execute NewRoadEvent...");
		r.addRoad(createRoad(r));
		log.info("Event executed");
	}

	/**
	 * Creates a new Road with the event parameters, if possible.
	 *
	 * @return The corresponding Road.
	 */
	public Road createRoad(RoadMap r) {
		Junction iniJ, endJ;
		Road newRoad;
		try {
			iniJ = verifyJunction(r, ini);
			endJ = verifyJunction(r, end);
			newRoad = new Road(roadID, length, maxVel, iniJ, endJ);
			
			
		} catch (ObjectNotFoundException e){
			throw new IllegalArgumentException("Error: Could not create road "
		+ roadID + " at time " + getTime() + ".\n" + e.getMessage(), e);
		}
		return newRoad;
	}

    
	@Override
	public boolean equals(Object o){
		boolean isEqual = false;
    	
		if(o != null && o instanceof NewRoadEvent){
			isEqual = (roadID.equals(((NewRoadEvent)o).roadID) 
				   && ini.equals(((NewRoadEvent)o).ini) 
				   && end.equals(((NewRoadEvent)o).end) 
				   && length == ((NewRoadEvent)o).length 
				   && maxVel == ((NewRoadEvent)o).maxVel);
		}
    	
		return isEqual;
	}

	/**
	 * Return a  description of the event.
	 *
	 * @param out A <code>Map<String, String></code> which will contain the representation of the event.
	 */
	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Type", "New road " + roadID);
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends EventBuilder{
		public static final String TAG = "new_road";

		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewRoadEvent build(IniSection ini){
			NewRoadEvent event = null;
			int time, maxVel, length;
			String id, iniJ, endJ;
			
			if(TAG.equals(ini.getTag())) {
				try{
					Logger log = Logger.getLogger(NewRoadEvent.class.getName());
					log.info("Attempting to parse NewRoadEvent...");
					
					time = parseTime(ini);
					id = parseID(ini, "id");
					iniJ = parseID(ini, "src");
					endJ = parseID(ini, "dest");
					maxVel = parsePositiveInt(ini, "max_speed");
					length = parsePositiveInt(ini, "length");
					
					event = new NewRoadEvent(time, id, iniJ, endJ, maxVel, length);
					log.info("Event parsed");
				} catch (Exception e){
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}	
			}
			
			return event;
		}
	}
}
