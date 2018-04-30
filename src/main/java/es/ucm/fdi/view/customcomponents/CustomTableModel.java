package es.ucm.fdi.view.customcomponents;

import java.util.List;		
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import es.ucm.fdi.exceptions.ObjectNotFoundException;
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
	private List<? extends Describable> origin;
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

	/**
	 * Clear the data in the model.
	 */
	public void clear() {
		data.clear();
		origin = null;
		change();
	}

	/**
	 * Return selected objects from the origin of the data
	 *
	 * @param rows An array of selected row indexes.
	 */
	public List<? extends Describable> getSelected(int[] rows) {
		List<Describable> selection = new ArrayList<Describable>();
		for(int i : rows) {
			try {
				selection.add(origin.get(i));
			} catch(IndexOutOfBoundsException e) {
				throw new ObjectNotFoundException("Table origin and selection mismatch:\n" + e.getMessage(), e);
			}
		}
		return selection;
	}

	/**
	 * Changes the model data.
	 *
	 * @param newData A <code>List</code> of <code>Describable</code> objects which contains the
	 * new data.
	 */
	public void setElements(List<? extends Describable> newData) {
		setElements(newData, "__");
	}

	/**
	 * Changes the model data, reserving a column for row index.
	 *
	 * @param newData A <code>List</code> of <code>Describable</code> objects which contains the
	 * new data.
	 * @param orderTag The tag of the column which will contain the row index.
	 */
	public void setElements(List<? extends Describable> newData, String orderTag) {
		data.clear();
		int i = 0;
		for(Describable obj : newData) {
			Map<String, String> map = new HashMap<String, String>();
			obj.describe(map);
			map.put(orderTag, ""+i);
			data.add(map);
			++i;
		}
		origin = newData;
		change();
	}
	
	private void change() {
		for(TableModelListener l : listenerList.getListeners(TableModelListener.class)) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
