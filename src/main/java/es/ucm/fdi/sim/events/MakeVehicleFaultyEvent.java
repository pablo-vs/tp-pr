package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
 * Represents a Make Vehicle Faulty <code>Event</code>.
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
	
	public void execute(RoadMap r) throws InvalidEventException{
		for(String s : vehicles){
			try{
				r.getVehicle(s).setBrokenTime(duration);
			}catch(ObjectNotFoundException e){
				throw new InvalidEventException("Error: vehicle not found.\n" + e.getMessage());
			}
		}
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
		 * @param ini The section from which to build the <code>Event</code>.
		 * @return A <code>MakeVehicleFaultyEvent</code>, or <code>null</code> if there were parsing errors.
		 */
		public MakeVehicleFaultyEvent build(IniSection ini){
			MakeVehicleFaultyEvent event;
			int time, duration;
			List<String> vehicles;
			String timeStr, vehiclesIdStr, durationStr;
			
			event = null;
			if(TAG.equals(ini.getTag())) {
				try	{
					//Check existence of all necessary keys and read the attributes
					//This ignores other unnecessary keys
					timeStr = ini.getValue("time");
					vehiclesIdStr = ini.getValue("vehicles");
					durationStr = ini.getValue("duration");

					//Parse the attributes
				    time = Integer.parseInt(timeStr);
					duration = Integer.parseInt(durationStr);

					vehicles = Arrays.asList(vehiclesIdStr.split(","));
					vehicles.forEach((id) -> checkIDValidity(id));
									       							    
				} catch(Exception e) {
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}

				event = new MakeVehicleFaultyEvent(time, vehicles, duration);
				
			}

			return event;
		}
	}
}
