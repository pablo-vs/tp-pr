package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.ArrayDeque;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.exceptions.UnreachableJunctionException;

public class Junction extends SimObject{
	
	private static String type = "junction";
	private int time, currentOpenQueue;
	private List<JunctionQueue> queues;

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

	private void updateTrafficLights() {
		queues.get(currentOpenQueue).setTrafficLight(false);
		currentOpenQueue = (currentOpenQueue + 1) % queues.size();
		queues.get(currentOpenQueue).setTrafficLight(true);
	}
	
	public Junction(String id) {
		super(id);
		queues = new ArrayList<JunctionQueue>();
		currentOpenQueue = -1;
	}

	public Junction(String id, List<Road> roads){
		super(id);
		queues = new ArrayList<JunctionQueue>(roads.size());
		currentOpenQueue = 0;
		for(int i = 0; i < roads.size(); ++i) {
			queues.set(i, new JunctionQueue(roads.get(i)));
		}
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public void vehicleIn(Vehicle v){
	        boolean found = false;
		Iterator<JunctionQueue> it = queues.iterator();
		JunctionQueue queue = null;
		while(!found && it.hasNext()) {
			queue = it.next();;
			if(queue.getRoad().equals(v.getRoad())) {
				queue.add(v);
				found = true;
			}
		}
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public Vehicle vehicleOut(){
		return queues.get(currentOpenQueue).removeFirst();
	}

	public Road getRoadToJunction(Junction j) throws UnreachableJunctionException{
		
		Road result = null;
		boolean found = false;
		Iterator<JunctionQueue> it = queues.iterator();
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
		queues.add(new JunctionQueue(r));
		if(currentOpenQueue == -1) {
			currentOpenQueue = 0;
			queues.get(0).setTrafficLight(true);
		}
	}
	
	public String generateReport(int t){
		StringBuilder report = new StringBuilder();
		boolean first = false, firstVehicle;

		report.append("[");
		report.append("_report]\nid = ");
		report.append(getID());
		report.append("\ntime = ");
		report.append(time);
		report.append("\nqueues = ");

		for(JunctionQueue queue : queues) {
			if(!first) {
				report.append(",");
			} else {
				first = false;
			}

			report.append("(");
			report.append(queue.getRoad().getID());
			if(queue.getTrafficLight()) {
				report.append(",green,[");	
			}	else {
				report.append(",red,[");
			}

			firstVehicle = true;
			for(Vehicle v : queue) {
				if(!firstVehicle) {
					report.append(",");
				} else {
					firstVehicle = false;
				}
				report.append(v.getID());
			}
			report.append("]");
		}

		return report.toString();
	}
}
