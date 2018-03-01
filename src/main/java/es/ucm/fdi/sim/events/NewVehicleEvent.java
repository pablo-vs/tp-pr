package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.exceptions.InvalidEventException;

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
	
	public String getVehicleID(){
		return vehicleID;
	}
	
	public int getMaxSpeed(){
		return maxSpeed;
	}
	
	public List<String> getItinerary(){ //IS IT POSSIBLE TO GIVE A REF?
		return itinerary;
	}
	
	public static class Builder extends EventBuilder {

		public static final String TAG = "new_vehicle";
		
		public NewVehicleEvent build(IniSection section) throws InvalidEventException {
			NewVehicleEvent event;
			String timeStr, vehicleIDStr, maxSpeedStr, itineraryStr;
			List<String> itinerary;

			event = null;
			if(TAG.equals(section.getTag())) {
				try	{ //WHY TRY CATCH?
					//Check existence of all necessary keys and read the attributes
					//This ignores other unnecessary keys
					itinerary = new ArrayList<String>();
					timeStr = section.getValue("time");
					vehicleIDStr = section.getValue("id");
					maxSpeedStr = section.getValue("max_speed");
					itineraryStr = section.getValue("itinerary");

					//Parse the attributes
					checkIDValidity(vehicleIDStr);

					/*THIS DOES NOT CHECK THAT THE JUNCTIONS EXIST.
					SHOULD THAT BE CHECKED WHEN THE	VEHICLE IS CREATED?*/
					for(String junctionID : itineraryStr.split(",")) {
						checkIDValidity(junctionID); 
						//WE MIGHT NEED TO CHECK WHETHER TWO CONSECUTIVE JUNCTIONS ARE JOINED
						itinerary.add(junctionID);
					}
				} catch(Exception e) {
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}
				event = new NewVehicleEvent(Integer.parseInt(timeStr), Integer.parseInt(maxSpeedStr),
						maxSpeedStr, itinerary);

			}

			return event;
		}
	}
}