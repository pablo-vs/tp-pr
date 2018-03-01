package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.ini.IniSection;

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
	
	//GETTER NEEDED?
	public String getRoadID(){
		return roadID;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getMaxVel(){
		return maxVel;
	}
	
	//PENDING MODS - HOW DO WE FIND THE JUNCTION?
	public static class Builder extends EventBuilder{
		public static final String TAG = "new_road";
		
		public NewRoadEvent build(IniSection ini){
			NewRoadEvent event;
			String timeStr, idStr, iniStr, endStr, maxVelStr, lengthStr;
			
			//CHECK THE JUNCTION LIST?
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
			return event;
		}
	}
}
