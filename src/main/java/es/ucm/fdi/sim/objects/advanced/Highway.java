package es.ucm.fdi.sim.objects.advanced;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

/**
 * Class that models the behavior of <code>Highways</code> in the simulation.
 * 
 * @version 15.03.2018
 */
public class Highway extends Road{
	private int lanes;
	
	public Highway(String id, int l, int maxV, Junction ini, Junction end, int nLanes){
		super(id,l,maxV,ini,end);
		lanes = nLanes;
	}
	
	/**
	 * Calculates the <code>baseSpeed</code> based on the formula
	 * 
	 * @return The <code>baseSpeed</code> for this <code>Highway</code>.
	 */
	@Override
	public int calculateBaseSpeed(){
		return Math.min(getMaxVel(), 
				getMaxVel()*lanes/(Math.max((int)getVehicles().sizeOfValues(), 1)) + 1);
	}
	
	/**
	 * Calculates the <code>reductionFactor</code> for this <code>Highway</code>
	 * 
	 * @return The <code>reductionFactor</code> for this <code>Highway</code>.
	 */
	@Override
	public int calculateReductionFactor(int brokenVehicles){
		if(brokenVehicles < lanes){
			return 1;
		}else{
			return 2;
		}
	}
	
	/**
	 * Adapted method that adds the type to the report.
	 */
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "lanes");
    }
}
