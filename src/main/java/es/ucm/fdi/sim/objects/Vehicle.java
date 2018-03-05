package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;

/**
 *	Class that controls the general behavior of <code>Vehicles</code> in the simulation. 
 *	Allows basic dynamics such as movement between <code>Roads</code> and <code>Junctions</code>.
 *
 *	@version 26.02.2018
 */
public class Vehicle extends SimObject{
	
    private static String report_header = "vehicle_report";
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
	super(id);
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
	if(v <= maxVel){
	    currentVel = v;	
	}else{
	    currentVel = maxVel;
	}
    }
	
    /**
     * Setter method for {@link Vehicle#maxVel}.
     * 
     * @param v New maximum velocity for this <code>Vehicle</code>.
     */
    //NECESSARY?
    public void setMaxVel(int v){
	maxVel = v;
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
	if(brokenTime > 0){
	    out.setValue("faulty", "1");
	}else{
	    out.setValue("faulty", "0");
	}
	if(arrived){
	    out.setValue("location", "arrived");
	}else{
	    out.setValue("location", "(" + currentRoad.getID() + "," + position + ")");
	}
    }

    /**
     * Returns the header for the object report.
     *
     * @return The header as a <code>String</code>
     */
    public String getReportHeader() {
	return report_header;
    }
}
