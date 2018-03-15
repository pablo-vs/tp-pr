package es.ucm.fdi.sim.objects.advanced;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

/**
 * Class that models the behavior of <code>DirtRoad</code> in the simulation.
 * 
 * @version 15.03.2018
 */
public class DirtRoad extends Road{

	public DirtRoad(String id, int l, int maxV, Junction ini, Junction end){
		super(id,l,maxV,ini,end);
	}
	
	/**
	 * Calculates the <code>reductionFactor</code> for this <code>DirtRoad</code>
	 * 
	 * @return The <code>reductionFactor</code> for this <code>DirtRoad</code>.
	 */
	@Override
	public int calculateReductionFactor(int brokenVehicles){
		return 1+brokenVehicles;
	}
	
	/**
	 * Calculates the <code>baseSpeed</code> based on the formula
	 * 
	 * @return The <code>baseSpeed</code> for this <code>DirtRoad</code>.
	 */
	@Override
	public int calculateBaseSpeed(){
		return getMaxVel();
	}
	
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "dirt");
    }
}
