package es.ucm.fdi.sim.objects;

import java.util.List;

public class RoundRobin extends Junction {
	int minTimeSlice, maxTimeSlice;
	
	public RoundRobin(String id, List<Road> incoming, List<Road> outgoing, 
			int min, int max){
		super(id,incoming,outgoing);
		minTimeSlice = min;
		maxTimeSlice = max;
	}
}
