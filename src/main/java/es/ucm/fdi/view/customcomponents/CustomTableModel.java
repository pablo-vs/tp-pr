package es.ucm.fdi.view.customcomponents;

import java.util.List;		
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import es.ucm.fdi.sim.Describable;

/**
 * TableModel for all tables in the view.
 *
 * @version 15/04/2018
 */
public class CustomTableModel extends AbstractTableModel {
	/**
	 * Generated version UID
	 */
	private static final long serialVersionUID = -4823431851535170164L;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private String[] columnTags;
	
	public CustomTableModel(String[] columnTags){
		this.columnTags = columnTags;
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
	
	public void clear() {
		data.clear();
		change();
	}

	public void setElements(List<? extends Describable> newData) {
		data.clear();
		for(Describable obj : newData) {
			Map<String, String> map = new HashMap<String, String>();
			obj.describe(map);
			data.add(map);
		}
		change();
	}
	
	private void change() {
		for(TableModelListener l : listenerList.getListeners(TableModelListener.class)) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
