package es.ucm.fdi.sim.events.advanced;

import java.util.List;
import java.util.Map;
import java.lang.IllegalArgumentException;
import java.util.logging.Logger;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.advanced.Car;
import es.ucm.fdi.sim.events.NewVehicleEvent;

/**
 * Represents the New Car event.
 */
public class NewCarEvent extends NewVehicleEvent {
	private int resistance;
	private double faultProb;
	private int faultMax;
	private long seed;

	/**
	 * Empty constructor.
	 */
	public NewCarEvent(){}

	/**
	 * Full constructor.
	 *
	 * @param t Time of the event.
	 * @param v Maximum speed of the car.
	 * @param id ID of the car.x
	 * @param it Itinerary of the car.
	 * @param resist Resistance of the car.
	 * @param prob Probability of failure.
	 * @param faultMax Maximum fault duration.
	 * @param seed Seed for the PRNG.
	 */
	public NewCarEvent(int t, int v, String id, List<String> it, int resist, double prob,
			       int faultMax, long seed){
		super(t, v, id, it);
	        resistance = resist;
		faultProb = prob;
		this.faultMax = faultMax;
		this.seed = seed;
	}
	
	/**
	 * Constructor from NewVehicleEvent.
	 *
	 * @param resist Resistance of the car.
	 * @param prob Probability of failure.
	 * @param faultMax Maximum fault duration.
	 * @param seed Seed for the PRNG.
	 */	 
       	public NewCarEvent(NewVehicleEvent e, int resist, double prob, int faultMax, long seed) {
		super(e);
		resistance = resist;
		faultProb = prob;
		this.faultMax = faultMax;
		this.seed = seed;
	}
	
	/**
	 * Instantiates a new car, given the parameters are valid.
	 * 
	 * @param r The <code>RoadMap</code> of the current simulation.
	 */
	@Override
	public void execute(RoadMap r) throws IllegalArgumentException {
		Logger log = Logger.getLogger(NewCarEvent.class.getName());
		log.info("Attempting to execute NewCarEvent...");
		r.addVehicle(createCar(r));
		log.info("Event executed");
	}

	/**
	 * Creates a new Vehicle with the event parameters, if possible.
	 *
	 * @return The corresponding Vehicle.
	 */
	public Car createCar(RoadMap r) {
		Car result;
		try{
			result = new Car(vehicleID, maxSpeed, createItinerary(r), resistance,
					 faultMax, faultProb, seed);
			
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error: could not create Car.\n"
							   + e.getMessage(), e);
		}
		return result;
	}
	
	/**
	 * Return a  description of the event.
	 *
	 * @param out A <code>Map<String, String></code> which will contain the representation of the event.
	 */
	@Override
	public void describe(Map<String, String> out) {
		super.describe(out);
		out.put("Type", "New Car " + vehicleID);
	}
	   
	/**
	 * Builder for this event.
	 */
	public static class Builder extends NewVehicleEvent.Builder {
		public static final String TYPE = "car";
		
		/**
		 * Build the event from a given INI section, returns null if the section tag does
		 * not match the event tag or the type is incorrect.
		 *
		 * @param section The <code>IniSection</code> from which to parse the event.
		 */
		@Override
		public NewCarEvent build(IniSection ini) throws IllegalArgumentException {
			NewCarEvent event;
			int resistance, faultMax;
		        Double faultProb;
		        long seed;
	
			event = null;
			if(TAG.equals(ini.getTag()) && isCorrectType(ini, TYPE)) {
				try {
					Logger log = Logger.getLogger(NewCarEvent.class.getName());
					log.info("Attempting to parse NewCarEvent...");
					
					NewVehicleEvent ev = super.build(ini);
					resistance = parsePositiveInt(ini, "resistance");
					faultMax = parsePositiveInt(ini, "max_fault_duration");
					faultProb = parseDouble(ini, "fault_probability");
					System.err.println(faultProb);
					if(faultProb.compareTo((double) 0) < 0
					   || faultProb.compareTo((double) 1) > 0) {
						throw new IllegalArgumentException("fault_probability must be in [0,1]");
					}
					seed = parseSeedOrMillis(ini);
						
					event = new NewCarEvent(ev, resistance, faultProb,
								faultMax, seed);
					log.info("Event parsed");
				} catch(Exception e) {
					throw new IllegalArgumentException("Error while parsing event:\n" + e.getMessage(), e);
				}
			}
	
			return event;
		}
	}
}
