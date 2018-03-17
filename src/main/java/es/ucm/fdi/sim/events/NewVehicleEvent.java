package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.exceptions.UnreachableJunctionException;

/**
 * Represents the New Vehicle event.
 */
public class NewVehicleEvent extends Event {
	private int maxSpeed;
	private String vehicleID;
	private List<String> itinerary;

	/**
	 * Empty constructor.
	 */
	public NewVehicleEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param v Maximum speed of the vehicle.
	 * @param id ID of the vehicle.
	 * @param it Itinerary of the vehicle.
	 */
	public NewVehicleEvent(int t, int v, String id, List<String> it) {
		super(t);
		maxSpeed = v;
		vehicleID = id;
		itinerary = it;
	}

	/**
	 * Copy constructor, builds a copy of the given event.
	 *
	 * @param e The NewVehicleEvent to copy.
	 */
	public NewVehicleEvent(NewVehicleEvent e) {
	        this(e.getTime(), e.maxSpeed, e.vehicleID, e.itinerary);
	}

	/**
	 * Instantiates a new vehicle, given the parameters are valid.
	 * 
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r){	
	        r.addVehicle(createVehicle(r));
	}

	/**
	 * Creates a new Vehicle with the event parameters, if possible.
	 *
	 * @return The corresponding Vehicle.
	 */
	public Vehicle createVehicle(RoadMap r) {
		List<Junction> it = new ArrayList<Junction>();
		Vehicle result;

		try{
			it.add(r.getJunction(itinerary.get(0)));
			for(int i = 1; i < itinerary.size(); ++i){
				Junction j = r.getJunction(itinerary.get(i));
				if(j == null) {
					throw new ObjectNotFoundException("No junction with id: "
									  + itinerary.get(i));
				} else if (it.get(i-1).getRoadToJunction(j) == null) {
					throw new UnreachableJunctionException("Cannot get from "
									       + it.get(i-1).getID()
									       + " to " + j.getID());
				} else {
					it.add(j);
				}
			}

			result = new Vehicle(vehicleID, maxSpeed, it);
			
		} catch(ObjectNotFoundException e){
			throw new IllegalArgumentException("Error: could not create Vehicle "
							   + vehicleID + " at time " + getTime()
							   + ".\n" + e.getMessage(), e);
		}
		return result;
	}

    
	@Override
	public boolean equals(Object o){
		boolean isEqual = false;
		
		if(o != null && o instanceof NewVehicleEvent){
			isEqual = (maxSpeed == ((NewVehicleEvent)o).maxSpeed) && 
    				(vehicleID.equals(((NewVehicleEvent)o).vehicleID)) &&
    				(itinerary.size() == ((NewVehicleEvent)o).itinerary.size());

			for(int i = 0; i < itinerary.size() && isEqual; ++i){
				isEqual = (itinerary.get(i).equals(((NewVehicleEvent)o).itinerary.get(i)));
			}
    		
		}
    	
		return isEqual;
	}
    
	/**
	 * Builder for this event.
	 */
	public static class Builder extends EventBuilder {
		public static final String TAG = "new_vehicle";

		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewVehicleEvent build(IniSection ini) {
			NewVehicleEvent event;
		        int time, maxSpeed;
		        String vehicleID;
		        List<String> itinerary;
			
			event = null;
			if(TAG.equals(ini.getTag())) {
				try {
				        
					time = parseTime(ini);
					itinerary = parseIDList(ini, "itinerary");
					if(itinerary.size() < 2) {
						throw new IllegalArgumentException("Error: itinerary must be at least of length 2");
					}
					vehicleID = parseID(ini, "id");
					maxSpeed = parsePositiveInt(ini, "max_speed");					
					event = new NewVehicleEvent(time, maxSpeed, vehicleID, itinerary);
					
				} catch(IllegalArgumentException e) {
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}
			}
			
			return event;
		}
	}
}
