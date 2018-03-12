package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.InvalidIDException;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
 * Represents the New Vehicle event.
 */
public class NewVehicleEvent extends Event {
    private int maxSpeed;
    private String vehicleID;
    private List<String> itinerary; //BETTER TO USE LIST AND INSTANTIATE ARRAYASLIST LATER?

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
    public NewVehicleEvent(int t, int v, String id, List<String> it){
		super(t);
		maxSpeed = v;
		vehicleID = id;
		itinerary = it;
    }
	
    /**
     * Instantiates a new vehicle, given the parameters are valid.
     * 
     * @param r The <code>RoadMap</code> of the current simulation.
     */
    @Override
    public void execute(RoadMap r) throws InvalidEventException {	
		List<Junction> it = new ArrayList<Junction>();
			
		try{
		    it.add(r.getJunction(itinerary.get(0)));
		    for(int i = 1; i < itinerary.size(); ++i){
			Junction j = r.getJunction(itinerary.get(i));
			if(j == null) {
			    throw new ObjectNotFoundException("No junction with id: " + itinerary.get(i));
			}
			if(it.get(i-1).getRoadToJunction(j) != null) {
			    it.add(j);
			}
		    }
		    r.addVehicle(new Vehicle(vehicleID, maxSpeed, it));
				
		} catch(ObjectNotFoundException e){
		    throw new InvalidEventException("Error while creating new vehicle.\n" + e.getMessage(), e);
		}
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
		public NewVehicleEvent build(IniSection ini) throws InvalidEventException {
		    NewVehicleEvent event;
		    String timeStr, vehicleIDStr, maxSpeedStr, itineraryStr;
		    List<String> itinerary;
	
		    event = null;
		    if(TAG.equals(ini.getTag())) {
			try	{
			    //Check existence of all necessary keys and read the attributes
			    //This ignores other unnecessary keys
			    itinerary = new ArrayList<String>();
			    timeStr = ini.getValue("time");
			    vehicleIDStr = ini.getValue("id");
			    maxSpeedStr = ini.getValue("max_speed");
			    itineraryStr = ini.getValue("itinerary");
	
			    //Parse the attributes
			    checkIDValidity(vehicleIDStr);
	
			    for(String junctionID : itineraryStr.split(",")) {
				checkIDValidity(junctionID);
				itinerary.add(junctionID);
			    }
						
			    event = new NewVehicleEvent(Integer.parseInt(timeStr),
							Integer.parseInt(maxSpeedStr),
						        vehicleIDStr, itinerary);
			} catch(Exception e) {
			    throw new InvalidEventException("Error while parsing event:\n" + e.getMessage(), e);
			}
		    }
	
		    return event;
		}
    }
}
