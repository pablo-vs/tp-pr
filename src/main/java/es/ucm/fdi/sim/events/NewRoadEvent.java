package es.ucm.fdi.sim.events;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.ini.IniSection;

public class NewRoadEvent extends Event {
	private String roadID;
	private int length, maxVel;
	private Junction ini, end;
	private List<Vehicle> vehicleList;
	
	public NewRoadEvent(int t, String s){
		super(t,s);
	}
	
	public String getRoadID(){
		return roadID;
	}
	
	public int getLength(){
		return length;
	}
	
	public int getMaxVel(){
		return maxVel;
	}
	
	public Junction getInitialJunction(){
		return ini;
	}
	
	public Junction getEndingJunction(){
		return end;
	}
	
	public List<Vehicle> getVehicleList(){ //Possible to return a ref?
		return vehicleList;
	}
	
	class NewRoadEventBuilder extends EventBuilder{
		public static final String TAG = "new_road";
		
		public NewRoadEvent build(IniSection ini){
			NewRoadEvent event;
			String lengthStr, maxVelStr; 
			
			//CHECK THE JUNCTION LIST?
			vehicleList = new ArrayList<Vehicle>();
			
			event = NewRoadEvent.this;
			return event;
		}
	}
}
