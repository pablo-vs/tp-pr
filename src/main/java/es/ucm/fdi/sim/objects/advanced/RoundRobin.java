package es.ucm.fdi.sim.objects.advanced;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction.IncomingRoad;

/**
 * Class that models <code>RoundRobins</code> in the simulation.
 */
public class RoundRobin extends Junction {
	private int minTimeSlice, maxTimeSlice, currentIntervalTime = 0, timeConsumed = 0;
	private boolean fullyUsed = false, notUsed = false;
	private List<Integer> intervals;
	
	/**
	 * Constructor for the <code>RoundRobin</code> class.
	 * 
	 * @param id		ID of the <code>RoundRobin</code>.
	 * @param min		Minimum time the currently open lane will remain open.
	 * @param max		Maximum time the currently open lane will remain open.
	 */
	public RoundRobin(String id, int min, int max){
		this(id, new ArrayList<Road>(), new ArrayList<Road>(), min, max);
	}
	
	/**
	 * Constructor for the <code>RoundRobin</code> class.
	 * 
	 * @param id		ID of the <code>RoundRobin</code>.
	 * @param incoming	List of <code>Roads</code> incoming to this <code>RoundRobin</code>.
	 * @param outgoing	List of <code>Roads</codes> outgoing from this <code>RoundRobin</code>.
	 * @param min		Minimum time the currently open lane will remain open.
	 * @param max		Maximum time the currently open lane will remain open.
	 */
	public RoundRobin(String id, List<Road> incoming, List<Road> outgoing, 
			int min, int max){
		super(id,incoming,outgoing);
		minTimeSlice = min;
		maxTimeSlice = max;
		intervals = new ArrayList<>();
	}

	
	/**
	 * Lets the next <code>Vehicle</code> waiting in the open lane move to the next <code>Road</code>
	 * and updates the {@link RoundRobin#fullyUsed} and {@link RoundRobin#notUsed} booleans.
	 */
	@Override
	public void move() {
		Vehicle out = vehicleOut();
		if(out != null) {
			out.moveToNextRoad();
			notUsed = false;
		}else{
			fullyUsed = false;
		}
		updateTrafficLights();
	}
	
	/**
	 * Adapted method that also adds the time interval to the list {@link RoundRobin#intervals}.
	 */
	@Override
	public void addIncomingRoad(Road r) {
		super.addIncomingRoad(r);
		intervals.add(maxTimeSlice);
	}
	
	/**
	 * Returns the list of time intervals associated to this <code>RoundRobin</code>.
	 * 
	 * @return	The list of time intervals.
	 */
	public List<Integer> getIntervals(){
		return intervals;
	}
	
	/**
	 * Adapted method that updates the traffic lights only if the currently open <code>Road</code>
	 * has consumed its time interval and calculates the time interval for the next road.
	 */
	@Override
	protected void updateTrafficLights(){
		if(incomingRoads.size() > 0){
			if(timeConsumed >= currentIntervalTime-1 || getCurrentOpenQueue() == -1) {
				if(fullyUsed){
					intervals.set(super.getCurrentOpenQueue(), 
							Math.min(currentIntervalTime+1, maxTimeSlice));
				}else if(notUsed){
					intervals.set(super.getCurrentOpenQueue(), 
							Math.max(currentIntervalTime-1, minTimeSlice));
				}
				
				super.updateTrafficLights();			
				fullyUsed = notUsed = true;
				currentIntervalTime = intervals.get(super.getCurrentOpenQueue());
				timeConsumed = 0;			
			} else {
				timeConsumed++;
			}	
		}
	}
	
	/**
	 * Adapted method that includes the type field.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "rr");
    }
	
	@Override
	protected String describeQueue(IncomingRoad queue) {
		boolean firstVehicle;
		StringBuilder aux = new StringBuilder();
		
		aux.append(queue.getRoad().getID());
		aux.append(queue.getTrafficLight() ? ",green:" + (currentIntervalTime-timeConsumed) + ",[" : ",red,[");
		
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
