package es.ucm.fdi.sim.events;

import es.ucm.fdi.ini.IniSection;

public class NewJunctionEvent extends Event {
	private String junctionID;
	
	public NewJunctionEvent(){}
	public NewJunctionEvent(int t, String s){
		super(t,s);
	}
	
	public String getJunctionID(){
		return junctionID;
	}
	
	public static class Builder extends EventBuilder{
		
		public NewJunctionEvent build(IniSection sec){
			NewJunctionEvent event;
			
			//CAREFUL, CHECK TIME IS VALID 
			
			//event = NewJunctionEvent.this; :)
			event = null;
			return event;
		}
	}
}
