package es.ucm.fdi.view.customcomponents;

import java.awt.BorderLayout;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JPanel;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;

import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;

/**
 * RoadMap <code>Graph</code> layout.
 */
public class CustomGraphLayout extends JPanel {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = -735338964701982371L;
	private GraphComponent _graphComp;
    
    /**
     * Empty constructor.
     */
	public CustomGraphLayout() {
		super(new BorderLayout());
		_graphComp = new GraphComponent();	
		add(_graphComp, BorderLayout.CENTER);
		setVisible(true);
	}

	public void updateGraph(RoadMap rm){
		Graph g = new Graph();
		Map<Junction, Node> junctionToNode 
			= new HashMap<Junction,Node>();
	
		for(Junction j : rm.getJunctions()){
			Node n = new Node(j.getID());
			g.addNode(n);
			junctionToNode.put(j, n);
		}

		for(Road r : rm.getRoads()){	
		 	Edge e = new Edge(r.getID(), junctionToNode.get(r.getIni())
		 			, junctionToNode.get(r.getEnd()), r.getLength());		 			 	
		 	for(Vehicle v : r.getVehicles().innerValues()){
			 	e.addDot(new Dot(v.getID(), v.getPosition()));
			}
		 	
		 	g.addEdge(e);
		 }
		
		_graphComp.setGraph(g);
	}
}
