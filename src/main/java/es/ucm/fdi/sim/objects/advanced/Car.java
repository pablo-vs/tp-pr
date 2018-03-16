package es.ucm.fdi.sim.objects.advanced;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;

/**
 *	Class that models the behavior of <code>Cars</code> in the simulation.
 */
public class Car extends Vehicle {
	private double faultyProbability;
	private int resistanceKM, maxFaultyDuration, lastTraveled; 
	private Random prng;
	
	/**
	 * Constructor for the class.
	 * 
	 * @param id				ID of this <code>Car</code>.
	 * @param maxVel			Maximum velocity of this <code>Car</code>.
	 * @param itinerary			Itinerary of this <code>Car</code>.
	 * @param resistance		KM this <code>Car</code> must cover before possibly being 
	 * 							faulty.
	 * @param faultyDuration	Time this <code>Car</code> spends broken when it breaks.
	 * @param prob				Probability for this <code>Car</code> to break.
	 * @param seed				Seed for the PRNG.
	 */
	public Car(String id, int maxVel, List<Junction> itinerary, int resistance, 
			int faultyDuration, double prob, long seed){
		super(id, maxVel, itinerary);
		resistanceKM = resistance;
		maxFaultyDuration = faultyDuration;
		faultyProbability = prob;
	    prng = new Random(seed);
		lastTraveled = 0;
	}
	
	/**
	 * 
	 * @param id
	 * @param maxVel
	 * @param itinerary
	 * @param resistance
	 * @param faultyDuration
	 * @param prob
	 */
	public Car(String id, int maxVel, List<Junction> itinerary, int resistance, 
			int faultyDuration, double prob){
		super(id, maxVel, itinerary);
		resistanceKM = resistance;
		maxFaultyDuration = faultyDuration;
		faultyProbability = prob;
	    prng = new Random(System.currentTimeMillis()); //VS DOING IT IN EVENTS
		lastTraveled = 0;
	}
	
	/**
	 * Adapted method that checks whether this <code>Car</code> can break this step.
	 */
	@Override
    public void move(){
		if(!isFaulty() && resistanceKM < lastTraveled 
				&& prng.nextDouble() < faultyProbability){
			setBrokenTime(1+prng.nextInt(maxFaultyDuration-1));
		}
		super.move();
		if(!isFaulty()){
			lastTraveled += getCurrentVelocity();
		}
	}
	
	/**
	 * 	Adapted setter method for {@link Vehicle#brokenTime}.
	 */
	@Override
    public void setBrokenTime(int t){
		super.setBrokenTime(t);
			lastTraveled = 0;
	}
	
	/**
	 * Adapted method that adds the type to the report.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "car");
    }
}
