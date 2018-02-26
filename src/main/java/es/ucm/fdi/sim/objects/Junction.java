package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;

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
	
	private static String report_header = "junction_report";
	private int time, currentOpenQueue;
	private List<IncomingRoad> incomingRoads;
	private List<Road> outgoingRoads;
	private HashMap<Road, IncomingRoad> roadToQueueMap;
	private HashMap<Junction, Road> junctionToRoadMap;

	/**
	 * Private class representing a <code>Road</code> with its respective queue of 
	 * <code>Vehicles</code> and traffic light.
	 */
	private class IncomingRoad extends ArrayDeque<Vehicle> {
		private Road road;
		private boolean trafficLight;

		/**
		 * Constructor.
		 * 
		 * @param road	<code>Road</code> associated to current <code>IncomingRoad</code>.
		 */
		IncomingRoad(Road road) {
			super();
			this.road = road;
			trafficLight = false;
		}

		/**
		 * Getter method for {@link IncomingRoad#road}.
		 * 
		 * @return Current <code>Road</code>.
		 */
		public Road getRoad() {
			return road;
		}

		/**
		 * Getter method for {@link IncomingRoad#trafficLight}.
		 * 
		 * @return State of traffic light.
		 */
		public boolean getTrafficLight() {
			return trafficLight;
		}

		/**
		 * Setter method for {@link IncomingRoad#trafficLight}.
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
		incomingRoads = new ArrayList<IncomingRoad>();
		outgoingRoads = new ArrayList<Road>();
		roadToQueueMap = new HashMap<Road, IncomingRoad>();
		junctionToRoadMap = new HashMap<Junction, Road>();

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
		incomingRoads = new ArrayList<IncomingRoad>(incoming.size());
		currentOpenQueue = 0;
		for(int i = 0; i < incoming.size(); ++i) {
			addIncomingRoad(incoming.get(i));
		}
		
		outgoingRoads = new ArrayList<Road>(outgoing.size());
		for(int i = 0; i < outgoing.size(); i++){
			addOutgoingRoad(outgoing.get(i));
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
		IncomingRoad queue = roadToQueueMap.get(v.getRoad());
		
		if(queue != null) {
			queue.add(v);
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
		Road result = junctionToRoadMap.get(j);

		if(result == null) {
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
		IncomingRoad newQueue = new IncomingRoad(r);
		incomingRoads.add(newQueue);
		roadToQueueMap.put(r, newQueue);
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
		outgoingRoads.add(r);
		junctionToRoadMap.put(r.getEnd(), r);
	}	

	/**
	* Returns the header for the object report.
	*
	* @return The header as a <code>String</code>
	*/
	public String getReportHeader() {
		return report_header;
	}

	/**
	* Fills the given map with the details of the state of the object.
	*
	* @param out Map to store the report.
	*/
	public void fillReportDetails(Map<String, String> out) {
		boolean first = true, firstVehicle;
		StringBuilder aux = new StringBuilder();
		
		for(IncomingRoad queue : incomingRoads){
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
		out.put("incomingRoads", aux.toString());
	}
}
