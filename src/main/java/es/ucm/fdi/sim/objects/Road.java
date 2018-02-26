package es.ucm.fdi.sim.objects;

import java.util.List;
import java.lang.String;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

public class Road extends SimObject{
	
	private static String type = "road_report";
	private List<Vehicle> vehicleList; //0 position is last. Keep entry order right to left
	//WE NEED TO USE A QUEUE
	private int length, maxVel;
	private Junction ini,end;
	
	public Road(String id, int l, int maxV, Junction ini, Junction end){
		super(id);
		length = l;
		maxVel = maxV;
		this.ini = ini;
		this.end = end;
		vehicleList = new ArrayList<Vehicle>();
	}
	
	//Invoked by Vehicle - Ordered insertion
	public void vehicleIn(Vehicle v){	
		//Last one in list
		vehicleList.add(v);
	}
	
	//Invoked by Vehicle. Since it is called by a vehicle, it is a precondition that a 
	//vehicle is in the list, given the program is correct
	public void vehicleOut(Vehicle v){
		vehicleList.remove(0);
	}
	
	//Invoked by Simulator
	public void move(){
		int baseSpeed = Math.min(maxVel, maxVel/(Math.max(vehicleList.size(), 1)) + 1),
				reductionFactor = 1;
		
		for(Vehicle v : vehicleList){
			//CALCULATE REDUCTION FACTOR!!!!!
			if(v.isFaulty()){
				reductionFactor = 2;
			}
			v.setCurrentVel(baseSpeed/reductionFactor);
			v.move();
		}
	}
	
	public int getLength(){
		return length;
	}
	
	public int getMaxVel(){
		return maxVel;
	}
	
	public Junction getIni(){
		return ini;
	}
	
	public Junction getEnd(){
		return end;
	}
	
	public IniSection generateReport(int t){
		boolean first = true;
		IniSection sec = new IniSection(type);
		StringBuilder aux = new StringBuilder(vehicleList.size()*60);
		
		sec.setValue("id", getID());
		sec.setValue("time", t);
		for(Vehicle v : vehicleList){
			if(!first){
				aux.append(",");
			}
			else{
				first = false;
			}
			aux.append("(");
			aux.append(v.getID());
			aux.append(",");
			aux.append(v.getPosition());
			aux.append(")");
		}
		sec.setValue("state", aux.toString());
		
		return sec;
	}
}
