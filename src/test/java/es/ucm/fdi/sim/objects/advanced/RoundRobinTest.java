package es.ucm.fdi.sim.objects.advanced;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.advanced.RoundRobin;

public class RoundRobinTest {
	
	@Test
	public void dynamicsTest(){
		RoadMap r = new RoadMap();
		List<Junction> it = new ArrayList<Junction>();
		Junction current;
		
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addJunction(new RoundRobin("rr", 2, 4));
		r.addRoad(new Road("r1", 50, 500, r.getJunction("j1"), r.getJunction("rr")));
		r.addRoad(new Road("r2", 50, 500, r.getJunction("rr"), r.getJunction("j2")));
		current = r.getJunction("rr");
		current.move();
		for(int i=0;i<7;++i){
			current.move();
		}
		assertEquals("Time interval does not match.", 2, 
				(int)((RoundRobin)current).getIntervals().get(0));
		
		it.addAll(Arrays.asList(r.getJunction("j1"), r.getJunction("rr"), r.getJunction("j2")));
		r.addVehicle(new Vehicle("v1", 50, it));
		r.addVehicle(new Vehicle("v2", 50, it));
		r.addVehicle(new Vehicle("v3", 50, it));
		r.getRoad("r1").move();
		
		for(int i=0;i<2;++i){
			current.move();
		}
		assertEquals("Time interval does not match.", 3, 
				(int)((RoundRobin)current).getIntervals().get(0));
		
	}
}
