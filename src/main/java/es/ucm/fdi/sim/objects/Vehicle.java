package es.ucm.fdi.sim.objects;

import java.util.List; //:(

import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;

/**
	Controla el comportamiento general de los vehículos en la simulación.
*/
public class Vehicle extends SimObject{
	
	private static String type = "vehicle_report";
	private String id;
	private Road currentRoad;
	private List<Junction> itinerary; //Carreteras??
	private int maxVel, currentVel, position, brokenTime;
	private boolean arrived, inQueue;
	
	public Vehicle(String id, List<Junction> itinerary){
		super(id);
		this.id = id;
		this.itinerary = itinerary;
		//currentRoad = ;
		maxVel = 0;
		currentVel = 0;
		position = 0;
		brokenTime = 0;
		arrived = false;
		inQueue = false;
	}
	
	public void move(){
		
		if(brokenTime == 0 && !inQueue){
			position += currentVel;
			
			if(position >= currentRoad.getLongitud()){ //???? INCORRECTO
				position = currentRoad.getLongitud();
				currentRoad.getEnd().vehicleIn(this);
				currentRoad.vehicleOut(this);
				currentVel = 0;
				inQueue = true;
			}
			
		}else if(brokenTime > 0){
			brokenTime--;
		}
	}
	
	public void moveToNextRoad(){
		position = 0;
		//carreteraActual++;
		inQueue = false;
		
		/*if(carreteraActual == itinerary.size()) { //NOPE
			haLlegado = true
			currentVel = 0;
		}else{
			velMaxima = carreteraActual.getMaxVel();
		}
	*/
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
	
	public String generateReport(int paso){
		//Genera el informe en formato INI
		String report;
		//STRING BUILDER
		report = "[" + type + "]\n";
		report += "id = " + id + "\n";
		report += "time = " + paso + "\n";
		report += "speed = " + currentVel + "\n";
		report += "kilometraje = " + position + "\n";
		report += "faulty = ";
		
		if(brokenTime > 0){
			report += 1;
		}
		else{
			report += 0;
		}
		report += "\n";
		
		report += "location = ";
		if(arrived){
			report += "arrived";
		}else{
			report += "(" + currentRoad.getID() + "," + position;
		}
		
		return report;
	}
}
