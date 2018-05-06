package es.ucm.fdi.view.customcomponents;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTable extends JTable{

	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = -3139454113666256490L;
	
	public CustomTable(AbstractTableModel m){
		super(m);
	}
	
	public void center(){
		DefaultTableCellRenderer centeredRenderer = new DefaultTableCellRenderer();
		centeredRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		for(int i = 0; i < getColumnCount(); ++i){
			this.getColumnModel().getColumn(i).setCellRenderer(centeredRenderer);
		}
	}
}
