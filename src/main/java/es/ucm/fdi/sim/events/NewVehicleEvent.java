package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
*	Represents the New Vehicle event.
*/
public class NewVehicleEvent extends Event {
	private int maxSpeed;
	private String vehicleID;
	private List<String> itinerary; //BETTER TO USE LIST AND INSTANTIATE ARRAYASLIST LATER?
	
	public NewVehicleEvent(){}
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
	public void execute(RoadMap r) {	
		List<Junction> it = new ArrayList<Junction>();
		
		try{
			for(String j : itinerary){
				it.add(r.getJunction(j));
			}
			r.addVehicle(new Vehicle(vehicleID, maxSpeed, it));
			
		} catch(ObjectNotFoundException e){
			//DO SOMETHING
		}
	}
	
	public static class Builder extends EventBuilder {
		public static final String TAG = "new_vehicle";
		
		public NewVehicleEvent build(IniSection ini) throws InvalidEventException {
			NewVehicleEvent event;
			String timeStr, vehicleIDStr, maxSpeedStr, itineraryStr;
			List<String> itinerary;

			event = null;
			if(TAG.equals(ini.getTag())) {
				try	{ //WHY TRY CATCH?
					//Check existence of all necessary keys and read the attributes
					//This ignores other unnecessary keys
					itinerary = new ArrayList<String>();
					timeStr = ini.getValue("time");
					vehicleIDStr = ini.getValue("id");
					maxSpeedStr = ini.getValue("max_speed");
					itineraryStr = ini.getValue("itinerary");

					//Parse the attributes
					checkIDValidity(vehicleIDStr);

					/*THIS DOES NOT CHECK THAT THE JUNCTIONS EXIST.
					SHOULD THAT BE CHECKED WHEN THE	VEHICLE IS CREATED?*/
					for(String junctionID : itineraryStr.split(",")) {
						checkIDValidity(junctionID); 
						//WE MIGHT NEED TO CHECK WHETHER TWO CONSECUTIVE JUNCTIONS ARE JOINED
						itinerary.add(junctionID);
					}
					
					event = new NewVehicleEvent(Integer.parseInt(timeStr), Integer.parseInt(maxSpeedStr),
							maxSpeedStr, itinerary);
				} catch(Exception e) {
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}
			}

			return event;
		}
	}
}