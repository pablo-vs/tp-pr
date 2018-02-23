package es.ucm.fdi.sim.objects;

import java.util.List;
import java.lang.String;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

public class Road extends SimObject{
	
	private static String type = "road";
	private List<Vehicle> vehicleList; //La localizacion 0 ocupa la ultima posicion.
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
		boolean end = false;
		int i = 0;

		/*TEMPORARY, FOR TESTING
		while(i < vehicleList.size() && !end){
			if(v.getPosition() < vehicleList.get(i).getPosition()){
				++i;
				}else{*/
				vehicleList.add(i,v);/*
				end = true;
			}
			}*/
	}
	
	//Invoked by Vehicle
	public void vehicleOut(Vehicle v){
		int i = 0; //SIEMPRE EL ÚLTIMO NO HACE FALTA BUSCAR
		while(!vehicleList.get(i).equals(v)){
			++i;
		}	
		vehicleList.remove(i);
	}
	
	//Invocado por el simulador
	public void move(){
		int baseSpeed = Math.min(maxVel, maxVel/(Math.max(vehicleList.size(), 1)) + 1), reductionFactor = 1;
		for(Vehicle v : vehicleList){
			//CALCULATE REDUCTION FACTOR!!!!!
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
	
	public String generateReport(int t){
		boolean first;
		StringBuilder report = new StringBuilder(vehicleList.size()*60);

		first = true;
		report.append("[");
		report.append(type);
		report.append("_report]\nid = ");
		report.append(getID());
		report.append("\nstate = ");

		for(Vehicle v : vehicleList){
			if(!first){
				report.append(",");
			}else{
				first = false;
			}
			
			report.append("(");
			report.append(v.getID());
			report.append(",");
			report.append(v.getPosition());
			report.append(")");
		}
		
		return report.toString();
	}
}
