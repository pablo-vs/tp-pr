package es.ucm.fdi.sim.objects.advanced;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.advanced.Car;

public class CarTest {
	private static long seed = 23471488;
	
	@Test
	public void dynamicsTest(){
		Road current;
		RoadMap r = new RoadMap();
		List<Junction> it = new ArrayList<Junction>();

		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		it.addAll(Arrays.asList(r.getJunction("j1"), r.getJunction("j2")));
		r.addRoad(new Road("r1", 50, 500, it.get(0), it.get(1)));	
		r.addVehicle(new Car("c", 20, it, 100, 2, 1.0f, seed));
		
		current = r.getRoad("r1");
		current.move();
		current.move();
		assertFalse("Car should not be broken yet.", r.getVehicle("c").isFaulty());
		current.move();
		assertFalse("Car should be broken.", r.getVehicle("c").isFaulty());
		
	}
}
