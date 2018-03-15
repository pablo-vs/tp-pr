package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

public class MostCrowed extends Junction{
	int timeInterval, usedTimeUnits;
	
	public MostCrowed(String id, List<Road> incoming, List<Road> outgoing){
		super(id,incoming,outgoing);
		timeInterval = 0;
		usedTimeUnits = 0;
	}
	
	//@Override
	public int findRoadToUpdate(){
		return 0;
	}
	
	@Override
	protected void updateTrafficLights(){
		
	}
	
	@Override
	public void move(){
		super.move();
	}
	
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "mc");
    }
}
