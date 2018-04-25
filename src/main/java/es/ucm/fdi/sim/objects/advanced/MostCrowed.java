package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction.IncomingRoad;

/**
 * Class that models the behavior of <code>MostCrowed</code> <code>Junctions</code> in the 
 * simulation.
 */
public class MostCrowed extends Junction{
	private int timeInterval = 0, usedTimeUnits = 0;

	/**
	 * Constructor for the class.
	 * 
	 * @param id		ID of the <code>Junction</code>.
	 */
	public MostCrowed(String id){
		super(id);
	}
	
	/**
	 * Constructor for the class.
	 * 
	 * @param id		ID of the <code>Junction</code>.
	 * @param incoming	List of incoming <code>Roads</code>.
	 * @param outgoing	List of outgoing <code>Roads</code>.
	 */
	public MostCrowed(String id, List<Road> incoming, List<Road> outgoing){
		super(id,incoming,outgoing);
	}
	
	/**
	 * Finds the next road up for traffic light updating.
	 * 
	 * @return Next <code>Road</code> whose traffic light should be set to green.
	 */
	public int findRoadToUpdate(){ //Assumes list isn't empty
		int max = 0;
		int size = incomingRoads.size();
		int open = super.getCurrentOpenQueue();
		for(int i=open;i<size+open;++i){
			if(incomingRoads.get(i%size).getWaiting() > incomingRoads.get(max).getWaiting()){
				max = i%size;
			}
		}
		return max;
	}
	
	/**
	 * Adapted method that finds the <code>Road</code> with the most <code>Vehicles</code>
	 * in queue and updates gives it priority on traffic light update.
	 */
	@Override
	protected void updateTrafficLights(){
		int open, next;
		if(incomingRoads.size() > 0 && timeInterval <= usedTimeUnits){
			if(super.getCurrentOpenQueue() != -1) {
				open = super.getCurrentOpenQueue();
				incomingRoads.get(open).setTrafficLight(false);
			}
			next = findRoadToUpdate();
			super.setCurrentOpenQueue(next);
			incomingRoads.get(next).setTrafficLight(true);
			timeInterval = Math.max(
					incomingRoads.get(super.getCurrentOpenQueue()).getWaiting()/2,
					1);
			usedTimeUnits = 0;
		}
	}
	
	/**
	 * Adapted move method that takes into account how many time units have been consumed.
	 */
	@Override
	public void move(){
		usedTimeUnits++;
		super.move();
	}
	
	/**
	 * Adapted method that includes the type field.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
		super.fillReportDetails(out);
		out.setValue("type", "mc");
	}
	
	@Override
	protected String describeQueue(IncomingRoad queue) {
		boolean firstVehicle;
		StringBuilder aux = new StringBuilder();
		
		aux.append(queue.getRoad().getID());
		aux.append(queue.getTrafficLight() ? ",green:" + (timeInterval-usedTimeUnits) + ",[" : ",red,[");
		
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

	/**
	 * Get time interval, for testing.
	 *
	 * @return The current time interval.
	 */
	public int getTimeInterval() {
		return timeInterval;
	}

	/**
	 * Get the used time, for testing.
	 *
	 * @return The current used time units.
	 */
	public int getUsedTimeUnits() {
		return usedTimeUnits;
	}
}
