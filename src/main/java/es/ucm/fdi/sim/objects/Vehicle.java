package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;

/**
 *	Controls the general behavior of vehicles in the simulation 
 */
public class Vehicle extends SimObject{
	
	private static String type = "vehicle_report";
	private Road currentRoad;
	private List<Junction> itinerary;
	private int maxVel, currentVel, position, brokenTime, kilometrage, nextJunction;
	private boolean arrived, inQueue;
	
	public Vehicle(String id, int maxVel, List<Junction> itinerary){
		super(id);
		this.itinerary = new ArrayList<Junction>(itinerary);
		this.maxVel = maxVel;
		currentVel = 0;
		position = 0;
		brokenTime = 0;
		kilometrage = 0;
	    nextJunction = 0; //Points to the position in the itinerary
		arrived = false;
		inQueue = false;
		moveToNextRoad();
	}
	
	public void move(){
		
		if(brokenTime == 0 && !inQueue){						
			if(position + currentVel >= currentRoad.getLength()) {
				kilometrage += currentRoad.getLength() - position;
				position = currentRoad.getLength();
				currentVel = 0;

				if(nextJunction == itinerary.size()-1) {
					//DESTROY VEHICLE??
					arrived = true;
				} else {
					currentRoad.getEnd().vehicleIn(this);
					inQueue = true;
				}
				
			} else {
				position += currentVel;
				kilometrage += currentVel;
			}
			
		}else if(brokenTime > 0){
			brokenTime--;
		} //WHAT DO WE DO WHEN IN QUEUE?
	}
	
	public void moveToNextRoad(){
		position = 0;
		inQueue = false;

		Junction currentJunction = itinerary.get(nextJunction);
		nextJunction++;
		currentRoad = currentJunction.getRoadToJunction(itinerary.get(nextJunction));
		currentRoad.vehicleIn(this);
		
	}
	
	public void setBrokenTime(int t){
		brokenTime += t;
		currentVel = 0;
	}
	
	public void setCurrentVel(int v){
		if(v <= maxVel){
			currentVel = v;	
		}else{
			currentVel = maxVel;
		}
	}
	
	//¿?
	public void setMaxVel(int v){
		maxVel = v;
	}
	
	public int getPosition(){
		return position;
	}

	public Road getRoad() {
		return currentRoad;
	}
	
	public boolean isFaulty() {
		boolean faulty = false;
		if(brokenTime > 0){
			faulty = true;
		}
		return faulty;
	}
	
	/**
	 * Generates an INI formatted report
	 */
	public IniSection generateReport(int t){
		IniSection sec = new IniSection(type);
		
		sec.setValue("id", getID());
		sec.setValue("time", t);
		sec.setValue("speed", currentVel);
		sec.setValue("kilometrage", kilometrage);
		if(brokenTime > 0){
			sec.setValue("faulty", "1");
		}else{
			sec.setValue("faulty", "0");
		}
		if(arrived){
			sec.setValue("location", "arrived");
		}else{ //NEEDS SB OPTIMIZATION
			sec.setValue("location", "(" + currentRoad.getID() + "," + position + ")");
		}

		return sec;
	}
}
