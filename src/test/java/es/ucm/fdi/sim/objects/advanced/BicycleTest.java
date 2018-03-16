package es.ucm.fdi.sim.objects.advanced;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.advanced.Bicycle;
import es.ucm.fdi.sim.events.MakeVehicleFaultyEvent;

public class BicycleTest {
	
	@Test
	public void dynamicsTest(){
		Road current;
		RoadMap r = new RoadMap();
		List<Junction> it = new ArrayList<Junction>();
		List<String> v = new ArrayList<String>();
		v.add("b");
		
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addJunction(new Junction("j3"));
		it.addAll(Arrays.asList(r.getJunction("j1"), r.getJunction("j2"),
				r.getJunction("j3") ));
		r.addRoad(new Road("r1", 20, 10, it.get(0), it.get(1)));
		r.addRoad(new Road("r2", 200, 20, it.get(1), it.get(2)));		
		r.addVehicle(new Bicycle("b", 20, it));
		
		
		current = r.getRoad("r1");
		current.move();
			
		MakeVehicleFaultyEvent event = new MakeVehicleFaultyEvent(0, v, 5);
		event.execute(r);		
		assertFalse("Bicycle should not be broken.", r.getVehicle("b").isFaulty());
		current.move();
		it.get(1).move();
		it.get(1).move();
		
		current = r.getRoad("r2");
		current.move();
		event.execute(r);
		assertTrue("Bicycle should now be broken", r.getVehicle("b").isFaulty());
		
	}
}
