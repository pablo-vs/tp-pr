package es.ucm.fdi.sim.objects.advanced;

import java.util.List;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;

public class RoundRobin extends Junction {
	int minTimeSlice, maxTimeSlice;
	
	public RoundRobin(String id, List<Road> incoming, List<Road> outgoing, 
			int min, int max){
		super(id,incoming,outgoing);
		minTimeSlice = min;
		maxTimeSlice = max;
	}
	
	@Override
	protected void updateTrafficLights(){
		
	}
	
	@Override
	public void fillReportDetails(IniSection out) {
    	super.fillReportDetails(out);
    	out.setValue("type", "rr");
    }
}
