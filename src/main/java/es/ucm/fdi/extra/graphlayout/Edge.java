package es.ucm.fdi.extra.graphlayout;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class Edge {
	private String _id;
	private Node _source;
	private Node _target;
	private int _length;
	private List<Dot> _dots;
	private Color _arrowColor;

	public Edge(String id, Node source, Node target, int length) {
		_source = source;
		_target = target;
		_id = id;
		_length = length;
		_dots = new ArrayList<>();
	}
	
	public Edge(String id, Node source, Node target, int length, Color color) {
	        this(id, source, target, length);
		_arrowColor = color;
	}
	
	public void addDot(Dot e) {
		_dots.add(e);
	}
	public String getId() {
		return _id;
	}
	
	public Node getSource() {
		return _source;
	}
	
	public Node getTarget() {
		return _target;
	}

	public int getLength() {
		return _length;
	}

	public Color getArrowColor() {
		return _arrowColor;
	}
	
	public List<Dot> getDots() {
		return _dots;
	}
}
