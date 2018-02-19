package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;

public class Junction extends SimObject{
	
	private static String type = "junction";
	private int time;
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

	public Junction(String id, List<Road> roads){
		super(id);
		queues = new ArrayList<JunctionQueue>(roads.size());
		for(int i = 0; i < roads.size(); ++i) {
			queues.set(i, new JunctionQueue(roads.get(i)));
		}
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public void vehicleIn(Vehicle v){
		//FIND ROAD IN THE QUEUES? CHANGE LIST TO MAP<ROAD, JUNCTIONQUEUE>??
		//queues[i].add(v);
	}
	
	//Invocado por Vehicles -> Ordenados por orden de llegada
	public void vehicleOut(Vehicle v){
		//queue[i].remove(queue.size()-1); //REVISAR - pop_back()
	}
	
	public void advance(){
		
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
