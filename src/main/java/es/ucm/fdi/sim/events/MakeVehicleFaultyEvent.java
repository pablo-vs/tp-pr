package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import java.lang.IllegalArgumentException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
 * Represents an <code>Event</code> that sets a list of <code>Vehicles</code> to faulty.
 *
 * @version 28.02.2018
 */
public class MakeVehicleFaultyEvent extends Event {
	private List<String> vehicles;
	private int duration;
	
	/**
	 * Empty constructor.
	 */
	public MakeVehicleFaultyEvent(){}	
	
	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param vehicles Identifiers of the <code>Vehicles</code> to break.
	 * @param duration Duration of the fault.
	 */
	public MakeVehicleFaultyEvent(int t, List<String> vehicles, int duration){
		super(t);
		this.vehicles = new ArrayList<String>(vehicles);
		this.duration = duration;
	}
	
	@Override
	public void execute(RoadMap r) throws IllegalArgumentException{
		for(String s : vehicles){
			try{
				r.getVehicle(s).setBrokenTime(duration);
			} catch(ObjectNotFoundException e) {
				throw new IllegalArgumentException("Error: vehicle " + s + " not found.\n" + e.getMessage());
			}
		}
	}
	
	@Override
	public boolean equals(Object o){
		boolean isEqual = false;
		
		if(o != null && o instanceof MakeVehicleFaultyEvent){
    		isEqual = (duration == ((MakeVehicleFaultyEvent)o).duration) && 
    				(vehicles.size() == ((MakeVehicleFaultyEvent)o).vehicles.size());

    		for(int i = 0; i < vehicles.size() && isEqual; ++i){
    			isEqual = (vehicles.get(i).equals(((MakeVehicleFaultyEvent)o).vehicles.get(i)));
    		}
    		
    	}
		
		return isEqual;
	}

	/**
	 * Return a  description of the event.
	 *
	 * @return A <code>String</code> representing the event.
	 */
	@Override
	public String getDescription() {
		return "Break vehicles " + String.join(" ", vehicles);
	}

	/**
	 * Builder for this event.
	 *
	 * @version 28.02.2016
	 */
	public static class Builder extends EventBuilder{
		public static final String TAG = "make_vehicle_faulty";
		
		/**
		 * Attempts to build a <code>MakeVehicleFaultyEvent</code> from the given <code>IniSection</code>.
		 *
		 * @param ini The <code>IniSection</code> from which to build the <code>Event</code>.
		 * @return A <code>MakeVehicleFaultyEvent</code>, or <code>null</code> if there were parsing errors.
		 */
		public MakeVehicleFaultyEvent build(IniSection ini) throws IllegalArgumentException{
			MakeVehicleFaultyEvent event;
			int time, duration;
			List<String> vehicles;
			
			event = null;
			if(TAG.equals(ini.getTag())) {
				try	{
				    time = parseTime(ini);
					duration = parsePositiveInt(ini, "duration");
					vehicles = parseIDList(ini, "vehicles");
					vehicles.forEach((id) -> checkIDValidity(id));
									       							    
				} catch(Exception e) {
				    throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}

				event = new MakeVehicleFaultyEvent(time, vehicles, duration);
				
			}

			return event;
		}
	}
}
