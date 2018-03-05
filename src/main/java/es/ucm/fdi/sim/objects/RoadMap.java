package es.ucm.fdi.sim.objects;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.exceptions.ObjectNotFoundException;

/**
* Class that contains all the <code>SimObject</code> objects in the model
* and manages addition and deletion of objects.
*
* @version 26.02.2018
*/
public class RoadMap {
	// búsqueda por ids, unicidad
	private Map<String, SimObject> simObjects;

	// listados reales
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();

	// listados read-only, via Collections.unmodifiableList();
	private List<Junction> junctionsRO;
	private List<Road> roadsRO;
	private List<Vehicle> vehiclesRO; 

	// búsqueda por ids, unicidad
	/**
	* Gets the object with the given identifier.
	*
	* @param id Identifier of the object.
	* @return The object corresponding to the id.
	*/
	public SimObject getSimObject(String id) {
		SimObject object = simObjects.get(id);
		if(object == null) {
			throw new ObjectNotFoundException("No object found with id " + id);
		}
		return object;
	}

	public Junction getJunction(String id) {
		SimObject result = getSimObject(id);
		if(!(result instanceof Junction)) {
			throw new ObjectNotFoundException("Object " + id + " is not a Junction");
		}
		return (Junction)result;
	}

	public Road getRoad(String id) {
		SimObject result = getSimObject(id);
		if(!(result instanceof Road)) {
			throw new ObjectNotFoundException("Object " + id + " is not a Road");
		}
		return (Road)result;
	}

	public Vehicle getVehicle(String id) {
		SimObject result = getSimObject(id);
		if(!(result instanceof Vehicle)) {
			throw new ObjectNotFoundException("Object " + id + " is not a Vehicle");
		}
		return (Vehicle)result;
	}

	//listado (sólo lectura)
	public List<Junction> getJunctions() {
		return junctionsRO;
	}

	public List<Road> getRoads() {
		return roadsRO;
	}

	public List<Vehicle> getVehicles() {
		return vehiclesRO;
	}

	private boolean checkIDUnicity(String id) {
		return (getSimObject(id) == null);
	}

	// inserción de objetos
	public void addJunction(Junction j) {
		String id = j.getID();
		if(checkIDUnicity(id)) {
			simObjects.put(id, j);
			junctions.add(j);
			junctionsRO.add(j);
		}
	}

	public void addRoad(Road r) {
		String id = r.getID();
		if(checkIDUnicity(id)) {
			simObjects.put(id, r);
			roads.add(r);
			roadsRO.add(r);
		}
	}

	public void addVehicle(Vehicle v) {
		String id = v.getID();
		if(checkIDUnicity(id)) {
			simObjects.put(id, v);
			vehicles.add(v);
			vehiclesRO.add(v);
		}
	}
}