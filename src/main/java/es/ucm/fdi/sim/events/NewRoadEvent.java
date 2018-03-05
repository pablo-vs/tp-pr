package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;

public class NewRoadEvent extends Event {
	private String roadID, ini, end;
	private int length, maxVel;
	
	public NewRoadEvent(){}
	public NewRoadEvent(int t, String id, String ini, String end, int v, int l){
		super(t);
		roadID = id;
		this.ini = ini;
		this.end = end;
		maxVel = v;
		length = l;
	}
	
	public void execute(RoadMap r){
		Junction iniJ, endJ;
		
		try{
			iniJ = r.getJunction(ini);
			endJ = r.getJunction(end);
			r.addRoad(new Road(roadID, length, maxVel, iniJ, endJ));
			
		} catch (ObjectNotFoundException e){
			//DO SOMETHING
		}
	}
	
	//PENDING MODS - HOW DO WE FIND THE JUNCTION?
	public static class Builder extends EventBuilder{
		public static final String TAG = "new_road";
		
		public NewRoadEvent build(IniSection ini){
			NewRoadEvent event;
			String timeStr, idStr, iniStr, endStr, maxVelStr, lengthStr;
			
			event = null;
			if(TAG.equals(ini.getTag()))
			{
				try{
					timeStr = ini.getValue("time");
					idStr = ini.getValue("id");
					iniStr = ini.getValue("src");
					endStr = ini.getValue("dest");
					maxVelStr = ini.getValue("max_speed");
					lengthStr = ini.getValue("length");
					
					checkIDValidity(idStr);
					checkIDValidity(iniStr);
					checkIDValidity(endStr);
					
					//event = NewRoadEvent.this; ^^
					event = new NewRoadEvent(Integer.parseInt(timeStr), idStr, iniStr,
							endStr, Integer.parseInt(maxVelStr), Integer.parseInt(lengthStr));
				} catch (Exception e){
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}	
			}

			return event;
		}
	}
}
