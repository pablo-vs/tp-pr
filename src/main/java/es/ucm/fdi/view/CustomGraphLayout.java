package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.util.Random;

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
		generateGraph();
		add(_graphComp, BorderLayout.CENTER);
		setVisible(true);
	}

	public void updateGraph(RoadMap rm){
		Graph g = new Graph();
		
		for(Junction j : rm.getJunctions()){
			g.addNode(new Node(j.getID()));
		}
		
		
		for(Road r : rm.getRoads()){
			int i1, i2;
			
			i1 = g.getNodes().indexOf(r.getIni().getID());
			i2 = g.getNodes().indexOf(r.getEnd().getID());
		 	Edge e = new Edge(r.getID(), g.getNodes().get(i1)
		 			, g.getNodes().get(i2), r.getLength());		 	
		 	
		 	/*for(Vehicle v : r.getVehicles().values()){
			 	e.addDot(id, point);
			}*/
		 	
		 	g.addEdge(e);
		 }
		
		_graphComp.setGraph(g);
	}
	
	
	/**
	 * 
	 */
	protected void generateGraph() {
	    Random _rand = new Random(System.currentTimeMillis());
		Graph g = new Graph();
		int numNodes = _rand.nextInt(20)+5;
		int numEdges = _rand.nextInt(2*numNodes);		
		
		for (int i=0; i<numNodes; i++) {
			g.addNode(new Node("n"+i));
		}
		
		for (int i=0; i<numEdges; i++) {
			int s = _rand.nextInt(numNodes);
			int t = _rand.nextInt(numNodes);
			if ( s == t ) {
				t = (t + 1) % numNodes;
			}
			int l = _rand.nextInt(30)+20;
			Edge e = new Edge("e"+i, g.getNodes().get(s), g.getNodes().get(t), l);
			
			int numDots = _rand.nextInt(5);
			for(int j=0; j<numDots; j++) {
				l = Math.max(0, _rand.nextBoolean() ? l/2 : l);
				e.addDot( new Dot("d"+j, l));
			}
			
			g.addEdge(e);
		}
		
		_graphComp.setGraph(g);

	}
}
