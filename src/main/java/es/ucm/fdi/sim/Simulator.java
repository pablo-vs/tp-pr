package es.ucm.fdi.sim;

import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.ini.Ini;

import java.util.ArrayList;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Contains the complete simulator model.
 *
 * @version 05.03.2018
 */
public class Simulator {

	MultiTreeMap<Integer, Event> eventList; //Ordered by time and arrival time.
	RoadMap roadMap;
	int timeLimit, timer;
	
	/**
	 * Default constructor for the <code>Simulator</code>.
	 */
	public Simulator(){
		eventList = new MultiTreeMap<Integer, Event>();
		roadMap = new RoadMap();
		timer = 0;
	}
	
	/**
	 * Adds a mew <code>Event</code> to the list, ordered by time.
	 *
	 * @param e The <code>Event</code> to insert.
	 */
	public void insertEvent(Event e){
		eventList.putValue(e.getTime(), e);
	}
	
	/**
	 * Advances the simulation, writing a report after each step.
	 *
	 * @param simulationSteps The number of steps to simulate
	 * @param outputFIle <code>OutputStream</code> to store the reports.
	 */
	public void execute(int simulationSteps, OutputStream outputFile) throws IOException {
		timeLimit = timer + simulationSteps - 1;
		while (timer <= timeLimit) {
			// 1. Execute corresponding events
			for(Event e : eventList.getOrDefault(timer, new ArrayList<Event>())) {
				e.execute(roadMap);
			}
			
			// 2. Invoke advance method for every Road
			for(Road r : roadMap.getRoads()) {
				r.move();
			}
				
			// 3. Invoke advance method for every Junction
			for(Junction j : roadMap.getJunctions()) {
				j.move();
			}
				
			// 4. Advance timer
			this.timer++;
				
			// 5. Write report in OutputStream (if it is not null)
			if(outputFile != null) {
				try {
					prepareReport().store(outputFile);
				}
				catch(IOException e) {
					throw new IOException("Error while writing report", e);
				}
			}
		}
	}
	
	/**
	 * Prepares a report of all the objects in the simulation, first the <code>Roads</code>,
	 * then the <code>Junctions</code> and finally the <code>vehicles</code>, by order of
	 * insertion.
	 *
	 * @return An <code>Ini</code> object storing the report
	 */
	public Ini prepareReport() {
		Ini report = new Ini();
		for(Junction j : roadMap.getJunctions()) {
			report.addsection(j.report(timer));
		}
		for(Road r : roadMap.getRoads()) {
			report.addsection(r.report(timer));
		}
		for(Vehicle v : roadMap.getVehicles()) {
			report.addsection(v.report(timer));
		}
		return report;
	}
}
