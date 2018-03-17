package es.ucm.fdi.sim.events.advanced;

import java.util.List;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.advanced.DirtRoad;
import es.ucm.fdi.sim.events.NewRoadEvent;

/**
 * Represents the New Dirt Road Event.
 *
 * @version 17.03.2018
 */
public class NewDirtRoadEvent extends NewRoadEvent {

	/**
	 * Empty constructor.
	 */
	public NewDirtRoadEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param id ID of the DirtRoad.
	 * @param ini Initial junction.
	 * @param end Final junction.
	 * @param v Maximum speed.
	 * @param l Length of the road.
	 */
	public NewDirtRoadEvent(int t, String id, String ini, String end, int v, int l){
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
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) {
		r.addRoad(createDirtRoad(r));
	}

	/**
	 * Creates a new DirtRoad with the event parameters, if possible.
	 *
	 * @return The corresponding DirtRoad.
	 */
	public DirtRoad createDirtRoad(RoadMap r) {
		DirtRoad newDirtRoad;
		try {
			newDirtRoad = new DirtRoad(super.createRoad(r));
						
		} catch (IllegalArgumentException e){
			throw new IllegalArgumentException("Error: Could not create DirtRoad.\n" + e.getMessage(), e);
		}
		return newDirtRoad;
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewRoadEvent.Builder{
	        public static final String TYPE = "dirt";

		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewDirtRoadEvent build(IniSection ini){
			NewDirtRoadEvent event = null;
			
			if(TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try{
					NewRoadEvent ev = super.build(ini);
					event = new NewDirtRoadEvent(ev);
								 
				} catch (Exception e){
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}	
			}

			return event;
		}
	}
}
