package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;

/**
	Controla el comportamiento general de los vehículos en la simulación.
*/
public class Vehicle extends SimObject{
	
	private static String type = "vehicle";
	private Road currentRoad;
	private List<Junction> itinerary; //Carreteras??
	private int maxVel, currentVel, position, brokenTime;
	private boolean arrived, inQueue;
	
	public Vehicle(String id, List<Junction> itinerary){
		super(id);
		this.itinerary = itinerary;
		//currentRoad = ;
		maxVel = 0;
		currentVel = 0;
		position = 0;
		brokenTime = 0;
		arrived = false;
		inQueue = false;
		itinerary = new ArrayList<Junction>();
	}
	
	public void move(){
		
		if(brokenTime == 0 && !inQueue){
			position += currentVel;
			
			if(position >= currentRoad.getLength()){ //???? INCORRECTO
				position = currentRoad.getLength();
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
	
	public String generateReport(int t){
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
		report.append("\nkilometraje = ");
		report.append(position);
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
		}
		
		return report.toString();
	}
}
