package es.ucm.fdi.sim.objects;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import es.ucm.fdi.exceptions.ObjectNotFoundException;
import es.ucm.fdi.exceptions.InvalidIDException;
import es.ucm.fdi.ini.Ini;

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
    public SimObject getSimObject(String id) throws ObjectNotFoundException{
	SimObject object = simObjects.get(id);
	if(object == null) {
	    throw new ObjectNotFoundException("No object found with id " + id);
	}
	return object;
    }
    
    /**
     * Gets the <code>Junction</code> with the given identifier.
     *
     * @param id Identifier of the Junciton.
     * @return The object corresponding to the id.
     */
    public Junction getJunction(String id) {
	SimObject result = getSimObject(id);
	if(!(result instanceof Junction)) {
	    result = null;
	}
	return (Junction)result;
    }
    
    /**
     * Gets the <code>Road</code> with the given identifier.
     *
     * @param id Identifier of the Road.
     * @return The object corresponding to the id.
     */
    public Road getRoad(String id) {
	SimObject result = getSimObject(id);
	if(!(result instanceof Road)) {
	    result = null;
	}
	return (Road)result;
    }
    
    /**
     * Gets the <code>Vehicle</code> with the given identifier.
     *
     * @param id Identifier of the Vehicle.
     * @return The object corresponding to the id.
     */
    public Vehicle getVehicle(String id) {
	SimObject result = getSimObject(id);
	if(!(result instanceof Vehicle)) {
	    result = null;
	}
	return (Vehicle)result;
    }

    //listado (sólo lectura)
    /**
     * Returns a list containing all the <code>Junctions</code> in the map.
     *
     * @return Constant list of Junctions.
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
     * @return Constant list of Vehicles.
     */
    public List<Vehicle> getVehicles() {
	return vehiclesRO;
    }
    
    /**
     * Checks whether an id is in the map.
     * 
     * @param id The id to check.
     */
    private boolean checkIdUnicity(String id) {
	return (getSimObject(id) == null);
    }

    // Object insertion (package-protected.
    /**
     * Adds a <code>Junction</code> to the map.
     *
     * @param j The <code>Junction</code> to add.
     */
    public void addJunction(Junction j) throws InvalidIDException{
	String id = j.getID();
	if(checkIdUnicity(id)) {
	    simObjects.put(id, j);
	    junctions.add(j);
	    junctionsRO.add(j);
	} else {
	    throw new InvalidIDException("Duplicated id: " + id);
	}
    }
    
    /**
     * Adds a <code>Road</code> to the map.
     *
     * @param j The <code>Road</code> to add.
     */
    public void addRoad(Road r) throws InvalidIDException{
	String id = r.getID();
	if(checkIdUnicity(id)) {
	    simObjects.put(id, r);
	    roads.add(r);
	    roadsRO.add(r);
	} else {
	    throw new InvalidIDException("Duplicated id: " + id);
	}
    }

    /**
     * Adds a <code>Vehicle</code> to the map.
     *
     * @param j The <code>Vehicle</code> to add.
     */
    public void addVehicle(Vehicle v) throws InvalidIDException {
	String id = v.getID();
	if(checkIdUnicity(id)) {
	    simObjects.put(id, v);
	    vehicles.add(v);
	    vehiclesRO.add(v);
	} else {
	    throw new InvalidIDException("Duplicated id: " + id);
	}
    }
    
    public Ini report(){
    	//First junctions
    	
    	//Second roads
    	
    	//Third vehciles
    	
    	return null;
    }
}