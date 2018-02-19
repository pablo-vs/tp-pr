package es.ucm.fdi.sim.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.exceptions.InvalidEventException;
import java.util.ArrayList;

/**
*	Represents the New Vehicle event.
*/
public class NewVehicleEvent extends Event {
	private int time;
	private String vehicleId;
	private int maxSpeed;
	private ArrayList<String> itinerary;

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
					maxSpeed = Integer.parseInt(maxSpeedStr);

					checkIdValidity(vehicleIdStr);
					vehicleId = vehicleIdStr;

					/*THIS DOES NOT CHECK THAT THE JUNCTIONS EXIST.
					SHOULD THAT BE CHECKED WHEN THE	VEHICLE IS CREATED?*/
					itinerary = new ArrayList<>();
					for(String junctionId : itineraryStr.split(",")) {
						checkIdValidity(junctionId);
						itinerary.add(junctionId);
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