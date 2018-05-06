package es.ucm.fdi.sim;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.ini.Ini;

import java.io.OutputStream;
import java.io.IOException;

/**
 * Contains the complete simulator model.
 *
 * @version 05.03.2018
 */
public class Simulator {

	private MultiTreeMap<Integer, Event> eventList = 
			new MultiTreeMap<Integer, Event>(); //Ordered by time and arrival time.
	private List<Listener> listeners = new ArrayList<Listener>();
	private RoadMap roadMap = new RoadMap();
	private int timeLimit;
	private int timer = 0;
	
	/**
	 * Adds a mew <code>Event</code> to the list, ordered by time.
	 *
	 * @param e The <code>Event</code> to insert.
	 */
	public void insertEvent(Event e){
		Logger.getLogger(Simulator.class.getName())
			.info("Inserting event " + e.getClass().getName());
		eventList.putValue(e.getTime(), e);
		fireUpdateEvent(EventType.NEW_EVENT);
	}
	
	/**
	 * Advances the simulation, writing a report after each step.
	 *
	 * @param simulationSteps The number of steps to simulate
	 * @param outputFIle <code>OutputStream</code> to store the reports.
	 */
	public void execute(int simulationSteps, OutputStream outputFile) throws IOException {
		timeLimit = timer + simulationSteps - 1;
		Logger.getLogger(Simulator.class.getName())
			.info("Advancing simulator " + simulationSteps + " steps...");
		while (timer <= timeLimit) {
			// 1. Execute corresponding events
			for(Event e : eventList.getOrDefault(timer, new ArrayList<Event>())) {
				e.execute(roadMap);
			}
			
			eventList.remove(timer);
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
			dumpOutput(outputFile);
		}
		Logger.getLogger(Simulator.class.getName())
			.info("Advanced");
		fireUpdateEvent(EventType.ADVANCED);
	}
	
	/**
	 * Prepares a report of all the objects in the simulation.
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


	/**
	* Resets the simulator
	*/
	public void reset() {
		Logger.getLogger(Simulator.class.getName())
			.info("Resetting simulator");
		eventList.clear();
		roadMap = new RoadMap();
		timer = 0;
		fireUpdateEvent(EventType.RESET);
	}
	
	/**
	 * @return Current road map.
	 */
	public RoadMap getRoadMap(){
		return roadMap;
	}

	/**
	 * @return	Current timer.
	 */
	public int getTimer(){
		return timer;
	}
	
	/**
	 * Dumps output to the indicated output stream
	 * 
	 * @param outputFile
	 */
	public void dumpOutput(OutputStream outputFile) throws IOException {
		if(outputFile != null) {
			try {
				prepareReport().store(outputFile);
			}
			catch(IOException e) {
				throw new IOException("Error while writing report", e);
			}
		}
	}
	
	/**
	* Registers a new Listener and sends a REGISTERED event.
	*
	* @param l The Listener to add.
	*/
	public void addSimulatorListener(Listener l) {
		listeners.add(l);
		UpdateEvent event = new UpdateEvent(EventType.REGISTERED);
		Logger.getLogger(Simulator.class.getName())
			.fine("Sending UpdateEvent of type REGISTERED");
		SwingUtilities.invokeLater(()->l.update(event, ""));
	}

	/**
	* Removes a Listener.
	*
	* @param l The Listener to remove.
	*/
	public void removeListener(Listener l) {
		listeners.remove(l);
	}

	/**
	* Updates all the registered Listeners with the given event.
	*
	* @param type The event type
	* @param error An error message
	*/
	private void fireUpdateEvent(EventType type, String error) {
		UpdateEvent event = new UpdateEvent(type);
		for(Listener l : listeners) {
			SwingUtilities.invokeLater(()->l.update(event, error));
		}
	}

	/**
	* Updates all the registered Listeners with the given event.
	*
	* @param type The event type
	*/
	private void fireUpdateEvent(EventType type) {
		Logger.getLogger(Simulator.class.getName())
			.fine("Sending UpdateEvent of type " + type.toString());
		fireUpdateEvent(type, "");
	}

	/**
	 * Interface that allows implementers to get information from this <code>Simulator</code>
	 * when certain events occur.
	 */
  	public interface Listener {
		/**
		 * Called whenever an event occurs in a <code>Simulator</code>
		 * with this instance registered as a <code>Listener</coede>.
		 *
		 * @param ue <code>UpdateEvent</code> containing the details of the event.
		 * @param error An error message.
		 */
		public void update(UpdateEvent ue, String error);
	}

	/**
	 * Types of events that can be fired by a <code>Simulator</code>.
	 */
	public enum EventType {
		REGISTERED("REGISTERED"),
		RESET("RESET"),
		NEW_EVENT("NEW_EVENT"),
		ADVANCED("ADVANCED"),
		ERROR("ERROR");

		private final String str;

		EventType(String str) {
			this.str = str;
		}

		public String toString() {
			return str;
		}
	}

	/**
	 * Contains the information of an event, including type and
	 * state of the <code>Simulator</code> at the current time.
	 */
	public class UpdateEvent {
		private int time = timer;
		private EventType type;
		
		private List<Road> roads = roadMap.getRoads();
		private List<Vehicle> vehicles = roadMap.getVehicles();
		private List<Event> eventQueue = eventList.valuesList();
		private List<Junction> junctions = roadMap.getJunctions();

		public UpdateEvent(EventType type) {
			this.type = type;
		}

		public EventType getType() {
			return type;
		}

		/**
		 * Getter method for {@link UpdateEvent#vehicles}.
		 *
		 * @return The list of <code>Vehicles</code>.
		 */
		public List<Vehicle> getVehicles() {
			return vehicles;
		}

		/**
		 * Getter method for {@link UpdateEvent#junctions}.
		 *
		 * @return The list of <code>Junctions</code>.
		 */
		public List<Junction> getJunctions() {
			return junctions;
		}

		/**
		 * Getter method for {@link UpdateEvent#roads}.
		 *
		 * @return The list of <code>Roads</code>.
		 */
		public List<Road> getRoads() {
			return roads;
		}

		/**
		 * Getter method for {@link UpdateEvent#eventQueue}.
		 *
		 * @return The list of pending <code>Events</code>.
		 */
		public List<Event> getEventQueue() {
			return eventQueue;
		}

		/**
		 * Getter method for {@link UpdateEvent#time}.
		 *
		 * @return The current time.
		 */
		public int getCurrentTime() {
			return time;
		}
	}
}
