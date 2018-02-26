package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.sim.events.*;
import es.ucm.fdi.ini.IniSection;

/**
 * This class is capable of reading events from a file
 */
public class EventParser {
	//BETTER ARRAY
	public static List<Event.EventBuilder> possibleEvents = new ArrayList() {{
		new NewJunctionEvent.Builder();
		new NewVehicleEvent.Builder();
		new NewMakeVehicleFaultyEvent.Builder();
	}};
	
	
	//PASAR A CONTROLLER
	public static Event parseEvent(String filename){
		Event aux = null;
		IniSection sec = new IniSection("Temporary");
		//READ
		
		for(Event.EventBuilder b : possibleEvents){
			if(b.build(sec) != null){
				aux = b.build(sec);
			}
		}
		
		return aux;
	}
}
