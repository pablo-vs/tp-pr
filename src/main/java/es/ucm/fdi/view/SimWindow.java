package es.ucm.fdi.view;

import java.io.IOException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.lang.IllegalArgumentException;
import java.util.ArrayList;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.view.CustomTextComponent;
import es.ucm.fdi.view.CustomTableModel;

public class SimWindow extends JPanel{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2574375309247665340L;

	private final double NS_SPLIT_DIVISION = 0.3;
	private final double EW_SPLIT_DIVISION = 0.5;
	
	private Controller controller;
	private CustomTextComponent eventsEditor, reportsArea;
	private JSpinner steps;
	
	private enum Actions{
		LOAD_EVENT("Load"), SAVE_EVENT("Save"), CLEAR_EDITOR("Clear"), 
		INSERT_EVENT_DATA("Insert"), PLAY("Play"), RESET("Reset"), EXIT("Exit");
		String s;	
		private Actions(String s){
			this.s = s;
		}
		
		public String toString(){
			return s;
		}
	}
	
	public SimWindow(Controller cont) {
		JFrame jf = new JFrame("Traffic Simulator");
		controller = cont;
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		eventsEditor = new CustomTextComponent(true);
		reportsArea = new CustomTextComponent(false);
		addActions();
		addToolbar();
		addCenterPanel(jf);
		
		jf.setLayout(new BorderLayout());
		jf.add(this, BorderLayout.CENTER);
		//jf.pack();
	}
	
	public void addActions(){
		new SimulatorAction(Actions.LOAD_EVENT, "open.png", "Loads an input file",
				KeyEvent.VK_L, "control shift L", ()->{
					try{
						eventsEditor.load();
					}catch(IOException e){
						
					}
				}).register(this);
		new SimulatorAction(Actions.SAVE_EVENT, "save.png", "Saves the event data in a file",
				KeyEvent.VK_S, "control shift S", ()->{
					try{
						eventsEditor.save();
					}catch(IOException e){
						
					}
				}).register(this);
		new SimulatorAction(Actions.CLEAR_EDITOR, "clear.png", "Clears the current event data",
				KeyEvent.VK_C, "control shift C", ()->{
					eventsEditor.clear();
				}).register(this);
		new SimulatorAction(Actions.INSERT_EVENT_DATA, "events.png", "Adds the event data to the event queue",
				KeyEvent.VK_I, "control shift I", ()->{
					try {
						controller.readEvents(new ByteArrayInputStream(eventsEditor.getText().getBytes(StandardCharsets.UTF_8)));
					} catch(IOException e) {
						System.err.println("IO error while reading event!");
					} catch(IllegalArgumentException e) {
						System.err.println("Invalid event file!");
					}
				}).register(this);
		new SimulatorAction(Actions.PLAY, "play.png", "Executes the indicated steps",
				KeyEvent.VK_X, "control shift X", ()->{
					try{
						controller.run((Integer)steps.getValue());
					} catch (IOException e) {
						//doStuff
					}
				}).register(this);
		new SimulatorAction(Actions.RESET, "reset.png", "Resets the simulation to its initial point",
				KeyEvent.VK_R, "control shift R", ()->{
					controller.reset();
				}).register(this);
		new SimulatorAction(Actions.EXIT, "exit.png", "Exit the program",
				KeyEvent.VK_E, "control shift E", ()->System.exit(0))
					.register(this);
	}
	
	public void addToolbar() {
		JLabel stepsLabel = new JLabel(" Steps: "), timeLabel = new JLabel(" Time: ");
		ActionMap m = getActionMap();

		steps = new JSpinner();
		((SpinnerNumberModel) steps.getModel()).setMinimum(0);
		steps.setPreferredSize(new Dimension(100,10));
		
		JTextField time = new JTextField();
		time.setPreferredSize(new Dimension(100,10));
		time.setText("0"); //initial
		time.setEditable(false);
		
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		bar.add(m.get(""+Actions.LOAD_EVENT));
		bar.add(m.get(""+Actions.SAVE_EVENT));
		bar.add(m.get(""+Actions.CLEAR_EDITOR));
		bar.add(m.get(""+Actions.INSERT_EVENT_DATA));
		bar.add(m.get(""+Actions.PLAY));
		bar.add(m.get(""+Actions.RESET));
		
		//Here goes the spinner
		bar.add(stepsLabel);
		bar.add(steps);
		
		//Here goes the JTextPanes
		bar.add(timeLabel);
		bar.add(time);
		
		bar.add(m.get(""+Actions.EXIT));
		add(bar, BorderLayout.NORTH);
	}

	public void addCenterPanel(JFrame jf) {
		JSplitPane eastWestSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				createSouthWestPanel(), 
				createSouthEastPanel());		
		JSplitPane northSouthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
			createNorthPanel(), 
			eastWestSplit);
		add(northSouthSplit, BorderLayout.CENTER);
		
		jf.pack();
		jf.setSize(1000, 1000);
		jf.setVisible(true);
		northSouthSplit.setDividerLocation(NS_SPLIT_DIVISION);
		northSouthSplit.setResizeWeight(NS_SPLIT_DIVISION);
		eastWestSplit.setDividerLocation(EW_SPLIT_DIVISION);
		eastWestSplit.setResizeWeight(EW_SPLIT_DIVISION);
	}

	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel();

	        CustomTableModel.EventsListModel eventsTableModel =
			new CustomTableModel.EventsListModel();
		
	        JScrollPane eventsTable = new JScrollPane(new JTable(eventsTableModel));
		
		controller.addListener(eventsTableModel);
		
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));

		northPanel.add(eventsEditor);
		eventsEditor.setBorder(BorderFactory.createTitledBorder("Events editor"));

		northPanel.add(eventsTable);
		eventsTable.setBorder(BorderFactory.createTitledBorder("Event List"));

		northPanel.add(reportsArea);
		reportsArea.setBorder(BorderFactory.createTitledBorder("Reports Area"));

		//northPanel.setPreferredSize(new Dimension(900, 150));
		
		return northPanel;
	}

	public JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel();
		
		String[] tags = {"data", "test"},
			dataRow1 = {"1", "hello"},
			dataRow2 = {"2", "hello"};
		ArrayList<String[]> data = new ArrayList<>();
		data.add(dataRow1);
		data.add(dataRow2);

		CustomTableModel.VehicleListModel vehiclesModel = new CustomTableModel.VehicleListModel();
		CustomTableModel.RoadListModel roadsModel = new CustomTableModel.RoadListModel();
		CustomTableModel.JunctionListModel junctionsModel = new CustomTableModel.JunctionListModel();
		JScrollPane table1 = new JScrollPane(new JTable(vehiclesModel)),
			table2 = new JScrollPane(new JTable(roadsModel)),
			table3 = new JScrollPane(new JTable(junctionsModel));

		controller.addListener(vehiclesModel);
		controller.addListener(roadsModel);
		controller.addListener(junctionsModel);
		
		southWestPanel.setLayout(new BoxLayout(southWestPanel, BoxLayout.Y_AXIS));

		southWestPanel.add(table1);
		table1.setBorder(BorderFactory.createTitledBorder("Vehicles"));

		southWestPanel.add(table2);
		table2.setBorder(BorderFactory.createTitledBorder("Roads"));

		southWestPanel.add(table3);
		table3.setBorder(BorderFactory.createTitledBorder("Junctions"));

		southWestPanel.setPreferredSize(new Dimension(400, 600));

		return southWestPanel;
	}

	public JPanel createSouthEastPanel() {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 600));
		return panel;
	}

}
