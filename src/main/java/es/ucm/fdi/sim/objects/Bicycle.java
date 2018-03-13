package es.ucm.fdi.sim.objects;

import java.util.List;

public class Bicycle extends Vehicle {

	public Bicycle(String id, int maxVel, List<Junction> itinerary){
		super(id,maxVel,itinerary);	
		
	}
	
	@Override
	public void setBrokenTime(int t){
		if(getCurrentVelocity() > getMaxVel()/2){
			super.setBrokenTime(t);
		}
	}
}
