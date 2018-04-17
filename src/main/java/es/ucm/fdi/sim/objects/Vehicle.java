package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.SimObject;

/**
 *	Class that controls the general behavior of <code>Vehicles</code> in the simulation. 
 *	Allows basic dynamics such as movement between <code>Roads</code> and <code>Junctions</code>.
 *
 *	@version 13.03.2018
 */
public class Vehicle extends SimObject{
	private static String vehicle_header = "vehicle_report";
	private Road currentRoad;
	private List<Junction> itinerary;
	private int maxVel, currentVel, position, brokenTime, kilometrage, nextJunction;
	private boolean arrived, inQueue;
	
	
	/**
	 * Constructor.
	 * 
	 * @param id 		ID of the new object.
	 * @param maxVel	Maximum velocity of the new object.
	 * @param itinerary	Itinerary to follow by the <code>Vehicle</code>
	 */
	public Vehicle(String id, int maxVel, List<Junction> itinerary){
		super(id, vehicle_header);
		this.itinerary = new ArrayList<Junction>(itinerary);
		this.maxVel = maxVel;
		currentVel = 0;
		position = 0;
		brokenTime = 0;
		kilometrage = 0;
		nextJunction = 0; //Points to the position in the itinerary
		arrived = false;
		inQueue = false;
		moveToNextRoad();
	}

	/**
	 * Constructor with only two junctions.
	 * 
	 * @param id 		ID of the new object.
	 * @param maxVel	Maximum velocity of the new object.
	 * @param ini	        Initial Junction.
	 * @param end           Final Junction.
	 */
	public Vehicle(String id, int maxVel, Junction ini, Junction end) {
		super(id, vehicle_header);
		ArrayList<Junction> it = new ArrayList<>();
		it.add(ini); it.add(end);
		this.itinerary = it;
		this.maxVel = maxVel;
		currentVel = 0;
		position = 0;
		brokenTime = 0;
		kilometrage = 0;
		nextJunction = 0; //Points to the position in the itinerary
		arrived = false;
		inQueue = false;
		moveToNextRoad();
	}

	/**
	 * Copy constructor
	 */
	public Vehicle(Vehicle v) {
		this(v.getID(), v.maxVel, v.itinerary);
	}
	
	/**
	 * Moves this <code>Vehicle</code> in the current road if it is not broken or waiting
	 * to move to the next road.
	 *  
	 * If it reaches the end, it stops and is indexed in the ending <code>Junction</code>'s
	 * entry queue. 
	 */
	public void move(){
		
		if(brokenTime == 0 && !inQueue){						
			if(position + currentVel >= currentRoad.getLength()) {
				kilometrage += currentRoad.getLength() - position;
				position = currentRoad.getLength();
				currentVel = 0;
				currentRoad.getEnd().vehicleIn(this);
				inQueue = true;
    							
			} else {
				position += currentVel;
				kilometrage += currentVel;
			}
    			
		}else if(brokenTime > 0){
			brokenTime--;
		}
	}
	
	/**
	 * Moves this <code>Vehicle</code> to the next road through the current <code>Junction</code>.
	 * That is, the method tries to find a road that connects the current <code>Junction</code> to
	 * the next on the itinerary.
	 */
	public void moveToNextRoad(){
		position = 0;
		inQueue = false;

		if(nextJunction > 0) {
			currentRoad.vehicleOut(this);
		}
		
		if(nextJunction == itinerary.size()-1) {
			arrived = true;
		} else {
			Junction currentJunction = itinerary.get(nextJunction);
			nextJunction++;
			currentRoad = currentJunction.getRoadToJunction(itinerary.get(nextJunction));
			currentRoad.vehicleIn(this);
		}
	}
	
	/**
	 * Setter method for {@link Vehicle#brokenTime}.
	 * 
	 * @param t	Time for this <code>Vehicle</code> to be broken.
	 */
	public void setBrokenTime(int t){
		brokenTime += t;
		currentVel = 0;
	}
        
	/**
	 * Setter method for {@link Vehicle#currentVel}.
	 * 
	 * @param v	New velocity for this <code>Vehicle</code>.
	 */
	public void setCurrentVel(int v){
		currentVel = v <= maxVel ? v : maxVel;
		currentVel = brokenTime > 0 ? 0 : currentVel;
	}
	
	/**
	 * Setter method for {@link Vehicle#maxVel}.
	 * 
	 * @param v New maximum velocity for this <code>Vehicle</code>.
	 */
	public void setMaxVel(int v){
		maxVel = v;
	}
	
	/**
	 * Getter method for {@link Vehicle#maxVel}
	 * 
	 * @return Current <code>Vehicle</code>'s velocity.
	 */
	public int getMaxVel(){
		return maxVel;
	}
    
	/**
	 * Getter method for {@link Vehicle#currentVel}.
	 * 
	 * @return Current <code>Vehicle</code>'s velocity.
	 */
	public int getCurrentVelocity(){
		return currentVel;
	}
    
	/**
	 * Getter method for {@link Vehicle#position}.
	 * 
	 * @return Current <code>Road</code>'s position.
	 */
	public int getPosition(){
		return position;
	}

	/**
	 * Getter method for {@link Vehicle#currentRoad}.
	 * 
	 * @return Current <code>Road</code>.
	 */
	public Road getRoad() {
		return currentRoad;
	}

	/**
	 * Getter method for {@link Vehicle#kilometrage}.
	 * 
	 * @return Current kilometrage.
	 */
	public int getKm() {
		return kilometrage;
	}

	/**
	 * Getter method for {@link Vehicle#brokenTime}.
	 * 
	 * @return	Time for this <code>Vehicle</code> to be broken.
	 */
	public int getBrokenTime(){
		return brokenTime;
	}

	/**
	 * Getter method for {@link Vehicle#itinerary}.
	 * 
	 * @return	The itinerary of this <code>Vehicle</code>.
	 */
	public List<Junction> getItinerary() {
		return itinerary;
	}
	
	/**
	 * Indicates whether this <code>Vehicle</code> is faulty.
	 * 
	 * @return true if this <code>Vehicle</code> is faulty, false otherwise.
	 */
	public boolean isFaulty() {
		boolean faulty = false;
		if(brokenTime > 0){
			faulty = true;
		}
		return faulty;
	}


	
	/**
	 * Fills the given map with the details of the state of the object.
	 *
	 * @param out Map to store the report.
	 */
	public void fillReportDetails(IniSection out) {
		out.setValue("speed", Integer.toString(currentVel));
		out.setValue("kilometrage", Integer.toString(kilometrage));
		out.setValue("faulty", brokenTime);
		if(arrived){
			out.setValue("location", "arrived");
		}else{
			out.setValue("location", "(" + currentRoad.getID() + "," + position + ")");
		}
	}
}
