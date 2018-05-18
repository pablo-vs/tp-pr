package es.ucm.fdi.view;

import javax.swing.*;	

import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.exceptions.SimulatorException;
import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.exceptions.UnreachableJunctionException;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.Simulator;
import es.ucm.fdi.sim.events.Event;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.SimObject;
import es.ucm.fdi.view.util.ConcurrentSimulation;
import es.ucm.fdi.view.util.Actions;
import es.ucm.fdi.view.customcomponents.CustomTable;
import es.ucm.fdi.view.customcomponents.CustomTableModel;
import es.ucm.fdi.view.customcomponents.CustomGraphLayout;
import es.ucm.fdi.view.customcomponents.CustomTextComponent;

/**
 * Main window for the simulator.
 * 
 * @version 06.05.2018
 */
public class SimWindow extends JPanel implements Simulator.Listener {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2574375309247665340L;

	private static final int DEFAULT_DELAY = 100;
	private static final String TEMPLATE_PATH = "./src/main/resources/templates/";
	private static final String TEMPLATE_INDEX_FILE = "Index.ini";

	private final double NS_SPLIT_DIVISION = 0.3;
	private final double EW_SPLIT_DIVISION = 0.5;

	private Controller controller;
	private CustomTextComponent eventsEditor = new CustomTextComponent(true);
	private CustomTextComponent reportsArea = new CustomTextComponent(false);

	private CustomTable eventsQueueTable = new CustomTable(new CustomTableModel(
			Event.getColumns()));
	private CustomTable vehiclesTable = new CustomTable(new CustomTableModel(
			Vehicle.getColumns()));
	private CustomTable roadsTable = new CustomTable(new CustomTableModel(
			Road.getColumns()));
	private CustomTable junctionsTable = new CustomTable(new CustomTableModel(
			Junction.getColumns()));

	private Actions[] actionsToUnlock = {Actions.CLEAR_EDITOR, 
										Actions.DELETE_REPORT, 
										Actions.INSERT_EVENT_DATA,
										Actions.LOAD_EVENT,
										Actions.PLAY,
										Actions.REDIRECT_OUTPUT,
										Actions.REPORT,
										Actions.REPORT_SELECTED,
										Actions.RESET,
										Actions.SAVE_EVENT,
										Actions.SAVE_REPORT};
	private JTextField contextualBar = new JTextField();
	private CustomGraphLayout graph = new CustomGraphLayout();
	private JSpinner steps;
	private JSpinner delay;
	private JTextField time;
	private Thread simulationThread = null;
	private int stepCount = 0;

	public SimWindow(Controller cont) {
		JFrame jf = new JFrame("Traffic Simulator");
		controller = cont;

		//We center the tables
		eventsQueueTable.center();
		vehiclesTable.center();
		roadsTable.center();
		junctionsTable.center();
		
		contextualBar.setEditable(false);
		contextualBar.setHorizontalAlignment(JTextField.CENTER);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLayout(new BorderLayout());
		setLayout(new BorderLayout());

		addActions();
		addButtonBar();
		addToolBar(jf);
		addCenterPanel(jf);

		jf.add(this, BorderLayout.CENTER);
		jf.add(contextualBar, BorderLayout.SOUTH);
		contextualBar.setText("Welcome to UCM's Custom Traffic Simulator");
		controller.addListener(this);
	}

	/**
	 * Adds to the action maps all possible actions in this window.
	 */
	private void addActions() {
		// LOADS EVENTS
		new SimulatorAction(Actions.LOAD_EVENT, "open.png",
				"Loads an input file", KeyEvent.VK_L, "control shift L",
				() -> {
					try {
						if (eventsEditor.load()) {
							contextualBar.setText("Events loaded from file");
						}
					} catch (IOException e) {
						Logger.getLogger(SimulatorAction.class.getName()).log(
								Level.WARNING,
								"Error while reading events file", e);
						showErrorMessage("Error while reading events file.\n"
								+ e.getMessage());
					}
				}).register(this);
		// SAVES EVENTS
		new SimulatorAction(Actions.SAVE_EVENT, "save.png",
				"Saves the event data in a file", KeyEvent.VK_S,
				"control shift S", () -> {
					try {
						if (eventsEditor.save()) {
							contextualBar.setText("Events saved to file");
						}
					} catch (IOException e) {
						Logger.getLogger(SimulatorAction.class.getName()).log(
								Level.WARNING, "Error while saving events", e);
						showErrorMessage("Error while saving events.\n"
								+ e.getMessage());
					}
				}).register(this);
		// CLEARS EVENT EDITOR
		new SimulatorAction(Actions.CLEAR_EDITOR, "clear.png",
				"Clears the current event data", KeyEvent.VK_C,
				"control shift C", () -> {
					eventsEditor.clear();
					contextualBar.setText("Event editor cleared");
				}).register(this);
		// ADDS EVENT DATA TO TABLE
		new SimulatorAction(Actions.INSERT_EVENT_DATA, "events.png",
				"Adds the event data to the event queue", KeyEvent.VK_I,
				"control shift I", () -> {
					try {
						controller.readEvents(new ByteArrayInputStream(
								eventsEditor.getText().getBytes(
										StandardCharsets.UTF_8)));
						contextualBar.setText("Events added to event queue");
					} catch (IOException e) {
						Logger.getLogger(SimulatorAction.class.getName())
							.log(Level.WARNING, "IO error while reading event"
								,e);
						showErrorMessage("IO error while reading event.");
					} catch (IllegalArgumentException | ObjectNotFoundException
							| UnreachableJunctionException e) {
						showErrorMessage("Invalid event file.\n"
								+ e.getMessage());
					}
				}).register(this);
		// RUNS FOR INDICATED STEPS
		new SimulatorAction(Actions.PLAY, "play.png",
				"Executes the indicated steps", KeyEvent.VK_X,
				"control shift X", () -> {
					try {
						
						stepCount = 0;
						
						ConcurrentSimulation csim = new ConcurrentSimulation(controller,
								(int)delay.getValue(), (int)steps.getValue(), this);
						
						if(!(simulationThread != null && simulationThread.isAlive())) {
							simulationThread = new Thread(csim);
							simulationThread.start();
						}
						
					} catch (SimulatorException e) {
						showErrorMessage(e.getMessage());
					}
				}).register(this);
		// STOPS SIMULATION
		new SimulatorAction(Actions.STOP, "stop.png", "Stops the simulation",
				KeyEvent.VK_T, "control shift T", ()->{
					if(!(simulationThread == null) && simulationThread.isAlive()) {
						simulationThread.interrupt();
						unlockActions(actionsToUnlock);
					}
				}).register(this);
		// RESETS SIMULATOR
		new SimulatorAction(Actions.RESET, "reset.png",
				"Resets the simulation to its initial point", KeyEvent.VK_R,
				"control shift R", () -> {
					controller.reset();
					contextualBar.setText("Simulator settings reset");
				}).register(this);
		// EXITS THE PROGRAM
		new SimulatorAction(Actions.EXIT, "exit.png", "Exit the program",
				KeyEvent.VK_E, "control shift E", () -> System.exit(0))
				.register(this);
		// SHOWS REPORTS IN TEXT AREA
		new SimulatorAction(Actions.REPORT, "report.png", "Show reports",
				KeyEvent.VK_T, " control shift T", () -> {
					try {
						controller.dumpOutput(reportsArea.getStreamToText());
						contextualBar.setText("Reports added to Report Area");
					} catch (IOException e) {
						Logger.getLogger(SimulatorAction.class.getName())
								.log(Level.WARNING,
										"Error while writing reports", e);
						showErrorMessage("Error while writing reports.\n"
								+ e.getMessage());
					}
				}).register(this);
		// SAVES REPORTS
		new SimulatorAction(
				Actions.SAVE_REPORT,
				"save_report.png",
				"Save reports",
				KeyEvent.VK_P,
				" control shift P",
				() -> {
					try {
						if (reportsArea.save()) {
							contextualBar.setText("Reports saved to file");
						}

					} catch (IOException e) {
						Logger.getLogger(SimulatorAction.class.getName()).log(
								Level.WARNING, "Error while saving reports", e);
						showErrorMessage("Error while saving reports.\n"
								+ e.getMessage());
					}
				}).register(this);
		// CLEARS REPORT AREA
		new SimulatorAction(Actions.DELETE_REPORT, "delete_report.png",
				"Delete reports", KeyEvent.VK_D, " control shift D", () -> {
					reportsArea.clear();
					contextualBar.setText("Report area cleared");
				}).register(this);
		// REDIRECTS OUTPUT
		new SimulatorAction(Actions.REDIRECT_OUTPUT, "report.png",
				"Redirects output", KeyEvent.VK_O, " control shift O",
				() -> {
					controller.redirectOutput(reportsArea.getStreamToText());
					contextualBar
							.setText("Output redirection preferences updated");
				}).register(this);
		
		//WRITES SELECTED
		new SimulatorAction(Actions.REPORT_SELECTED, "report.png", 
				"Generates reports for selected items", KeyEvent.VK_I, "control shift I",
				()->{
					writeSelectedReports();
				}).register(this);
	}

	/**
	 * Creates the upper toolbar.
	 * 
	 * @param jf	Current frame.
	 */
	private void addToolBar(JFrame jf) {
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu simulator = new JMenu("Simulator");
		JMenu reports = new JMenu("Reports");
		ActionMap m = getActionMap();

		file.add(new JMenuItem(m.get("" + Actions.LOAD_EVENT)));
		file.add(new JMenuItem(m.get("" + Actions.SAVE_EVENT)));
		file.addSeparator();
		file.add(new JMenuItem(m.get("" + Actions.SAVE_REPORT)));
		file.addSeparator();
		file.add(new JMenuItem(m.get("" + Actions.EXIT)));

		simulator.add(new JMenuItem(m.get("" + Actions.PLAY)));
		simulator.add(new JMenuItem(m.get("" + Actions.STOP)));
		simulator.add(new JMenuItem(m.get("" + Actions.RESET)));
		JCheckBox redirectOutput = new JCheckBox("Redirect Output");
		redirectOutput.setAction(m.get("" + Actions.REDIRECT_OUTPUT));
		simulator.add(redirectOutput);

		reports.add(new JMenuItem(m.get("" + Actions.REPORT)));
		reports.add(new JMenuItem(m.get("" + Actions.DELETE_REPORT)));
		reports.add(new JMenuItem(m.get("" + Actions.REPORT_SELECTED)));

		menu.add(file);
		menu.add(simulator);
		menu.add(reports);
		jf.add(menu, BorderLayout.NORTH);

	}

	/**
	 * Creates the upper button menu.
	 */
	private void addButtonBar() {
		JLabel stepsLabel = new JLabel(" Steps: ");
		JLabel timeLabel = new JLabel(" Time: ");
		JLabel delayLabel = new JLabel(" Delay: ");
		ActionMap m = getActionMap();

		steps = new JSpinner();
		delay = new JSpinner();
		((SpinnerNumberModel) steps.getModel()).setMinimum(0);
		((SpinnerNumberModel) delay.getModel()).setMinimum(0);
		((SpinnerNumberModel) delay.getModel()).setStepSize(10);
		((SpinnerNumberModel) delay.getModel()).setValue(DEFAULT_DELAY);
		steps.setPreferredSize(new Dimension(100, 10));

		time = new JTextField();
		time.setPreferredSize(new Dimension(100, 10));
		time.setEditable(false);
		time.setText("0");

		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		// Main buttons
		bar.add(m.get("" + Actions.LOAD_EVENT));
		bar.add(m.get("" + Actions.SAVE_EVENT));
		bar.add(m.get("" + Actions.CLEAR_EDITOR));
		bar.add(m.get("" + Actions.INSERT_EVENT_DATA));
		bar.add(m.get("" + Actions.PLAY));
		bar.add(m.get("" + Actions.STOP));
		bar.add(m.get("" + Actions.RESET));
		bar.addSeparator();
		// Here goes the delay JSpinner
		delay.setMaximumSize(new Dimension(100, 50));
		bar.add(delayLabel);
		bar.add(delay);
		// Here goes the steps JSpinner
		steps.setMaximumSize(new Dimension(100, 50));
		bar.add(stepsLabel);
		bar.add(steps);
		bar.addSeparator();
		// Here go the JTextPanes
		time.setMaximumSize(new Dimension(100, 50));
		bar.add(timeLabel);
		bar.add(time);
		// Last buttons
		bar.addSeparator();
		bar.add(m.get("" + Actions.REPORT));
		bar.add(m.get("" + Actions.SAVE_REPORT));
		bar.add(m.get("" + Actions.DELETE_REPORT));
		bar.add(m.get("" + Actions.EXIT));
		add(bar, BorderLayout.NORTH);
	}

	/**
	 * Creates the central panel.
	 * 
	 * @param jf Current frame.
	 */
	private void addCenterPanel(JFrame jf) {
		JSplitPane eastWestSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				createSouthWestPanel(), createSouthEastPanel());
		JSplitPane northSouthSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				createNorthPanel(), eastWestSplit);
		add(northSouthSplit, BorderLayout.CENTER);

		jf.pack();
		jf.setSize(1000, 1000);
		jf.setVisible(true);

		//InvokeLater used to ensure correct division placement
		SwingUtilities.invokeLater(() -> {
			northSouthSplit.setDividerLocation(NS_SPLIT_DIVISION);
			northSouthSplit.setResizeWeight(NS_SPLIT_DIVISION);
			eastWestSplit.setDividerLocation(EW_SPLIT_DIVISION);
			eastWestSplit.setResizeWeight(EW_SPLIT_DIVISION);
		});
	}

	/**
	 * Creates the north panel for the central panel division.
	 * 
	 * @return	Built north panel.
	 */
	private JPanel createNorthPanel() {
		ActionMap m = getActionMap();
		JPanel northPanel = new JPanel();
		JPopupMenu eventJPM = new JPopupMenu();
		JMenu templateMenu = new JMenu("Add template");
		JScrollPane eventsTableScroll = new JScrollPane(eventsQueueTable);
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));

		try {
			StringBuilder sb = new StringBuilder(TEMPLATE_PATH);
			sb.append(TEMPLATE_INDEX_FILE);
			Ini indexInfo = new Ini(sb.toString());
			JMenuItem nextItem;

			for (IniSection is : indexInfo.getSections()) {
				sb = new StringBuilder(TEMPLATE_PATH);
				nextItem = new JMenuItem();

				nextItem.setText(is.getValue("option"));
				nextItem.setToolTipText(is.getValue("tooltip"));
				sb.append(is.getValue("file"));

				final String text = new String(Files.readAllBytes(Paths.get(sb
						.toString())), "UTF-8");
				nextItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						eventsEditor.append(text);
						contextualBar.setText("Template added to event editor");
					}

				});

				templateMenu.add(nextItem);
			}

		} catch (IOException e) {
			showErrorMessage("Error reading template files.\n" + e.getMessage());
		}

		eventJPM.add(templateMenu);
		eventJPM.addSeparator();
		eventJPM.add(m.get("" + Actions.LOAD_EVENT));
		eventJPM.add(m.get("" + Actions.SAVE_EVENT));
		eventJPM.add(m.get("" + Actions.CLEAR_EDITOR));

		eventsEditor.setPopupMenu(eventJPM);
		northPanel.add(eventsEditor);
		eventsEditor.setBorder(BorderFactory
				.createTitledBorder("Events editor"));

		northPanel.add(eventsTableScroll);
		eventsTableScroll.setBorder(BorderFactory
				.createTitledBorder("Event List"));

		JPopupMenu reportsJPMenu = new JPopupMenu();
		reportsJPMenu.add(m.get("" + Actions.REPORT_SELECTED));
		reportsArea.setPopupMenu(reportsJPMenu);
		
		northPanel.add(reportsArea);
		reportsArea.setBorder(BorderFactory.createTitledBorder("Reports Area"));

		return northPanel;
	}

	/**
	 * Creates the SouthWest panel for the Central Panel division.
	 * 
	 * @return SouthWest panel.
	 */
	private JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel();

		JScrollPane table1 = new JScrollPane(vehiclesTable);
		JScrollPane table2 = new JScrollPane(roadsTable);
		JScrollPane table3 = new JScrollPane(junctionsTable);

		southWestPanel
				.setLayout(new BoxLayout(southWestPanel, BoxLayout.Y_AXIS));

		southWestPanel.add(table1);
		table1.setBorder(BorderFactory.createTitledBorder("Vehicles"));

		southWestPanel.add(table2);
		table2.setBorder(BorderFactory.createTitledBorder("Roads"));

		southWestPanel.add(table3);
		table3.setBorder(BorderFactory.createTitledBorder("Junctions"));

		southWestPanel.setPreferredSize(new Dimension(400, 600));

		return southWestPanel;
	}

	private JPanel createSouthEastPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		
		panel.setPreferredSize(new Dimension(400, 600));
		panel.setBorder(BorderFactory.createTitledBorder("Road Map"));
		panel.add(graph, BorderLayout.CENTER);
		
		return panel;
	}

	/**
	 * Receives an <code>UpdateEvent</code> from the <code>Simulator</code> and
	 * updates the GUI accordingly.
	 *
	 * @param ue
	 *            The <code>UpdateEvent</code> containing the details of the
	 *            event.
	 * @param error
	 *            An error message, possibly null.
	 */
	public void update(Simulator.UpdateEvent ue, String error) {
		CustomTableModel vehiclesModel = (CustomTableModel) vehiclesTable
				.getModel();
		CustomTableModel junctionsModel = (CustomTableModel) junctionsTable
				.getModel();
		CustomTableModel roadsModel = (CustomTableModel) roadsTable
				.getModel();
		CustomTableModel eventsModel = (CustomTableModel) eventsQueueTable
				.getModel();

		switch (ue.getType()) {
			case RESET:
				time.setText("0");
				graph.updateGraph(controller.getSimulator().getRoadMap());
	
				vehiclesModel.clear();
				roadsModel.clear();
				junctionsModel.clear();
				eventsModel.clear();
				break;
	
			case ERROR:
				break;
	
			case REGISTERED:
				break;
	
			case ADVANCED:
				time.setText(Integer.toString(controller.getSimulator().getTimer()));
				graph.updateGraph(controller.getSimulator().getRoadMap());
	
				contextualBar.setText("Simulation ran for " +
						++stepCount + " steps");
				
				writeSelectedReports();
	
				vehiclesModel.setElements(ue.getVehicles());
				roadsModel.setElements(ue.getRoads());
				junctionsModel.setElements(ue.getJunctions());
				break;
	
			case NEW_EVENT:
				eventsModel.setElements(ue.getEventQueue(), "#");
				break;
		}
	}

	/**
	 * Writes reports for selected table items if applicable.
	 */
	@SuppressWarnings("unchecked")
	private void writeSelectedReports() {
		CustomTableModel vehiclesModel = (CustomTableModel) vehiclesTable
				.getModel();
		CustomTableModel junctionsModel = (CustomTableModel) junctionsTable
				.getModel();
		CustomTableModel roadsModel = (CustomTableModel) roadsTable.getModel();

		ByteArrayOutputStream reports = new ByteArrayOutputStream();

		try {
			List<SimObject> selectedObjects = (List<SimObject>) junctionsModel
					.getSelected(junctionsTable.getSelectedRows());
			selectedObjects.addAll((List<SimObject>) roadsModel
					.getSelected(roadsTable.getSelectedRows()));
			selectedObjects.addAll((List<SimObject>) vehiclesModel
					.getSelected(vehiclesTable.getSelectedRows()));

			for (SimObject obj : selectedObjects) {
				obj.report(controller.getSimulator().getTimer()).store(reports);
				reports.write('\n');
			}
			reportsArea.append(reports.toString("UTF-8"));
		} catch (ObjectNotFoundException e) {
			showErrorMessage("Error while getting selected items.\n"
					+ e.getMessage());
		} catch (IOException e) {
			showErrorMessage("Could not write selected reports:\n"
					+ e.getMessage());
		}
	}

	/**
	 * Utility  method for the one below.
	 * 
	 * @param title		Error title
	 * @param message	Error message
	 */
	private void showErrorMessage(String title, String message) {
		Logger.getLogger(SimWindow.class.getName()).info(
				"Showing message: " + title + "\n" + message);
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Shows a popup message error dialogue
	 * 
	 * @param message	Error message
	 */
	private void showErrorMessage(String message) {
		showErrorMessage("An error occurred", message);
	}

	
	public void lockActions(Actions[] actions){
		ActionMap m = getActionMap();
		for(Actions a : actions){
			m.get(""+a).setEnabled(false);
		}
	}
	
	public void unlockActions(Actions[] actions){
		ActionMap m = getActionMap();
		for(Actions a : actions){
			m.get(""+a).setEnabled(true);
		}
	}
}
