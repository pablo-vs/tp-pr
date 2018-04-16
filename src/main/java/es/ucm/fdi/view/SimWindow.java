package es.ucm.fdi.view;

import java.io.IOException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.JSpinner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.view.CustomTextComponent;
import es.ucm.fdi.view.InitializedTableModel;

public class SimWindow extends JPanel{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -2574375309247665340L;

	Controller controller;
	CustomTextComponent eventsEditor, reportsArea;
	
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
					//doStuff
				}).register(this);
		new SimulatorAction(Actions.PLAY, "play.png", "Executes the indicated steps",
				KeyEvent.VK_X, "control shift X", ()->{
					//doStuff
				}).register(this);
		new SimulatorAction(Actions.RESET, "reset.png", "Resets the simulation to its initial point",
				KeyEvent.VK_R, "control shift R", ()->{
					//doStuff
				}).register(this);
		new SimulatorAction(Actions.EXIT, "exit.png", "Exit the program",
				KeyEvent.VK_E, "control shift E", ()->System.exit(0))
					.register(this);
	}
	
	public void addToolbar() {
		JLabel stepsLabel = new JLabel(" Steps: "), timeLabel = new JLabel(" Time: ");
		ActionMap m = getActionMap();

		JSpinner steps = new JSpinner();
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
		northSouthSplit.setDividerLocation(.5);
		eastWestSplit.setDividerLocation(.5);
	}

	public JPanel createNorthPanel() {
		JPanel northPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel centerPanel = new JPanel(new GridLayout());
		JPanel rightPanel = new JPanel();
		
		String[] tags = {"data", "test"};
		String[][] data = {{"1", "hello"}, {"2", "hello"}};
		JTable table = new JTable(
				new InitializedTableModel(tags,data) );
		
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
		leftPanel.add(eventsEditor);
		leftPanel.setBorder(BorderFactory.createTitledBorder("Events editor"));
		centerPanel.add(new JScrollPane(table));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Event List"));
		rightPanel.add(reportsArea);
		rightPanel.setBorder(BorderFactory.createTitledBorder("Reports Area"));
		
		northPanel.add(leftPanel);
		northPanel.add(centerPanel);
		northPanel.add(rightPanel);
		return northPanel;
	}

	public JPanel createSouthWestPanel() {
		JPanel southWestPanel = new JPanel();
		JPanel northPanel = new JPanel(new GridLayout());
		JPanel centerPanel = new JPanel(new GridLayout());
		JPanel southPanel = new JPanel(new GridLayout());
		
		String[] tags = {"data", "test"};
		String[][] data = {{"1", "hello"}, {"2", "hello"}};
		JTable table1 = new JTable(new InitializedTableModel(tags,data) ),
				table2 = new JTable(new InitializedTableModel(tags,data) ),
				table3 = new JTable((new InitializedTableModel(tags,data) ));
		
		southWestPanel.setLayout(new BoxLayout(southWestPanel, BoxLayout.Y_AXIS));
		northPanel.add(new JScrollPane(table1));
		northPanel.setBorder(BorderFactory.createTitledBorder("Vehicles"));
		centerPanel.add(new JScrollPane(table2));
		centerPanel.setBorder(BorderFactory.createTitledBorder("Roads"));
		southPanel.add(new JScrollPane(table3));
		southPanel.setBorder(BorderFactory.createTitledBorder("Junctions"));
		southWestPanel.add(northPanel);
		southWestPanel.add(centerPanel);
		southWestPanel.add(southPanel);
		
		return southWestPanel;
	}

	public JPanel createSouthEastPanel() {
		return new JPanel();
	}
}
