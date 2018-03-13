package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.Random;

/**
 * 
 *
 */
public class Car extends Vehicle {
	double faultyProbability;
	int resistanceKM, maxFaultyDuration, lastTraveled; 
	Random prng;
	
	/**
	 * 
	 * @param id
	 * @param maxVel
	 * @param itinerary
	 * @param resistance
	 * @param faultyDuration
	 * @param prob
	 * @param seed
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
	 * 
	 */
	@Override
    public void setBrokenTime(int t){
		super.setBrokenTime(t);
			lastTraveled = 0;
	}
}
