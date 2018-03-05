package es.ucm.fdi.sim.events;

import es.ucm.fdi.exceptions.InvalidEventException;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;

public class NewJunctionEvent extends Event {
	private String junctionID;
	
	public NewJunctionEvent(){}
	public NewJunctionEvent(int t, String id){
		super(t);
		junctionID = id;
	}
	
	public void execute(RoadMap r){
		r.addJunction(new Junction(junctionID));
	}
	
	public static class Builder extends EventBuilder{
		public static final String TAG = "new_junction";
		
		public NewJunctionEvent build(IniSection ini){
			NewJunctionEvent event;
			String tStr, idStr;
			
			event = null;
			if(TAG.equals(ini.getTag()))
			{
				try{
					//CAREFUL, CHECK TIME IS VALID
					tStr = ini.getValue("time");
					idStr = ini.getValue("id");
					checkIDValidity(idStr);
					
					event = new NewJunctionEvent(Integer.parseInt(tStr), idStr);	
				} catch(Exception e){
					throw new InvalidEventException("Error while parsing event:\n" + e.getMessage());
				}	
			}
			
			return event;
		}
	}
}
