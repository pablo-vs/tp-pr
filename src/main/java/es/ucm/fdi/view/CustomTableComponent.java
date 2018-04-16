package es.ucm.fdi.view;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;

import java.util.concurrent.Callable;

import es.ucm.fdi.sim.Simulator;

public class CustomTableComponent extends JTable implements Simulator.Listener {
	
	private static final long serialVersionUID = 5234935651685122579L;

	private JTable table;
	private Callable listener;

	public CustomTableComponent(String[] tags, String[][] data) {
		super(new InitializedTableModel(tags, data));
	}

	public CustomTableComponent(String[] tags, String[][] data, Callable<TableModelEvent> listener) {
		this(tags, data);
	}

	public void update(Simulator.UpdateEvent ue, String error) {
		tableChanged(listener.call(ue, error));
	}
}