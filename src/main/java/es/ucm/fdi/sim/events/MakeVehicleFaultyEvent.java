package es.ucm.fdi.sim.events;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.exceptions.InvalidEventException;

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
	 * @param id Identifiers of the <code>Vehicles</code> to break.
	 * @param duration Duration of the fault.
	 */
	public MakeVehicleFaultyEvent(int t, List<String> vehicles, int duration){
		super(t);
		this.vehicles = new ArrayList(vehicles);
		this.duration = duration;
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
		 * @param section The section from which to build the <code>Event</code>.
		 * @return A <code>MakeVehicleFaultyEvent</code>, or <code>null</code> if there were parsing errors.
		 */
		public MakeVehicleFaultyEvent build(IniSection section){
			MakeVehicleFaultyEvent event;
			int time, duration;
			List<String> vehicles;
			String timeStr, vehiclesIdStr, durationStr;

			if(TAG.equals(section.getTag())) {
				try	{
					//Check existence of all necessary keys and read the attributes
					//This ignores other unnecessary keys
					timeStr = section.getValue("time");
					vehiclesIdStr = section.getValue("vehicles");
					durationStr = section.getValue("duration");

					//Parse the attributes
				        time = Integer.parseInt(timeStr);
					duration = Integer.parseInt(durationStr);

					vehicles = Arrays.asList(vehiclesIdStr.split(","));
					vehicles.forEach((id) -> checkIDValidity(id));
									       							    
				} catch(Exception e) {
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}

				event = new MakeVehicleFaultyEvent(time, vehicles, duration);
				
			} else {
				event = null;
			}

			return event;
		}
	}
}
