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
	
	public NewVehicleEvent(int t, String s){
		super(t,s);
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
	
	class NewVehicleEventBuilder extends EventBuilder {

		public static final String TAG = "new_vehicle";
		
		public NewVehicleEvent build(IniSection section) throws InvalidEventException {
			NewVehicleEvent event;
			String timeStr, vehicleIdStr, maxSpeedStr, itineraryStr;

			if(TAG.equals(section.getTag())) {
				try	{
					//Check existence of all necessary keys and read the attributes
					//This ignores other unnecessary keys
					timeStr = section.getValue("time");
					vehicleIdStr = section.getValue("id");
					maxSpeedStr = section.getValue("max_speed");
					itineraryStr = section.getValue("itinerary");

					//Parse the attributes
					time = Integer.parseInt(timeStr);
					//REQUIRES FIX - CONSTRUCTOR ?
					maxSpeed = Integer.parseInt(maxSpeedStr);

					checkIDValidity(vehicleIdStr);
					vehicleID = vehicleIdStr;

					/*THIS DOES NOT CHECK THAT THE JUNCTIONS EXIST.
					SHOULD THAT BE CHECKED WHEN THE	VEHICLE IS CREATED?*/
					itinerary = new ArrayList<>();
					for(String junctionID : itineraryStr.split(",")) {
						checkIDValidity(junctionID); 
						//WE MIGHT NEED TO CHECK WHETHER TWO CONSECUTIVE JUNCTIONS ARE JOINED
						itinerary.add(junctionID);
					}
				} catch(Exception e) {
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}
				event = NewVehicleEvent.this;

			} else {
				event = null;
			}

			return event;
		}
	}
}