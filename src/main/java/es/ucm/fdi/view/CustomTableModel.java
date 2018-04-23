package es.ucm.fdi.view;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.util.MultiTreeMap;

public abstract class CustomTableModel extends AbstractTableModel implements Simulator.Listener{
	/**
	 * Generated version UID
	 */
	private static final long serialVersionUID = -4823431851535170164L;
	private String[] columnTags;
	private List<String[]> data;
	private List<Simulator.EventType> updateEvents;

	public CustomTableModel(String[] columnTags){
		this.columnTags = columnTags;
		this.data = new ArrayList<String[]>();
		this.updateEvents = new ArrayList<Simulator.EventType>();
	}
	
	public CustomTableModel(String[] columnTags, List<Simulator.EventType> updateEvents){
		this.columnTags = columnTags;
		this.data = new ArrayList<String[]>();
		this.updateEvents = updateEvents;
	}
	
	public CustomTableModel(String[] columnTags, List<String[]> data,
				     List<Simulator.EventType> updateEvents){
		this.columnTags = columnTags;
		this.data = data;
		this.updateEvents = updateEvents;
	}
	
	@Override
	public int getRowCount(){
		return data.size();
	}
	
	@Override
	public int getColumnCount(){
		return columnTags.length;
	}
	
	@Override
	public Object getValueAt(int row, int column){
		return data.get(row)[column];
	}
	
	@Override
	public String getColumnName(int i){
		return columnTags[i];
	}

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> newData) {
		data = newData;
	}

	public void update(Simulator.UpdateEvent ue, String error) {
	    switch(ue.getType()) {
			case REGISTERED:
			case ERROR: break;
			case RESET: {
				setData(new ArrayList<String[]>());
			} break;
			default: {
				
				if(updateEvents.contains(ue.getType())) {
					setData(getStrings(ue));
				}
				
			}
		}
		
		for(TableModelListener l : listenerList.getListeners(TableModelListener.class)) {
			l.tableChanged(new TableModelEvent(this));
		}
	}

	public abstract List<String[]> getStrings(Simulator.UpdateEvent ue);
	
	public static class EventsListModel extends CustomTableModel {

		/**
		 * Generate UID.
		 */
		private static final long serialVersionUID = 4711550276080442763L;
		private static final String[] tags = {"#", "Time", "Type"};
		private static final Simulator.EventType[] events = {Simulator.EventType.NEW_EVENT,
									 Simulator.EventType.ADVANCED};

		public EventsListModel() {
			super(tags, Arrays.asList(events));
		}

		@Override
		public List<String[]> getStrings(Simulator.UpdateEvent ue) {
			ArrayList<String[]> list = new ArrayList<>();
			int i = 0;
			for(Event e : ue.getEventQueue()) {
				String[] row = {Integer.toString(i),
						Integer.toString(e.getTime()),
						e.getDescription()};
				list.add(row);
				++i;
			}
			return list;
		}
	}

	public static class VehicleListModel extends CustomTableModel {

		/**
		 * Generated UID.
		 */
		private static final long serialVersionUID = 1L;
		private static final String[] tags = {"ID", "Road", "Location", "Speed", "Km",
					 "Faulty Units", "Initerary"};
		private static final Simulator.EventType[] events = {Simulator.EventType.ADVANCED};
		
		public VehicleListModel() {
			super(tags, Arrays.asList(events));
		}

		@Override
		public List<String[]> getStrings(Simulator.UpdateEvent ue) {
			ArrayList<String[]> list = new ArrayList<>();
			int i = 0;
			for(Vehicle v : ue.getVehicles()) {
				String[] row = {v.getID(),
						v.getRoad().getID(),
						Integer.toString(v.getPosition()),
						Integer.toString(v.getCurrentVelocity()),
						Integer.toString(v.getKm()),
						Integer.toString(v.getBrokenTime()),
						"[" + String.join(",", v.getItinerary()
								  .stream().map(SimObject::getID)
								  .collect(Collectors.toList())) + "]"};
				list.add(row);
			}
			return list;
		}		
	}

	public static class RoadListModel extends CustomTableModel {

		/**
		 * Generated UID.
		 */
		private static final long serialVersionUID = 1L;
		private static final String[] tags = {"ID", "Source", "Target", "Length", "Max Speed",
						      "Vehicles"};
		private static final Simulator.EventType[] events = {Simulator.EventType.ADVANCED};
		
		public RoadListModel() {
			super(tags, Arrays.asList(events));
		}

		@Override
		public List<String[]> getStrings(Simulator.UpdateEvent ue) {
			ArrayList<String[]> list = new ArrayList<>();
			int i = 0;
			for(Road r : ue.getRoads()) {
				String[] row = {r.getID(),
						r.getIni().getID(),
						r.getEnd().getID(),
						Integer.toString(r.getLength()),
						Integer.toString(r.getMaxVel()),
						"[" + String.join(",", r.getVehicles().valuesList()
								  .stream().map(SimObject::getID)
								  .collect(Collectors.toList())) + "]"};
				list.add(row);
			}
			return list;
		}		
	}

	public static class JunctionListModel extends CustomTableModel {

		/**
		 * Generated UID.
		 */
		private static final long serialVersionUID = 1L;
		private static final String[] tags = {"ID", "Green", "Red"};
		private static final Simulator.EventType[] events = {Simulator.EventType.ADVANCED};
		
		public JunctionListModel() {
			super(tags, Arrays.asList(events));
		}

		@Override
		public List<String[]> getStrings(Simulator.UpdateEvent ue) {
			ArrayList<String[]> list = new ArrayList<>();
			int i = 0;
			for(Junction j : ue.getJunctions()) {
				String[] row = {j.getID(), "[" + describeQueue(j.getOpenRoad()) + "]",
						"[" + String.join(",", j.getClosedRoads()
								  .stream().map(this::describeQueue)
								  .collect(Collectors.toList())) + "]"};
				list.add(row);
			}
			return list;
		}

		public String describeQueue(Junction.IncomingRoad r) {
			if(r != null) {
				return "(" + r.getRoad().getID() +
					(r.getTrafficLight() ? ",green,[" : ",red,[") +
					String.join(",", r.stream().map(SimObject::getID)
						    .collect(Collectors.toList())) + "]";
			} else {
				return "";
			}
			
		}
			
	}
	
	
}
