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

/**
 *	Class that models the general behavior for <code>Junctions</code> in the simulation.
 *
 *	@version 26.02.2018
 */
public class Junction extends SimObject{
	
	private static String type = "junction_report";
	private int time, currentOpenQueue;
	private List<JunctionQueue> incomingRoads;
	private List<JunctionQueue> outgoingRoads;

	/**
	 * Private class representing a <code>Road</code> with its respective queue of 
	 * <code>Vehicles</code> and traffic light.
	 */
	private class JunctionQueue extends ArrayDeque<Vehicle> {
		private Road road;
		private boolean trafficLight;

		/**
		 * Constructor.
		 * 
		 * @param road	<code>Road</code> associated to current <code>JunctionQueue</code>.
		 */
		JunctionQueue(Road road) {
			super();
			this.road = road;
			trafficLight = false;
		}

		/**
		 * Getter method for {@link JunctionQueue#road}.
		 * 
		 * @return Current <code>Road</code>.
		 */
		public Road getRoad() {
			return road;
		}

		/**
		 * Getter method for {@link JunctionQueue#trafficLight}.
		 * 
		 * @return State of traffic light.
		 */
		public boolean getTrafficLight() {
			return trafficLight;
		}

		/**
		 * Setter method for {@link JunctionQueue#trafficLight}.
		 * 
		 * @param b	New state of the traffic light.
		 */
		public void setTrafficLight(boolean b) {
			trafficLight = b;
		}
	}
	
	/**
	 * Constructor.
	 * 
	 * @param id	ID of current <code>Junction</code>.
	 */
	public Junction(String id) {
		super(id);
		incomingRoads = new ArrayList<JunctionQueue>();
		outgoingRoads = new ArrayList<JunctionQueue>();
		currentOpenQueue = -1;
	}

	/**
	 * Constructor
	 * 
	 * @param id		ID of current <code>Junction</code>.
	 * @param incoming	List of the incoming <code>Roads</code> of this <code>Junction</code>.
	 * @param outgoing	List of the outgoing <code>Roads</code> of this <code>Junction</code>.
	 */
	public Junction(String id, List<Road> incoming, List<Road> outgoing){
		super(id);
		incomingRoads = new ArrayList<JunctionQueue>(incoming.size());
		currentOpenQueue = 0;
		for(int i = 0; i < incoming.size(); ++i) {
			incomingRoads.set(i, new JunctionQueue(incoming.get(i)));
		}
		
		outgoingRoads = new ArrayList<JunctionQueue>(outgoing.size());
		for(int i = 0; i < outgoing.size(); i++){
			outgoingRoads.set(i, new JunctionQueue(outgoing.get(i)));
		}
	}
	
	/**
	 * Getter method for {@link Junction#time}.
	 * 
	 * @return	Current time.
	 */
	//??????? WHY ????????
	public int getTime(){
		return time;
	}

	/**
	 * Sets the trafficLights to its next state.
	 */
	private void updateTrafficLights() {
		incomingRoads.get(currentOpenQueue).setTrafficLight(false);
		currentOpenQueue = (currentOpenQueue + 1) % incomingRoads.size();
		incomingRoads.get(currentOpenQueue).setTrafficLight(true);
	}
	
	/**
	 * Lets a <code>Vehicle</code> into this <code>Junction</code>.
	 * 
	 * @param v	<code>Vehicle</code> to insert into this <code>Junction</code>.
	 */
	//Invocado por Vehicles -> Ordenados por orden de llegada
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
	
	/**
	 * Lets a <code>Vehicle</code> out of this <code>Junction</code>.
	 * 
	 * @return <code>Vehicle</code> that comes out of this <code>Junction</code>.
	 */
	public Vehicle vehicleOut(){
		return incomingRoads.get(currentOpenQueue).removeFirst();
	}
	
	/**
	 * Returns the <code>Road</code> joining this <code>Junction</code> and the one passed
	 * by parameter.
	 * 
	 * @param j	<code>Junction</code> to be joined to this.
	 * @return	<code>Road</code> that joins this <code>Junction</code> and the parameter.
	 * @throws UnreachableJunctionException.
	 */
	public Road getRoadToJunction(Junction j) throws UnreachableJunctionException{
		Road result = null, road;
		boolean found = false;
		Iterator<JunctionQueue> it = outgoingRoads.iterator();
		while(!found && it.hasNext()) {
			road = it.next().getRoad();
			if(road.getEnd().equals(j)) {
				result = road;
				found = true;
			}
		}

		if(!found) {
			throw new UnreachableJunctionException("Could not get from " + getID() + " to " + j.getID());
		}

		return result;
	}
	
	/**
	 * Lets the next <code>Vehicle</code> waiting in the open lane move to the next <code>Road</code>.
	 */
	public void move(){
		Vehicle out = vehicleOut();
		out.moveToNextRoad();
		updateTrafficLights();
	}
	
	/**
	 * Adds a new <code>Road</code> to the incoming list.
	 * 
	 * @param r	<code>Road</code> to add.
	 */
	public void addIncomingRoad(Road r) {
		incomingRoads.add(new JunctionQueue(r));
		if(currentOpenQueue == -1) {
			currentOpenQueue = 0;
			incomingRoads.get(0).setTrafficLight(true);
		}
	}

	/**
	 * Adds a new <code>Road</code> to the incoming list.
	 * 
	 * @param r	<code>Road</code> to add.
	 */
	public void addOutgoingRoad(Road r) {
		outgoingRoads.add(new JunctionQueue(r));
	}	

	//AUTOMATIC JAVADOC¿?
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
