package es.ucm.fdi.view;

import javax.swing.table.AbstractTableModel;

public class InitializedTableModel extends AbstractTableModel {
	/**
	 * Generated version UID
	 */
	private static final long serialVersionUID = -4823431851535170164L;
	private String[] columnTags;
	private String[][] data; //BETTER TO USE LIST
	
	public InitializedTableModel(String[] columnTags, String[][] data){
		this.columnTags = columnTags;
		this.data = data;
	}
	
	@Override
	public int getRowCount(){
		return data.length;
	}
	
	@Override
	public int getColumnCount(){
		return columnTags.length;
	}
	
	@Override
	public Object getValueAt(int row, int column){
		return data[row][column];
	}
	
	@Override
	public String getColumnName(int i){
		return columnTags[i];
	}

}
