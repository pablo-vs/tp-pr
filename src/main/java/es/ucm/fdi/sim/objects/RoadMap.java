package es.ucm.fdi.sim.objects;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

import java.lang.IllegalArgumentException;

/**
 * Class that contains all the <code>SimObject</code> objects in the model and
 * manages addition and deletion of objects.
 *
 * @version 26.02.2018
 */
public class RoadMap {
	// ID Search, unicity
	private Map<String, SimObject> simObjects = new HashMap<String, SimObject>();

	// Real lists
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();

	// Read-only Lists, via Collections.unmodifiableList();
	private List<Junction> junctionsRO = Collections
			.unmodifiableList(junctions);
	private List<Road> roadsRO = Collections.unmodifiableList(roads);
	private List<Vehicle> vehiclesRO = Collections.unmodifiableList(vehicles);

	/**
	 * Gets the object with the given identifier.
	 *
	 * @param id
	 *            Identifier of the object.
	 * @return The object corresponding to the id.
	 */
	public SimObject getSimObject(String id) {
		SimObject object = simObjects.get(id);
		return object;
	}

	/**
	 * Gets the <code>Junction</code> with the given identifier.
	 *
	 * @param id
	 *            Identifier of the <code>Junction</code>.
	 * @return The object corresponding to the id.
	 */
	public Junction getJunction(String id) {
		SimObject result = getSimObject(id);
		if (!(result instanceof Junction)) {
			result = null;
		}
		return (Junction) result;
	}

	/**
	 * Gets the <code>Road</code> with the given identifier.
	 *
	 * @param id
	 *            Identifier of the <code>Road</code>.
	 * @return The object corresponding to the id.
	 */
	public Road getRoad(String id) {
		SimObject result = getSimObject(id);
		if (!(result instanceof Road)) {
			result = null;
		}
		return (Road) result;
	}

	/**
	 * Gets the <code>Vehicle</code> with the given identifier.
	 *
	 * @param id
	 *            Identifier of the <code>Vehicle</code>.
	 * @return The object corresponding to the id.
	 */
	public Vehicle getVehicle(String id) {
		SimObject result = getSimObject(id);
		if (!(result instanceof Vehicle)) {
			result = null;
		}
		return (Vehicle) result;
	}

	// List (Read-only)
	/**
	 * Returns a list containing all the <code>Junctions</code> in the map.
	 *
	 * @return Constant list of <code>Junctions</code>.
	 */
	public List<Junction> getJunctions() {
		return junctionsRO;
	}

	/**
	 * Returns a list containing all the <code>Roads</code> in the map.
	 *
	 * @return Constant list of Roads.
	 */
	public List<Road> getRoads() {
		return roadsRO;
	}

	/**
	 * Returns a list containing all the <code>Vehicles</code> in the map.
	 *
	 * @return Constant list of <code>Vehicles</code>.
	 */
	public List<Vehicle> getVehicles() {
		return vehiclesRO;
	}

	/**
	 * Checks whether an id is in the map.
	 * 
	 * @param id
	 *            The id to check.
	 */
	private boolean checkIdUnicity(String id) {
		return (getSimObject(id) == null);
	}

	// Object insertion (package-protected)
	/**
	 * Adds a <code>Junction</code> to the map.
	 *
	 * @param j
	 *            The <code>Junction</code> to add.
	 */
	public void addJunction(Junction j) throws IllegalArgumentException {
		String id = j.getID();
		if (checkIdUnicity(id)) {
			simObjects.put(id, j);
			junctions.add(j);
		} else {
			throw new IllegalArgumentException("Duplicated id: " + id);
		}
	}

	/**
	 * Adds a <code>Road</code> to the map.
	 *
	 * @param j
	 *            The <code>Road</code> to add.
	 */
	public void addRoad(Road r) throws IllegalArgumentException {
		String id = r.getID();
		if (checkIdUnicity(id)) {
			simObjects.put(id, r);
			roads.add(r);
		} else {
			throw new IllegalArgumentException("Duplicated id: " + id);
		}
	}

	/**
	 * Adds a <code>Vehicle</code> to the map.
	 *
	 * @param j
	 *            The <code>Vehicle</code> to add.
	 */
	public void addVehicle(Vehicle v) throws IllegalArgumentException {
		String id = v.getID();
		if (checkIdUnicity(id)) {
			simObjects.put(id, v);
			vehicles.add(v);
		} else {
			throw new IllegalArgumentException("Duplicated id: " + id);
		}
	}

}
