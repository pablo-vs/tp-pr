package es.ucm.fdi.sim.events;

import es.ucm.fdi.ini.IniSection;

public class NewMakeVehicleFaultyEvent extends Event {

	public NewMakeVehicleFaultyEvent(int t, String s){
		super(t,s);
	}
	
	class NewMakeVehicleFaultyEventBuilder extends EventBuilder{
		
		public NewMakeVehicleFaultyEvent build(IniSection sec){
			NewMakeVehicleFaultyEvent event;
			
			
			
			event = NewMakeVehicleFaultyEvent.this;
			return event;
		}
	}
}
