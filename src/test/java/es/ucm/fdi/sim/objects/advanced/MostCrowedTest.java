package es.ucm.fdi.sim.objects.advanced;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;


public class MostCrowedTest {

	@Test
	public void trafficLightTest() {

		MostCrowed cr = new MostCrowed("cr");
	
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
	
		Road r1 = new Road("r1", 5, 50, j1, cr), r2 = new Road("r2", 5, 50, j2, cr),
			r3 = new Road("r3", 5, 50, j3, cr);
	
		Vehicle v1_1 = new Vehicle("v1_1", 50, j1, cr), v1_2 = new Vehicle("v1_2", 50, j1, cr),
			v2_1 = new Vehicle("v2_1", 50, j2, cr), v3_1 = new Vehicle("v3_1", 50, j3, cr),
			v3_2 = new Vehicle("v3_2", 50, j3, cr), v3_3 = new Vehicle("v3_3", 50, j3, cr);
	
		r1.move(); r2.move(); r3.move();
		
		assertEquals("Selected incorrect road to update", 2, cr.findRoadToUpdate());

		cr.updateTrafficLights();

		assertEquals("Wrong time interval", 1, cr.getTimeInterval());
		
		cr.move();
		
		assertEquals("Selected incorrect road to update", 0, cr.getCurrentOpenQueue());

		cr.move();

		assertEquals("Selected incorrect road to update", 2, cr.getCurrentOpenQueue());

		cr.move();
		
		assertEquals("Selected incorrect road to update", 0, cr.getCurrentOpenQueue());
		
		cr.move();

		assertEquals("Selected incorrect road to update", 1, cr.getCurrentOpenQueue());

		cr.move();

		assertEquals("Selected incorrect road to update", 2, cr.getCurrentOpenQueue());
	}
}
