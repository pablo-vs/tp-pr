package es.ucm.fdi.sim.objects.advanced;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;

/**
 * Class that models the behavior of <code>Cars</code> in the simulation.
 */
public class Car extends Vehicle {
	private double faultyProbability;
	private int resistanceKM;
	private int maxFaultyDuration;
	private int lastTraveled = 0;
	private Random prng;
	private long seed;

	/**
	 * Constructor for the class.
	 * 
	 * @param id
	 *            ID of this <code>Car</code>.
	 * @param maxVel
	 *            Maximum velocity of this <code>Car</code>.
	 * @param itinerary
	 *            Itinerary of this <code>Car</code>.
	 * @param resistance
	 *            KM this <code>Car</code> must cover before possibly being
	 *            faulty.
	 * @param faultyDuration
	 *            Time this <code>Car</code> spends broken when it breaks.
	 * @param prob
	 *            Probability for this <code>Car</code> to break.
	 * @param seed
	 *            Seed for the PRNG.
	 */
	public Car(String id, int maxVel, List<Junction> itinerary, int resistance,
			int faultyDuration, double prob, long seed) {
		super(id, maxVel, itinerary);
		resistanceKM = resistance;
		maxFaultyDuration = faultyDuration;
		faultyProbability = prob;
		this.seed = seed;
		prng = new Random(this.seed);
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
			int faultyDuration, double prob) {
		this(id, maxVel, itinerary, resistance, faultyDuration, prob, System
				.currentTimeMillis());
	}

	/**
	 * Adapted method that checks whether this <code>Car</code> can break this
	 * step.
	 */
	@Override
	public void move() {
		if (!isFaulty() && resistanceKM < lastTraveled
				&& prng.nextDouble() < faultyProbability) {
			setBrokenTime(1 + prng.nextInt(maxFaultyDuration));
		}
		super.move();
		if (!isFaulty()) {
			lastTraveled += getCurrentVelocity();
		}
	}

	/**
	 * Adapted setter method for {@link Vehicle#brokenTime}.
	 */
	@Override
	public void setBrokenTime(int t) {
		super.setBrokenTime(t);
		lastTraveled = 0;
	}

	/**
	 * Adapted method that adds the type to the report.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
		out.setValue("type", "car");
		super.fillReportDetails(out);
	}
}
