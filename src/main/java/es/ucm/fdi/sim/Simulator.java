package es.ucm.fdi.sim;

import java.util.List;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

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

	private MultiTreeMap<Integer, Event> eventList; //Ordered by time and arrival time.
	private RoadMap roadMap;
	private int timeLimit, timer;
	private List<Listener> listeners;
	
	/**
	 * Default constructor for the <code>Simulator</code>.
	 */
	public Simulator(){
		eventList = new MultiTreeMap<Integer, Event>();
		roadMap = new RoadMap();
		timer = 0;
		listeners = new ArrayList<>();
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

	public void addSimulatorListener(Listener l) {
		listeners.add(l);
		UpdateEvent event = new UpdateEvent(EventType.REGISTERED);
		SwingUtilities.invokeLater(()->l.update(event, ""));
	}

	public void removeListener(Listener l) {
		listeners.remove(l);
	}

	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent event = new UpdateEvent(type, roadMap, eventList, timer);
		for(Listener l : listeners) {
			l.update(event, error);
		}
	}

	public interface Listener {
		void update(UpdateEvent ue, String error);
	}

	public enum EventType {
		REGISTERED,
		RESET,
		NEW_EVENT,
		ADVANCED,
		ERROR;
	}

	public class UpdateEvent {
		private EventType type;
		private List<Vehicle> vehicles;
		private List<Road> roads;
		private List<Junction> junctions;
		private List<Event> eventQueue;
		private int time;

		public UpdateEvent(EventType type) {
			this.type = type;
			vehicles = new ArrayList<>();
			junctions = new ArrayList<>();
			roads = new ArrayList<>();
			eventQueue = new ArrayList<>();
			time = 0;
		}

		public UpdateEvent(EventType type, RoadMap map, MultiTreeMap<Integer, Event> eventQueue, int time) {
			this.type = type;
			vehicles = map.getVehicles();
			junctions = map.getJunctions();
			roads = map.getRoads();
			this.eventQueue = eventQueue.valuesList();
			this.time = time;
		}

		public EventType getEvent() {
			return type;
		}

		public List<Vehicle> getVehicles() {
			return vehicles;
		}

		public List<Junction> getJunctions() {
			return junctions;
		}

		public List<Road> getRoads() {
			return roads;
		}

		public List<Event> getEventQeue() {
			return eventQueue;
		}

		public int getCurrentTime() {
			return time;
		}
	}
}
