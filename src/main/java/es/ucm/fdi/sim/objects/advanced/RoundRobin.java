package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

/**
 * Class that models <code>RoundRobins</code> in the simulation.
 */
public class RoundRobin extends Junction {
	private int minTimeSlice, maxTimeSlice, currentIntervalTime, timeConsumed;
	
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
		currentIntervalTime = 0;
		timeConsumed = 0;
	}
	
	/**
	 * Adapted method that updates the traffic lights only if the currently open road has
	 * consumed its time interval and calculates the time interval for the next road.
	 */
	@Override
	protected void updateTrafficLights(){
		if(timeConsumed == currentIntervalTime){
			super.updateTrafficLights();
			//currentIntervalTime = ; //Here the calculation !!!!
			timeConsumed = 0;
			
		} else {
			timeConsumed++;
		}
	}
	
	/**
	 * Adapted method that 
	 */
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "rr");
    }
}
