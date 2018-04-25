package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;

/**
 * 	Class that models the behavior of <code>Bicycles</code> in the simulation.
 * 
 *	Version 16.03.2018
 */
public class Bicycle extends Vehicle {
	
	/**
	 * Constructor for the <code>Bicycle</code> class.
	 * 
	 * @param id			ID of this <code>Bicycle</code>.
	 * @param maxVel		Maximum velocity of this <code>Bicycle</code>.
	 * @param itinerary		Itinerary of this <code>Bicycle</code>.
	 */
	public Bicycle(String id, int maxVel, List<Junction> itinerary){
		super(id,maxVel,itinerary);	
		
	}

	/**
	 * Adjusted method that sets {@link Vehicle#brokenTime} only if {@link Vehicle#currentVel}
	 * is larger than half of {@link Vehicle#maxVel}.
	 */
	@Override
	public void setBrokenTime(int t){
		if(getCurrentVelocity() > getMaxVel()/2){
			super.setBrokenTime(t);
		}
	}
	
	/**
	 * Adjusted method that adds the type field to the report of this <code>Bicycle</code>.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
		out.setValue("type", "bike");
		super.fillReportDetails(out);
	}
}
