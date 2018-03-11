package es.ucm.fdi.sim.objects;

import java.util.List;

public class Car extends Vehicle {
	double faultyProbability;
	int resistanceKM, maxFaultyDuration; 
	long seed;
	
	
	public Car(String id, int maxVel, List<Junction> itinerary, int resistance, 
			int faultyDuration, double prob, long seed){
		super(id, maxVel, itinerary);
		resistanceKM = resistance;
		maxFaultyDuration = faultyDuration;
		faultyProbability = prob;
		this.seed = seed;
	}
	
}
