package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Vehicle;

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
	
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "bike");
    }
}
