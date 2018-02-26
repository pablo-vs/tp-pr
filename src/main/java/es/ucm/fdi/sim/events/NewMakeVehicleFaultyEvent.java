package es.ucm.fdi.sim.events;

import es.ucm.fdi.ini.IniSection;

public class NewMakeVehicleFaultyEvent extends Event {

	public NewMakeVehicleFaultyEvent(){}
	public NewMakeVehicleFaultyEvent(int t, String s){
		super(t,s);
	}
	
	public static class Builder extends EventBuilder{
		
		public NewMakeVehicleFaultyEvent build(IniSection sec){
			NewMakeVehicleFaultyEvent event;
			
			
			
			//event = NewMakeVehicleFaultyEvent.this; ;)
			event = null;
			return event;
		}
	}
}
