package es.ucm.fdi.sim.events;

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
		
		public NewJunctionEvent build(IniSection sec){
			NewJunctionEvent event;
			String tStr, idStr;
			
			//CAREFUL, CHECK TIME IS VALID
			tStr = sec.getValue("time");
			idStr = sec.getValue("id");
			checkIDValidity(idStr);
			
			//event = NewJunctionEvent.this; :)
			event = new NewJunctionEvent(Integer.parseInt(tStr), idStr); //:)
			return event;
		}
	}
}
