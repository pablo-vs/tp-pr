package es.ucm.fdi.sim.events.advanced;

import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.advanced.RoundRobin;
import es.ucm.fdi.sim.events.NewJunctionEvent;


/**
 * Represents the <code>Event</code> used to instantiate a new <code>Junction</code>.
 *
 * @version 10.03.2018
 */
public class NewRoundRobinEvent extends NewJunctionEvent {
	int min, max;
	
	/**
	 * Empty constructor.
	 */
	public NewRoundRobinEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param id ID of the junction.
	 */
	public NewRoundRobinEvent(int t, String id, int min, int max){
		super(t, id);
		this.min = min;
		this.max = max;
	}

	/**
	 * Constructor from NewJunctionEvent
	 */
	public NewRoundRobinEvent(NewJunctionEvent e, int min, int max) {
		super(e);
		this.min = min;
		this.max = max;
	}

	/**
	 * Instantiates a new junction, given the parameters are valid.
	 *
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r){
		r.addJunction(createRoundRobin());
	}

	public RoundRobin createRoundRobin() {
		return new RoundRobin(junctionID, min, max);
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewJunctionEvent.Builder{
	        private static final String TYPE = "rr";
	
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewRoundRobinEvent build(IniSection ini){
			NewRoundRobinEvent event = null;
			int min, max;
			        
			if(TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try{
					NewJunctionEvent ev = super.build(ini);
					min = parsePositiveInt(ini, "min_time_slice");
					max = parsePositiveInt(ini, "max_time_slice");
					event = new NewRoundRobinEvent(ev, min, max);
					
				} catch(Exception e){
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}	
			}
			
			return event;
		}
	}
}
