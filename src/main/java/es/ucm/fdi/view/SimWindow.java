package es.ucm.fdi.view;

import java.io.IOException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.lang.IllegalArgumentException;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.view.CustomTableModel;
import es.ucm.fdi.view.CustomGraphLayout;
import es.ucm.fdi.view.CustomTextComponent;

public class SimWindow extends JPanel{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2574375309247665340L;

	private final double NS_SPLIT_DIVISION = 0.3;
	private final double EW_SPLIT_DIVISION = 0.5;
	
	private Controller controller;
	private CustomTextComponent eventsEditor, reportsArea;
	/*
	 * Events editor needs contextual menu support
	 * 
	 * 	Add template
	 * 		New RR Junction
	 * 		New MC Junction
	 * 		New Junction
	 * 		New Dirt Road
	 * 		New Lanes Road
	 * 		New Road
	 * 		New Bike
	 * 		New Car
	 * 		New Vehicle
	 * 		Make vehicle faulty
	 * -------------
	 * Load
	 * Save
	 * Clear
	 * 
	 */
	
	/*
	 * It needs to be possible to choose simulation objects.
	 */
	private CustomGraphLayout graph;
	private JSpinner steps;
	private JTextField time;
	
	private enum Actions{
		LOAD_EVENT("Load"), 
		SAVE_EVENT("Save"), 
		CLEAR_EDITOR("Clear"), 
		INSERT_EVENT_DATA("Insert"), 
		PLAY("Play"), 
		RESET("Reset"), 
		EXIT("Exit"),
		REPORT("Report"), 
		LOAD_REPORT("Load Report"),
		SAVE_REPORT("Save Report"), 
		DELETE_REPORT("Delete Report");
		
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
		eventsEditor = new CustomTextComponent(true);
		reportsArea = new CustomTextComponent(false);
		graph = new CustomGraphLayout();
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());	
		setLayout(new BorderLayout());
		
		addActions();
		addButtonBar();
		addToolBar(jf); //THIS IS NOT WORKING YET (?). ToolBar is not properly placed
		addCenterPanel(jf);
		
		jf.add(this, BorderLayout.CENTER);
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
						time.setText(Integer.toString(controller
								.getSimulator().getTimer()));
						graph.updateGraph(controller.getSimulator().getRoadMap());
					} catch (IOException e) {
						//doStuff
					}
				}).register(this);
		new SimulatorAction(Actions.RESET, "reset.png", "Resets the simulation to its initial point",
				KeyEvent.VK_R, "control shift R", ()->{
					time.setText("0");
					controller.reset();
					graph.updateGraph(controller.getSimulator().getRoadMap());
				}).register(this);
		new SimulatorAction(Actions.EXIT, "exit.png", "Exit the program",
				KeyEvent.VK_E, "control shift E", ()->System.exit(0))
					.register(this);
		new SimulatorAction(Actions.REPORT, "report.png", "Show reports", 
				KeyEvent.VK_T, " control shift T", ()->{
					//PENDING
				}).register(this);
		new SimulatorAction(Actions.SAVE_REPORT, "save_report.png", "Save reports", 
				KeyEvent.VK_P, " control shift P", ()->{
					//PENDING
				}).register(this);
		new SimulatorAction(Actions.DELETE_REPORT, "delete_report.png", "Delete reports", 
				KeyEvent.VK_D, " control shift D", ()->{
					//PENDING
				}).register(this);
	}
	
	public void addToolBar(JFrame jf){
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu reports = new JMenu("Reports");
		ActionMap m = getActionMap();
		
		file.add(new JMenuItem(m.get(""+Actions.LOAD_EVENT)));
		file.add(new JMenuItem(m.get(""+Actions.SAVE_EVENT)));
		file.addSeparator();
		file.add(new JMenuItem("Save Report"));
		file.addSeparator();
		file.add(new JMenuItem(m.get(""+Actions.EXIT)));
	
		simulator.add(new JMenuItem(m.get(""+Actions.PLAY)));
		simulator.add(new JMenuItem(m.get(""+Actions.RESET)));
		simulator.add(new JMenuItem("Redirect output"));
		
		reports.add(new JMenuItem(m.get(""+Actions.REPORT)));
		reports.add(new JMenuItem("Clear"));
		
		menu.add(file);
		menu.add(simulator);
		menu.add(reports);
		jf.add(menu, BorderLayout.NORTH);
		/*
		 * Requires finishing implementation
		 */
		
	}
	
	public void addButtonBar() {
		JLabel stepsLabel = new JLabel(" Steps: ");
		JLabel timeLabel = new JLabel(" Time: ");
		ActionMap m = getActionMap();

		steps = new JSpinner();
		((SpinnerNumberModel) steps.getModel()).setMinimum(0);
		steps.setPreferredSize(new Dimension(100,10));
		
		time = new JTextField();
		time.setPreferredSize(new Dimension(100,10));
		time.setEditable(false);
		time.setText("0");
		
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		//MAIN BUTTONS
		bar.add(m.get(""+Actions.LOAD_EVENT));
		bar.add(m.get(""+Actions.SAVE_EVENT));
		bar.add(m.get(""+Actions.CLEAR_EDITOR));
		bar.add(m.get(""+Actions.INSERT_EVENT_DATA));
		bar.add(m.get(""+Actions.PLAY));
		bar.add(m.get(""+Actions.RESET));
		bar.addSeparator();		
		//Here goes the spinner
		steps.setMaximumSize(new Dimension(100, 50));
		bar.add(stepsLabel);
		bar.add(steps);
		bar.addSeparator();	
		//Here go the JTextPanes
		time.setMaximumSize(new Dimension(100, 50));
		bar.add(timeLabel);
		bar.add(time);
		//LAST BUTTONS
		bar.addSeparator();
		bar.add(m.get(""+Actions.REPORT));
		bar.add(m.get(""+Actions.SAVE_REPORT));
		bar.add(m.get(""+Actions.DELETE_REPORT));
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

		SwingUtilities.invokeLater(()->{
				northSouthSplit.setDividerLocation(NS_SPLIT_DIVISION);
				northSouthSplit.setResizeWeight(NS_SPLIT_DIVISION);
				eastWestSplit.setDividerLocation(EW_SPLIT_DIVISION);
				eastWestSplit.setResizeWeight(EW_SPLIT_DIVISION);
		});
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
		
		return northPanel;
	}

	public JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel();

		CustomTableModel.VehicleListModel vehiclesModel = new CustomTableModel.VehicleListModel();
		CustomTableModel.RoadListModel roadsModel = new CustomTableModel.RoadListModel();
		CustomTableModel.JunctionListModel junctionsModel = new CustomTableModel.JunctionListModel();
		JScrollPane table1 = new JScrollPane(new JTable(vehiclesModel));
		JScrollPane	table2 = new JScrollPane(new JTable(roadsModel));
		JScrollPane	table3 = new JScrollPane(new JTable(junctionsModel));

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
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(400, 600));
		panel.setBorder(BorderFactory.createTitledBorder("Road Map"));
		panel.add(graph, BorderLayout.CENTER);
		return panel;
	}

}
