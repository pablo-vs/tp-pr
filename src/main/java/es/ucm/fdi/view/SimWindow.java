package es.ucm.fdi.view;

import java.io.IOException;	
import java.io.FileReader;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.lang.IllegalArgumentException;

import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;

import es.ucm.fdi.view.util.Tables;
import es.ucm.fdi.view.util.Actions;
import es.ucm.fdi.view.customcomponents.CustomGraphLayout;
import es.ucm.fdi.view.customcomponents.CustomTableModel;
import es.ucm.fdi.view.customcomponents.CustomTextComponent;

public class SimWindow extends JPanel implements Simulator.Listener {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 
			-2574375309247665340L;

	private static final String TEMPLATE_PATH = 
			"src/main/resources/templates";
	private static final String TEMPLATE_INDEX_FILE = "Index.ini";
	
	private final double NS_SPLIT_DIVISION = 0.3;
	private final double EW_SPLIT_DIVISION = 0.5;
	
	private Controller controller;
	private CustomTextComponent eventsEditor = new CustomTextComponent(true);
	private CustomTextComponent reportsArea = new CustomTextComponent(false);
	
	private JTable eventsQueueTable = new JTable(new CustomTableModel(Tables.EVENT_LIST.getTags()));
	private JTable vehiclesTable = new JTable(new CustomTableModel(Tables.VEHICLES.getTags()));
	private JTable roadsTable = new JTable(new CustomTableModel(Tables.ROADS.getTags()));
	private JTable junctionsTable = new JTable(new CustomTableModel(Tables.JUNCTIONS.getTags()));
	
	/*
	 * Events editor needs contextual menu support [just adding it now]
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
	 */
	
	/*
	 * It needs to be possible to choose simulation objects.
	 */
	private CustomGraphLayout graph;
	private JSpinner steps;
	private JTextField time;	
	
	public SimWindow(Controller cont) {
		JFrame jf = new JFrame("Traffic Simulator");
		controller = cont;

		graph = new CustomGraphLayout();
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());	
		setLayout(new BorderLayout());
		
		addActions();
		addButtonBar();
		addToolBar(jf);
		addCenterPanel(jf);
		
		jf.add(this, BorderLayout.CENTER);
		controller.addListener(this);
	}
	
	public void addActions(){
		//LOADS EVENTS
		new SimulatorAction(Actions.LOAD_EVENT, "open.png", "Loads an input file",
				KeyEvent.VK_L, "control shift L", ()->{
					try{
						eventsEditor.load();
					}catch(IOException e){
						
					}
				}).register(this);
		//SAVES EVENTS
		new SimulatorAction(Actions.SAVE_EVENT, "save.png", "Saves the event data in a file",
				KeyEvent.VK_S, "control shift S", ()->{
					try{
						eventsEditor.save();
					}catch(IOException e){
						
					}
				}).register(this);
		//CLEARS EVENT EDITOR
		new SimulatorAction(Actions.CLEAR_EDITOR, "clear.png", "Clears the current event data",
				KeyEvent.VK_C, "control shift C", ()->{
					eventsEditor.clear();
				}).register(this);
		//ADDS EVENT DATA TO TABLE
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
		//RUNS FOR INDICATED STEPS 
		new SimulatorAction(Actions.PLAY, "play.png", "Executes the indicated steps",
				KeyEvent.VK_X, "control shift X", ()->{
					try{
						controller.run((Integer)steps.getValue());
					} catch (IOException e) {
						//doStuff
					}
				}).register(this);
		//RESETS SIMULATOR
		new SimulatorAction(Actions.RESET, "reset.png", "Resets the simulation to its initial point",
				KeyEvent.VK_R, "control shift R", ()->{
					controller.reset();
				}).register(this);
		//EXITS THE PROGRAM
		new SimulatorAction(Actions.EXIT, "exit.png", "Exit the program",
				KeyEvent.VK_E, "control shift E", ()->System.exit(0))
					.register(this);
		//SHOWS REPORTS IN TEXT AREA
		new SimulatorAction(Actions.REPORT, "report.png", "Show reports", 
				KeyEvent.VK_T, " control shift T", ()->{
					reportsArea.clear();
					try{
						controller.dumpOutput(reportsArea.getStreamToText());
					}catch(IOException e){
						//doStuff
					}
				}).register(this);
		//SAVES REPORTS
		new SimulatorAction(Actions.SAVE_REPORT, "save_report.png", "Save reports", 
				KeyEvent.VK_P, " control shift P", ()->{
					try{
						reportsArea.save();
					}catch(IOException e){
						//doStuff
					}
				}).register(this);
		//CLEARS REPORT AREA
		new SimulatorAction(Actions.DELETE_REPORT, "delete_report.png", "Delete reports", 
				KeyEvent.VK_D, " control shift D", ()->{
					reportsArea.clear();
				}).register(this);
		//REDIRECTS OUTPUT
		new SimulatorAction(Actions.REDIRECT_OUTPUT, "report.png", "Redirects output", 
				KeyEvent.VK_O, " control shift O", ()->{
					controller.redirectOutput(reportsArea.getStreamToText());
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
		file.add(new JMenuItem(m.get(""+Actions.SAVE_REPORT)));
		file.addSeparator();
		file.add(new JMenuItem(m.get(""+Actions.EXIT)));
	
		simulator.add(new JMenuItem(m.get(""+Actions.PLAY)));
		simulator.add(new JMenuItem(m.get(""+Actions.RESET)));
		simulator.add(new JMenuItem(m.get(""+Actions.REDIRECT_OUTPUT)));
		
		reports.add(new JMenuItem(m.get(""+Actions.REPORT)));
		reports.add(new JMenuItem(m.get(""+Actions.DELETE_REPORT)));
		
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
		
        JScrollPane eventsTableScroll = new JScrollPane(eventsQueueTable);	
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		ActionMap m = getActionMap();
		
		//WE NEED TO CREATE AND ADD JPOPUPMENU HERE
		JPopupMenu eventJPM = new JPopupMenu();
		JMenu templateMenu = new JMenu("Add template");
		/*
		 * ADD FILES WITH INDEX OF TEMPLATE FILES
		 * 
		 * Format
		 * 
		 * Action Name Tooltip 
		 * Use loop to create actions below
		 */
		try(FileReader fr = new FileReader(TEMPLATE_INDEX_FILE)){
			
		}catch(IOException e){
			
		}
		templateMenu.add(new JMenuItem("New RR Junction"));
		templateMenu.add(new JMenuItem("New MC Junction"));
		templateMenu.add(new JMenuItem("New Junction"));
		templateMenu.add(new JMenuItem("New Dirt Road"));
		templateMenu.add(new JMenuItem("New Lanes Road"));
		templateMenu.add(new JMenuItem("New Road"));
		templateMenu.add(new JMenuItem("New Bike"));
		templateMenu.add(new JMenuItem("New Car"));
		templateMenu.add(new JMenuItem("New Vehicle"));
		templateMenu.add(new JMenuItem("Make Vehicle Faulty"));
		eventJPM.add(templateMenu);
		eventJPM.addSeparator();
		eventJPM.add(m.get(""+Actions.LOAD_EVENT));
		eventJPM.add(m.get(""+Actions.SAVE_EVENT));
		eventJPM.add(m.get(""+Actions.CLEAR_EDITOR));
		
		eventsEditor.setPopupMenu(eventJPM);
		northPanel.add(eventsEditor);
		eventsEditor.setBorder(BorderFactory.createTitledBorder("Events editor"));
		
		
		northPanel.add(eventsTableScroll);
		eventsTableScroll.setBorder(BorderFactory.createTitledBorder("Event List"));

		//DO WE NEED ANOTHER JPOPUPMENU?
		northPanel.add(reportsArea);
		reportsArea.setBorder(BorderFactory.createTitledBorder("Reports Area"));
		
		return northPanel;
	}

	public JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel();

		JScrollPane table1 = new JScrollPane(vehiclesTable);
		JScrollPane	table2 = new JScrollPane(roadsTable);
		JScrollPane	table3 = new JScrollPane(junctionsTable);

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
	
	public void update(Simulator.UpdateEvent ue, String error) {
		CustomTableModel vehiclesModel = (CustomTableModel) vehiclesTable.getModel();
		CustomTableModel junctionsModel = (CustomTableModel) junctionsTable.getModel();
		CustomTableModel roadsModel= (CustomTableModel) roadsTable.getModel();
		CustomTableModel eventsModel = (CustomTableModel) eventsQueueTable.getModel();
		
		switch(ue.getType()) {
		case RESET:
			time.setText("0");
			graph.updateGraph(controller.getSimulator().getRoadMap());
			
			vehiclesModel.clear();
			roadsModel.clear();
			junctionsModel.clear();
			eventsModel.clear();
		case ERROR:
		case REGISTERED:
			break;
		case ADVANCED:
			time.setText(Integer.toString(controller.getSimulator().getTimer()));
			graph.updateGraph(controller.getSimulator().getRoadMap());
			
			reportsArea.clear();
			vehiclesModel.getSelected(vehiclesTable.getSelectedRows());
			
			vehiclesModel.setElements(ue.getVehicles());
			roadsModel.setElements(ue.getRoads());
			junctionsModel.setElements(ue.getJunctions());
			break;
		case NEW_EVENT:
			eventsModel.setElements(ue.getEventQueue(), "#");
			break;
		}
	}

}
