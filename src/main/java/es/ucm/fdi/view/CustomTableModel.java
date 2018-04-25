package es.ucm.fdi.view;

import java.util.List;	
import java.util.ArrayList;
import java.util.Map;	
import java.util.HashMap;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.sim.Describable;
import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.util.MultiTreeMap;

/**
 * TableModel for all tables in the view.
 *
 * @version 15/04/2018
 */
public abstract class CustomTableModel extends AbstractTableModel implements Simulator.Listener {
	/**
	 * Generated version UID
	 */
	private static final long serialVersionUID = -4823431851535170164L;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private List<Describable> origin;
	private String[] columnTags;
	private List<Simulator.EventType> updateEvents;
	
	public CustomTableModel(String[] columnTags, List<Simulator.EventType> updateEvents, List<Describable> origin){
		this.columnTags = columnTags;
		this.updateEvents = updateEvents;
		this.origin = origin;
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
		return data.get(row).get(columnTags[column]);
	}
	
	@Override
	public String getColumnName(int i){
		return columnTags[i];
	}

	public void setElements(List<Describable> newData) {
		int i = 0;
		for(Describable obj : newData) {
			Map<String, String> map = data.get(i);
			obj.describe(map);
			data.set(i, map);
			++i;
		}
		origin = newData;
	}

	public void update(Simulator.UpdateEvent ue, String error) {
	    switch(ue.getType()) {
			case REGISTERED:
			case ERROR: break;
			case RESET: {
				data.clear();
			} break;
			default: {
				if(updateEvents.contains(ue.getType())) {
					setElements(origin);
				}
			}
		}
		for(TableModelListener l : listenerList.getListeners(TableModelListener.class)) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
