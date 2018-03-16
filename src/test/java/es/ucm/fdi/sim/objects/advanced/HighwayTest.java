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
import es.ucm.fdi.sim.objects.advanced.Highway;

public class HighwayTest {

	@Test
	public void dynamicsTest(){
		Road current;
		RoadMap r = new RoadMap();
		List<Junction> it = new ArrayList<Junction>();
		
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addRoad(new Highway("hw", 1000, 50, r.getJunction("j1"), r.getJunction("j2"), 3));
		it.addAll(Arrays.asList(r.getJunction("j1"), r.getJunction("j2")));
		r.addVehicle(new Vehicle("v1", 50, it));
		r.addVehicle(new Vehicle("v2", 50, it));	
		r.addVehicle(new Vehicle("v3", 50, it));
		current = r.getRoad("hw");
		current.move();
		current.move();	
		assertEquals("Velocity does not match expected.", 50, r.getVehicle("v3").
				getCurrentVelocity());
		
		r.addVehicle(new Vehicle("v4", 50, it));
		current.move();
		assertEquals("Velocity does not match expected.", 38, r.getVehicle("v3").
				getCurrentVelocity());
	}
}

