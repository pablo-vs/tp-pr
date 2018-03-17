package es.ucm.fdi.sim.events.advanced;

import java.util.List;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.advanced.Bicycle;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.events.NewVehicleEvent;

/**
 * Represents the New Bicycle event.
 */
public class NewBicycleEvent extends NewVehicleEvent {

	/**
	 * Empty constructor.
	 */
	public NewBicycleEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param v Maximum speed of the bicycle.
	 * @param id ID of the bicycle.
	 * @param it Itinerary of the bicycle.
	 */
	public NewBicycleEvent(int t, int v, String id, List<String> it){
		super(t, v, id, it);
	}
	
	/**
	 * Constructor from NewVehicleEvent.
	 */	 
       	public NewBicycleEvent(NewVehicleEvent e) {
		super(e);
	}
	
	/**
	 * Instantiates a new car, given the parameters are valid.
	 * 
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) throws IllegalArgumentException {
		r.addVehicle(createBicycle(r));
	}

	/**
	 * Creates a new Bicycle with the event parameters, if possible.
	 *
	 * @return The corresponding Bicycle.
	 */
	public Bicycle createBicycle(RoadMap r) {
		Bicycle result;
		try{
			result = new Bicycle(super.createVehicle(r));
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error: could not create Bicycle.\n"
							   + e.getMessage(), e);
		}
		return result;
	}
	   
	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewVehicleEvent.Builder {
		public static final String TYPE = "bike";
		
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag or the type is incorrect.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewBicycleEvent build(IniSection ini) throws IllegalArgumentException {
			NewBicycleEvent event = null;
			
			if(TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				
				try	{
					
					NewVehicleEvent ev = super.build(ini);
					event = new NewBicycleEvent(ev);
					
				} catch(Exception e) {
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}
			}
	
			return event;
		}
	}
}
