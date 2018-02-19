package es.ucm.fdi.sim.objects;

import java.util.List;
import java.lang.String;
import java.util.ArrayList;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

public class Road {
	
	private static String type = "road_report";
	private List<Vehicle> vehicleList; //La localizacion 0 ocupa la ultima posicion.
	private String id;
	private int length, maxVel;
	private Junction ini,end;
	
	public Road(String id, int l, int maxV, Junction ini, Junction end){
		this.id = id;
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
		
		while(i < vehicleList.size() && !end){
			if(v.getPosition() < vehicleList.get(i).getPosition()){
				++i;
			}else{
				vehicleList.add(i,v);
				end = true;
			}
		}
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
	public void advance(){
		//Avanzar + Calcular velocidadBase + reajustar la velocidad + hacer avanzar al Vehicle 
		for(Vehicle v : vehicleList){
			//VELOCIDADBASE
			v.move();
		}
	}
	
	public String getID(){
		return id;
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
	
	public String generateReport(){
		boolean first;
		StringBuilder sb = new StringBuilder(vehicleList.size()*10);

		first = true;
		sb.append("[");
		sb.append(type);
		sb.append("]\nid = ");
		sb.append(id);
		sb.append("\nstate = ");

		for(Vehicle v : vehicleList){
			if(!first){
				sb.append(",");
			}else{
				first = true;
			}
			
			sb.append("(");
			sb.append(v.getID());
			sb.append(",");
			sb.append(v.getPosition());
			sb.append(")");
		}
		
		return sb.toString();
	}
}