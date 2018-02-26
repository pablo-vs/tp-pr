package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.ArrayDeque;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.exceptions.UnreachableJunctionException;

public class Junction extends SimObject{
	
	private static String type = "junction_report";
	private int time, currentOpenQueue;
	private List<JunctionQueue> incomingRoads;

	private class JunctionQueue extends ArrayDeque<Vehicle> {
		private Road road;
		private boolean trafficLight;

		JunctionQueue(Road road) {
			super();
			this.road = road;
			trafficLight = false;
		}

		public Road getRoad() {
			return road;
		}

		public boolean getTrafficLight() {
			return trafficLight;
		}

		public void setTrafficLight(boolean b) {
			trafficLight = b;
		}
	}
	
	
	public Junction(String id) {
		super(id);
		incomingRoads = new ArrayList<JunctionQueue>();
		currentOpenQueue = -1;
	}

	public Junction(String id, List<Road> roads){
		super(id);
		incomingRoads = new ArrayList<JunctionQueue>(roads.size());
		currentOpenQueue = 0;
		for(int i = 0; i < roads.size(); ++i) {
			incomingRoads.set(i, new JunctionQueue(roads.get(i)));
		}
	}
	
	public int getTime(){
		return time;
	}

	private void updateTrafficLights() {
		incomingRoads.get(currentOpenQueue).setTrafficLight(false);
		currentOpenQueue = (currentOpenQueue + 1) % incomingRoads.size();
		incomingRoads.get(currentOpenQueue).setTrafficLight(true);
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	//WEIRD BEHAVIOR - MIGHT NEED REFS IN ROAD QUEUE
	public void vehicleIn(Vehicle v){
		boolean found = false;
		Iterator<JunctionQueue> it = incomingRoads.iterator();
		JunctionQueue queue = null;
		
		while(!found && it.hasNext()) {
			queue = it.next();
			
			//System.err.print(queue.getRoad().getID() + " vs " + v.getRoad().getID()+ " - ");
			//System.err.print(queue.getRoad() + " vs " + v.getRoad() + " - ");
			if(queue.getRoad().equals(v.getRoad())) {
				queue.add(v);
				found = true;
			}
		}
	}
	
	public Vehicle vehicleOut(){
		return incomingRoads.get(currentOpenQueue).removeFirst();
	}

	public Road getRoadToJunction(Junction j) throws UnreachableJunctionException{
		Road result = null;
		boolean found = false;
		Iterator<JunctionQueue> it = incomingRoads.iterator();
		JunctionQueue queue = null;
		while(!found && it.hasNext()) {
			queue = it.next();
			if(queue.getRoad().getEnd().equals(j)) {
				result = queue.getRoad();
				found = true;
			}
		}

		if(!found) {
			throw new UnreachableJunctionException("Could not get from " + getID() + " to " + j.getID());
		}

		return result;
	}
	
	public void move(){
		Vehicle out = vehicleOut();
		out.moveToNextRoad();
		updateTrafficLights();
	}
	
	public void addRoad(Road r) {
		incomingRoads.add(new JunctionQueue(r));
		if(currentOpenQueue == -1) {
			currentOpenQueue = 0;
			incomingRoads.get(0).setTrafficLight(true);
		}
	}
	
	//
	public IniSection generateReport(int t){
		IniSection sec = new IniSection(type);
		boolean first = true, firstVehicle;
		StringBuilder aux = new StringBuilder();
		
		sec.setValue("id", getID());
		sec.setValue("time", t);
		
		for(JunctionQueue queue : incomingRoads){
			if(!first){
				aux.append(",");
			} else {
				first = false;
			}
			aux.append("(");
			aux.append(queue.getRoad().getID());
			if(queue.getTrafficLight()){
				aux.append(",green,[");
			} else {
				aux.append(",red,[");
			}
			
			firstVehicle = true;
			for(Vehicle v : queue){
				if(!firstVehicle){
					aux.append(",");
				} else {
					firstVehicle = false;
				}
				aux.append(v.getID());
			}
			aux.append("]");
			aux.append(")");
		}
		sec.setValue("incomingRoads", aux);

		return sec;
	}
}
