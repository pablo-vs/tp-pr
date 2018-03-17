package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

/**
 * Class that models the behavior of <code>MostCrowed</code> <code>Junctions</code> in the 
 * simulation.
 */
public class MostCrowed extends Junction{
	private int timeInterval, usedTimeUnits;

	/**
	 * Constructor for the class.
	 * 
	 * @param id		ID of the <code>Junction</code>.
	 */
	public MostCrowed(String id){
		super(id);
		timeInterval = 0;
		usedTimeUnits = 0;
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
		timeInterval = 0;
		usedTimeUnits = 0;
	}

	/**
	 * Constructor from Junction.
	 */
	public MostCrowed(Junction j) {
		super(j);
	}
	
	/**
	 * Finds the next road up for traffic light updating.
	 * 
	 * @return Next <code>Road</code> whose traffic light should be set to green.
	 */
	public int findRoadToUpdate(){ //Assumes list isn't empty
		int max = 0;
		for(int i=0;i<incomingRoads.size();++i){
			if(incomingRoads.get(i).getWaiting() > incomingRoads.get(max).getWaiting()){
				max = i;
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
		if(incomingRoads.size() > 0 && timeInterval == usedTimeUnits){
			if(super.getCurrentOpenQueue() != -1) {
				incomingRoads.get(super.getCurrentOpenQueue()).setTrafficLight(false);
			}
			super.setCurrentOpenQueue(findRoadToUpdate());
			incomingRoads.get(super.getCurrentOpenQueue()).setTrafficLight(true);
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
