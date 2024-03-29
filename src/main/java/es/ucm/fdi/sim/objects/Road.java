package es.ucm.fdi.sim.objects;

import java.lang.String;
import java.util.Map;

import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

/**
 * Class that models the general behavior of <code>Roads</code> in the
 * simulation.
 * 
 * @version 13.03.2018
 */
public class Road extends SimObject {

	private static String road_header = "road_report";
	// VehicleList: Position -> List of vehicles
	// VehicleList is inversely ordered
	private MultiTreeMap<Integer, Vehicle> vehicleList;
	private int length;
	private int maxVel;
	private Junction ini;
	private Junction end;
	
	private static final String[] COLUMNS = {"ID", "Source", "Target",
		"Length", "Max Speed", "Vehicles"};

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            ID of the current object.
	 * @param l
	 *            Length of the current <code>Road</code>.
	 * @param maxV
	 *            Maximum velocity of the current <code>Road</code>.
	 * @param ini
	 *            Initial <code>Junction</code> of the current <code>Road</code>
	 *            .
	 * @param end
	 *            Ending <code>Junction</code> of the current <code>Road</code>.
	 */
	public Road(String id, int l, int maxV, Junction ini, Junction end) {
		super(id, road_header);
		length = l;
		maxVel = maxV;
		this.ini = ini;
		this.end = end;
		ini.addOutgoingRoad(this);
		end.addIncomingRoad(this);
		vehicleList = new MultiTreeMap<Integer, Vehicle>(
				(Integer a, Integer b) -> b - a);
	}

	/**
	 * Inserts a <code>Vehicle</code> in the current <code>Road</code>.
	 * 
	 * @param v
	 *            <code>Vehicle</code> to insert in the current
	 *            <code>Road</code>.
	 */
	public void vehicleIn(Vehicle v) {
		vehicleList.putValue(0, v);
	}

	/**
	 * Removes the given <code>Vehicle</code> from the end of the road.
	 * 
	 * @param The
	 *            vehicle to remove
	 */
	public void vehicleOut(Vehicle v) {
		vehicleList.removeValue(length, v);
	}

	/**
	 * Calculates the <code>baseSpeed</code> based on the formula
	 * 
	 * @return The <code>baseSpeed</code> for this <code>Road</code>.
	 */
	public int calculateBaseSpeed() {
		return Math.min(maxVel,
				maxVel / (Math.max((int) vehicleList.sizeOfValues(), 1)) + 1);
	}

	/**
	 * Calculates the <code>reductionFactor</code> for this <code>Road</code>
	 * 
	 * @return The <code>reductionFactor</code> for this <code>Road</code>.
	 */
	public int calculateReductionFactor(int brokenVehicles) {
		if (brokenVehicles == 0) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * Executes a <code>move</code> operation for every object in this
	 * <code>Road</code>.
	 */
	// Invoked by Simulator
	public void move() {
		int baseSpeed = calculateBaseSpeed(), reductionFactor, counter = 0;

		// Store the vehicles in a new map to avoid messing the iterator
		MultiTreeMap<Integer, Vehicle> newVehicleList = new MultiTreeMap<Integer, Vehicle>(
				(Integer a, Integer b) -> b - a);
		for (Vehicle v : vehicleList.innerValues()) {
			// Check if there is a faulty vehicle
			// All vehicles further in the list will be slowed down
			reductionFactor = calculateReductionFactor(counter);
			if (v.isFaulty()) {
				counter++;
			}
			if (v.getPosition() < length) {
				v.setCurrentVel(baseSpeed / reductionFactor);
				v.move();
			}
			newVehicleList.putValue(v.getPosition(), v);
		}
		vehicleList = newVehicleList;
	}

	/**
	 * Getter method for {@link Road#vehicleList}
	 */
	public MultiTreeMap<Integer, Vehicle> getVehicles() {
		return vehicleList;
	}

	/**
	 * Getter method for {@link Road#length}.
	 * 
	 * @return Length of current <code>Road</code>.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Getter method for {@link Road#maxVel}.
	 * 
	 * @return Maximum velocity of current <code>Road</code>.
	 */
	public int getMaxVel() {
		return maxVel;
	}

	/**
	 * Getter method for {@link Road#ini}.
	 * 
	 * @return Initial <code>Junction</code> of current <code>Road</code>.
	 */
	public Junction getIni() {
		return ini;
	}

	/**
	 * Getter method for {@link Road#end}.
	 * 
	 * @return Ending <code>Junction</code> of current <code>Road</code>.
	 */
	public Junction getEnd() {
		return end;
	}

	/**
	 * Return a description of the object.
	 *
	 * @param out
	 *            A <code>Map<String, String></code> which will contain the
	 *            representation of the object.
	 */
	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Source", ini.getID());
		out.put("Target", end.getID());
		out.put("Length", "" + length);
		out.put("Max Speed", "" + maxVel);
		out.put("Vehicles", "[" + describeVehicles() + "]");
	}
	
	public static String[] getColumns() {
		return COLUMNS;
	}

	/**
	 * Fills the given map with the details of the state of the object.
	 *
	 * @param out
	 *            Map to store the report.
	 */
	public void fillReportDetails(IniSection out) {
		out.setValue("state", describeVehicles());
	}

	private String describeVehicles() {
		boolean first = true;
		StringBuilder aux = new StringBuilder(vehicleList.size() * 20);

		for (Vehicle v : vehicleList.innerValues()) {
			if (!first) {
				aux.append(",");
			} else {
				first = false;
			}
			aux.append("(");
			aux.append(v.getID());
			aux.append(",");
			aux.append(v.getPosition());
			aux.append(")");
		}
		return aux.toString();
	}
}
