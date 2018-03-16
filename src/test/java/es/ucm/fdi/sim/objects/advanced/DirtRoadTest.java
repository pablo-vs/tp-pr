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
import es.ucm.fdi.sim.objects.advanced.DirtRoad;
import es.ucm.fdi.sim.events.MakeVehicleFaultyEvent;

public class DirtRoadTest {

	@Test
	public void dynamicsTest(){
		Road current;
		RoadMap r = new RoadMap();
		List<Junction> it = new ArrayList<Junction>();
		MakeVehicleFaultyEvent event;
		
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addRoad(new DirtRoad("dirt", 1000, 50, r.getJunction("j1"), r.getJunction("j2")));
		current = r.getRoad("dirt");
		
		it.addAll(Arrays.asList(r.getJunction("j1"), r.getJunction("j2")));
		r.addVehicle(new Vehicle("v1", 50, it));
		current.move();
		assertEquals("Velocities do not match.", r.getVehicle("v1").getCurrentVelocity(), 50);
		r.addVehicle(new Vehicle("v2", 50, it));
		current.move();		
		r.addVehicle(new Vehicle("v3", 50, it));
		current.move();
		
		event = new MakeVehicleFaultyEvent(4, Arrays.asList("v1", "v2"), 10);
		event.execute(r);
		current.move();
		
		assertEquals("Reduction factor not correct, vehicle speed not adequate", 16,
				r.getVehicle("v3").getCurrentVelocity());
	}
}
