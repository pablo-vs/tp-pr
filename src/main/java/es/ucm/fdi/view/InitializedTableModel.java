package es.ucm.fdi.view;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class InitializedTableModel extends AbstractTableModel {

	private String[] columnTags;
	private String[][] data;
	
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
