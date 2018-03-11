package es.ucm.fdi.sim.objects;

import java.util.List;

public class MostCrowed extends Junction{
	int timeInterval, usedTimeUnits;
	
	public MostCrowed(String id, List<Road> incoming, List<Road> outgoing){
		super(id,incoming,outgoing);
		timeInterval = 0;
		usedTimeUnits = 0;
		
	}
}
