package es.ucm.fdi.sim.events.advanced;

import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.advanced.MostCrowed;
import es.ucm.fdi.sim.events.NewJunctionEvent;


/**
 * Represents the <code>Event</code> used to instantiate a new <code>Junction</code>.
 *
 * @version 10.03.2018
 */
public class NewMostCrowedEvent extends NewJunctionEvent {
	
	/**
	 * Empty constructor.
	 */
	public NewMostCrowedEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param id ID of the junction.
	 */
	public NewMostCrowedEvent(int t, String id){
		super(t, id);
	}

	/**
	 * Constructor from NewJunctionEvent
	 */
	public NewMostCrowedEvent(NewJunctionEvent e) {
		super(e);
	}

	/**
	 * Instantiates a new junction, given the parameters are valid.
	 *
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r){
		r.addJunction(createMostCrowed());
	}

	public MostCrowed createMostCrowed() {
		return new MostCrowed(super.createJunction());
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewJunctionEvent.Builder{
	        private static final String TYPE = "mc";
	
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewMostCrowedEvent build(IniSection ini){
			NewMostCrowedEvent event = null;
			        
			if(TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try{
					event = new NewMostCrowedEvent(super.build(ini));
					
				} catch(Exception e){
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}	
			}
			
			return event;
		}
	}
}
