package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;

/**
 *	Controla el comportamiento general de los vehículos en la simulación.
 */
public class Vehicle extends SimObject{
	
	private static String type = "vehicle";
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
	        nextJunction = 0;
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
		}
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
	}
	
	public void setCurrentVel(int v){
		if(v <= maxVel){
			currentVel = v;	
		}else{
			currentVel = maxVel;
		}
	}
	
	public void setMaxVel(int v){
		maxVel = v;
	}
	
	public int getPosition(){
		return position;
	}

	public Road getRoad() {
		return currentRoad;
	}
	
	public String generateReport(int t){
		//REESCRIBIR TODOS LOS REPORTS CON INISECTION
		//Genera el informe en formato INI
		StringBuilder report = new StringBuilder(100);
		//STRING BUILDER
		report.append("[");
		report.append(type);
		report.append("_report]\nid = ");
		report.append(getID());
		report.append("\ntime = ");
		report.append(t);
		report.append("\nspeed = ");
		report.append(currentVel);
		report.append("\nkilometrage = ");
		report.append(kilometrage);
		report.append("\nfaulty = ");
		
		if(brokenTime > 0){
			report.append("1\nlocation = ");
		}
		else{
			report.append("0\nlocation = ");
		}
		
		if(arrived){
			report.append("arrived");
		}else{
			report.append("(");
			report.append(currentRoad.getID());
			report.append(",");
			report.append(position);
			report.append(")");
		}
		
		return report.toString();
	}
}
