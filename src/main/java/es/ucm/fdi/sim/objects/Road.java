package es.ucm.fdi.sim.objects;

import java.util.List;
import java.lang.String;
import java.util.ArrayList;

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
	private List<Vehicle> vehicleList; //0 position is last. Keep entry order right to left
	//WE NEED TO USE A QUEUE
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
		vehicleList = new ArrayList<Vehicle>();
	}
	
	/**
	 * Inserts a <code>Vehicle</code> in the current <code>Road</code>.
	 * 
	 * @param v	<code>Vehicle</code> to insert in the current <code>Road</code>.
	 */
	//Invoked by Vehicle - Ordered insertion
	public void vehicleIn(Vehicle v){	
		//Last one in list
		vehicleList.add(v);
	}
	
	/**
	 * Removes the first vehicle of the list.
	 * 
	 * @param v ??
	 */
	//Invoked by Vehicle. Since it is called by a vehicle, it is a precondition that a 
	//vehicle is in the list, given the program is correct NEEDS PARAMETERS?????
	public void vehicleOut(Vehicle v){
		vehicleList.remove(0);
	}
	
	/**
	 * Executes a <code>move</code> operation for every object in this <code>Road</code>.
	 */
	//Invoked by Simulator
	public void move(){
		int baseSpeed = Math.min(maxVel, maxVel/(Math.max(vehicleList.size(), 1)) + 1),
				reductionFactor = 1;
		
		for(Vehicle v : vehicleList){
			//CALCULATE REDUCTION FACTOR!!!!!
			if(v.isFaulty()){
				reductionFactor = 2;
			}
			v.setCurrentVel(baseSpeed/reductionFactor);
			v.move();
		}
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
		for(Vehicle v : vehicleList){
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
