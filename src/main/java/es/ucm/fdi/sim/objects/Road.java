package es.ucm.fdi.sim.objects;

import java.util.List;
import java.lang.String;
import java.util.ArrayList;

import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

/**
 * Class that models the general behavior of <code>Roads</code> in the simulation.
 * 
 * @version 26.02.2018
 */
public class Road extends SimObject{
	
	private static String type = "road_report";
	//VehicleList: Position -> List of vehicles
	//VehicleList is inversely ordered
	private MultiTreeMap<Integer, Vehicle> vehicleList;
	private int length, maxVel;
	private Junction ini,end;
	
	/**
	 * Constructor.
	 * 
	 * @param id 	ID of the current object.
	 * @param l		Length of the current <code>Road</code>.
	 * @param maxV	Maximum velocity of the current <code>Road</code>.
	 * @param ini	Initial <code>Junction</code> of the current <code>Road</code>.
	 * @param end	Ending <code>Junction</code> of the current <code>Road</code>.
	 */
	public Road(String id, int l, int maxV, Junction ini, Junction end){
		super(id);
		length = l;
		maxVel = maxV;
		this.ini = ini;
		this.end = end;
		vehicleList = new MultiTreeMap<Integer, Vehicle>((Integer a, Integer b) -> b-a);
	}
	
	/**
	 * Inserts a <code>Vehicle</code> in the current <code>Road</code>.
	 * 
	 * @param v	<code>Vehicle</code> to insert in the current <code>Road</code>.
	 */
	public void vehicleIn(Vehicle v){
		vehicleList.putValue(0, v);
	}
	
	/**
	 * Removes the given vehicle from the end of the road.
	 * 
	 * @param The vehicle to remove
	 */
	public void vehicleOut(Vehicle v){
		vehicleList.removeValue(length, v);
	}
	
	/**
	 * Executes a <code>move</code> operation for every object in this <code>Road</code>.
	 */
	//Invoked by Simulator
	public void move(){
		int baseSpeed = Math.min(maxVel, maxVel/(Math.max(vehicleList.size(), 1)) + 1),
				reductionFactor = 1,
				faultyPos = 0;

		//Store the vehicles in a new map to avoid messing the iterator
		MultiTreeMap newVehicleList = new MultiTreeMap<Integer, Vehicle>((Integer a, Integer b) -> b-a);
		for(Vehicle v : vehicleList.innerValues()){
			//Check if vehicle is behind a faulty vehicle
			//All vehicles further in the list will also be behind it
			if(v.getPosition() < faultyPos){
				reductionFactor = 2;
			}
			//If the vehicle is faulty, we need to slow down all vehicles whose
			//position is STRICTLY lower than v's
			if(faultyPos == 0 && v.isFaulty()) {
				faultyPos = v.getPosition();
			}
			v.setCurrentVel(baseSpeed/reductionFactor);
			v.move();
			newVehicleList.putValue(v.getPosition(), v);
		}
		vehicleList = newVehicleList;
	}
	
	/**
	 * Getter method for {@link Road#length}.
	 * 
	 * @return Length of current <code>Road</code>.
	 */
	public int getLength(){
		return length;
	}
	
	/**
	 * Getter method for {@link Road#maxVel}.
	 * 
	 * @return Maximum velocity of current <code>Road</code>.
	 */
	public int getMaxVel(){
		return maxVel;
	}
	
	/**
	 * Getter method for {@link Road#ini}.
	 * 
	 * @return Initial <code>Junction</code> of current <code>Road</code>.
	 */
	public Junction getIni(){
		return ini;
	}
	
	/**
	 * Getter method for {@link Road#end}.
	 * 
	 * @return Ending <code>Junction</code> of current <code>Road</code>.
	 */
	public Junction getEnd(){
		return end;
	}
	
	//Automatic javadoc?
	public IniSection generateReport(int t){
		boolean first = true;
		IniSection sec = new IniSection(type);
		StringBuilder aux = new StringBuilder(vehicleList.size()*60);
		
		sec.setValue("id", getID());
		sec.setValue("time", t);
		for(Vehicle v : vehicleList.innerValues()){
			if(!first){
				aux.append(",");
			}
			else{
				first = false;
			}
			aux.append("(");
			aux.append(v.getID());
			aux.append(",");
			aux.append(v.getPosition());
			aux.append(")");
		}
		sec.setValue("state", aux.toString());
		
		return sec;
	}
}
