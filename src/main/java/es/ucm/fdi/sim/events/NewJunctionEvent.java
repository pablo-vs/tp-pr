package es.ucm.fdi.sim.events;

import java.lang.IllegalArgumentException;
import java.util.Map;
import java.util.logging.Logger;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;


/**
 * Represents the <code>Event</code> used to instantiate a new <code>Junction</code>.
 *
 * @version 10.03.2018
 */
public class NewJunctionEvent extends Event {
	protected String junctionID;

	/**
	 * Empty constructor.
	 */
	public NewJunctionEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param id ID of the junction.
	 */
	public NewJunctionEvent(int t, String id){
		super(t);
		junctionID = id;
	}

	/**
	 * Copy constructor.
	 */
	public NewJunctionEvent(NewJunctionEvent e) {
		this(e.getTime(), e.junctionID);
	}

	/**
	 * Instantiates a new junction, given the parameters are valid.
	 *
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r){
		Logger log = Logger.getLogger(NewJunctionEvent.class.getName());
		log.info("Attempting to execute NewJunctionEvent...");
		r.addJunction(createJunction());
		log.info("Event executed");
	}

	/**
	 * Creates a <code>Junction</code> given its id.
	 * @return	The newly created <code>Junction</code>.
	 */
	public Junction createJunction() {
		return new Junction(junctionID);
	}

	/**
	 * Equals method for this type of <code>Event</code>.
	 */
	@Override
	public boolean equals(Object o){
		return (o instanceof NewJunctionEvent 
    			&& junctionID.equals(((NewJunctionEvent)o).junctionID));
	}

	/**
	 * Return a  description of the event.
	 *
	 * @param out A <code>Map<String, String></code> which will contain the representation of the event.
	 */
	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Type", "New junction " + junctionID);
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends EventBuilder{
		public static final String TAG = "new_junction";
	
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewJunctionEvent build(IniSection ini){
			NewJunctionEvent event = null;
			String id;
			int time;
			        
			if(TAG.equals(ini.getTag())) {
				try{
					Logger log = Logger
						.getLogger(NewJunctionEvent.class.getName());
					
					log.info("Attempting to parse NewJunctionEvent...");
					
					id = parseID(ini, "id");
					time = parseTime(ini);
					
					event = new NewJunctionEvent(time, id);
					log.info("Event parsed");
				} catch(Exception e){
					throw new IllegalArgumentException
						("Error while parsing event:\n"
						 + e.getMessage(), e);
				}	
			}
			
			return event;
		}
	}
}
