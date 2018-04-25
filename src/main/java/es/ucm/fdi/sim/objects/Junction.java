package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ArrayDeque;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.exceptions.UnreachableJunctionException;

/**
 *	Class that models the general behavior for <code>Junctions</code> in the simulation.
 *
 *	@version 16.03.2018
 */
public class Junction extends SimObject{
	
	protected List<IncomingRoad> incomingRoads;
	
	private static String junction_header = "junction_report";	
	private int currentOpenQueue = -1;
	private List<Road> outgoingRoads;
	private HashMap<Road, IncomingRoad> roadToQueueMap;
	private HashMap<Junction, Road> junctionToRoadMap;

	/**
	 * Class representing a <code>Road</code> with its respective queue of 
	 * <code>Vehicles</code> and traffic light.
	 */
	public class IncomingRoad extends ArrayDeque<Vehicle> {
		/**
		 * Generated UID.
		 */
		private static final long serialVersionUID = 4467738671085074265L;
		private Road road;
		private boolean trafficLight;
		private int waiting;

		/**
		 * Constructor.
		 * 
		 * @param road	<code>Road</code> associated to current <code>IncomingRoad</code>.
		 */
		IncomingRoad(Road road) {
			super();
			this.road = road;
			trafficLight = false;
			waiting = 0;
		}
		
		/**
		 * Adapted add method that updates the number of <code>Vehicles</code> waiting.
		 */
		@Override
		public boolean add(Vehicle v){
			boolean ret = super.add(v);
			if(ret) waiting++;
			return ret;
		}
		
		/**
		 * Adapted {@link ArrayDeque#removeFirst} method that updates the number of <code>Vehicles</code> 
		 * waiting.
		 */
		@Override
		public Vehicle removeFirst(){
			Vehicle v = super.removeFirst();
			waiting--;
			return v;
		}
		
		/**
		 * Getter method for {@link IncomingRoad#waiting}.
		 * 
		 * @return	The number of <code>Vehicles</code> waiting in this queue.
		 */
		public int getWaiting(){
			return waiting;
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
		this(id, new ArrayList<Road>(), new ArrayList<Road>());
	}

	/**
	 * Constructor
	 * 
	 * @param id		ID of current <code>Junction</code>.
	 * @param incoming	List of the incoming <code>Roads</code> of this <code>Junction</code>.
	 * @param outgoing	List of the outgoing <code>Roads</code> of this <code>Junction</code>.
	 */
	public Junction(String id, List<Road> incoming, List<Road> outgoing){
		super(id, junction_header);
		roadToQueueMap = new HashMap<Road, IncomingRoad>();
		junctionToRoadMap = new HashMap<Junction, Road>();
		incomingRoads = new ArrayList<IncomingRoad>(incoming.size());
		currentOpenQueue = -1;
		for(int i = 0; i < incoming.size(); ++i) {
			addIncomingRoad(incoming.get(i));
		}
		
		outgoingRoads = new ArrayList<Road>(outgoing.size());
		for(int i = 0; i < outgoing.size(); i++){
			addOutgoingRoad(outgoing.get(i));
		}
	}
	
	/**
	 * Sets the traffic lights to their next state.
	 */
	protected void updateTrafficLights() {
		if(incomingRoads.size() > 0) {		
			if(currentOpenQueue != -1) {
				incomingRoads.get(currentOpenQueue).setTrafficLight(false);
			}
			currentOpenQueue = (currentOpenQueue + 1) % incomingRoads.size();
			incomingRoads.get(currentOpenQueue).setTrafficLight(true);
		}
	}
    
	/**
	 * Lets a <code>Vehicle</code> into this <code>Junction</code>.
	 * 
	 * @param v	<code>Vehicle</code> to insert into this <code>Junction</code>.
	 */
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
		return (currentOpenQueue == -1 || incomingRoads.size() == 0 ||
			incomingRoads.get(currentOpenQueue).size() == 0) ?
			null : incomingRoads.get(currentOpenQueue).removeFirst();
	}
	
	/**
	 * Getter method for {@link Junction#currentOpenQueue}.
	 * 
	 * @return	The index of the <code>Road</code> that currently has the traffic light set to
	 * green.
	 */
	public int getCurrentOpenQueue(){
		return currentOpenQueue;
	}
	
	/**
	 * Setter method for {@link Junction#currentOpenQueue}.
	 * 
	 * @param t	New value for the open queue.
	 */
	public void setCurrentOpenQueue(int t){
		currentOpenQueue = t;
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
	public void move() {
		Vehicle out = vehicleOut();
		if(out != null) {
			out.moveToNextRoad();
		}
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
	 * Returns the current open incoming road.
	 *
	 * @return The open <code>IncomingRoad</code>;
	 */
	public IncomingRoad getOpenRoad() {
		if(currentOpenQueue != -1) {
			return incomingRoads.get(currentOpenQueue);
		} else return null;
	}

	/**
	 * Returns the current closed roads.
	 *
	 * @return A <code>List</code> of closed <code>IncomingRoads</code>.
	 */
	public List<IncomingRoad> getClosedRoads() {
		ArrayList<IncomingRoad> list = new ArrayList<>();
		for(IncomingRoad r : roadToQueueMap.values()) {
			if(!r.equals(getOpenRoad())) {
				list.add(r);
			}
		}
		return list;
	}

	/**
	 * Fills the given map with the details of the state of the object.
	 *
	 * @param out Map to store the report.
	 */
	public void fillReportDetails(IniSection out) {
		boolean first = true;
		StringBuilder aux = new StringBuilder();
		
		for(IncomingRoad queue : incomingRoads){
			if(!first){
				aux.append(",");
			} else {
				first = false;
			}
			aux.append("(");
			aux.append(describeQueue(queue));
			aux.append(")");
		}
		out.setValue("queues", aux.toString());
	}
	
	protected String describeQueue(IncomingRoad queue) {
		boolean firstVehicle;
		StringBuilder aux = new StringBuilder();
		
		aux.append(queue.getRoad().getID());
		aux.append(queue.getTrafficLight() ? ",green,[" : ",red,[");
		
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
		return aux.toString();
	}
}
