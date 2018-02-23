package es.ucm.fdi.sim.objects;

import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

/**
 * Contains unit tests for es.ucm.fdi.sim.objects.Vehicle
 */
public class VehicleTest {

	/**
	 * Tests building and reporting two different vehicles
	 */
	@Test
	public void buildReportTest() throws Exception {
		String v1report1 = "[vehicle_report]\nid = v1\ntime = 5\nspeed = 0\nkilometrage = 0\nfaulty = 0\nlocation = (r1,0)",
			v2report1 = "[vehicle_report]\nid = v2\ntime = 8\nspeed = 0\nkilometrage = 0\nfaulty = 0\nlocation = (r2,0)";
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
		Road r1 = new Road("r1", 2, 2, j1, j2);
		Road r2 = new Road("r2", 5, 3, j2, j3);
		ArrayList<Junction> it1 = new ArrayList<>(), it2 = new ArrayList<>();

		j1.addRoad(r1);
		j2.addRoad(r2);
		
		it1.add(j1);
		it1.add(j2);
		it2.add(j2);
		it2.add(j3);
		it1.add(j1);

		Vehicle v1 = new Vehicle("v1", 2, it1), v2 = new Vehicle("v2", 30, it2);
		assertEquals("Report does not match", v1report1, v1.generateReport(5));
		assertEquals("Report does not match", v2report1, v2.generateReport(8));
	}

	/**
	 * Tests executing and reporting a whole vehicle itinerary
	 */
	@Test
	public void completeItineraryTest() throws Exception {
		String v1report1 = "[vehicle_report]\nid = v1\ntime = 5\nspeed = 0\nkilometrage = 0\nfaulty = 0\nlocation = (r1,0)",
			v1report2 = "[vehicle_report]\nid = v1\ntime = 6\nspeed = 2\nkilometrage = 2\nfaulty = 0\nlocation = (r1,2)",
			v1report3 = "[vehicle_report]\nid = v1\ntime = 7\nspeed = 0\nkilometrage = 3\nfaulty = 0\nlocation = (r1,3)",
			v1report4 = "[vehicle_report]\nid = v1\ntime = 8\nspeed = 0\nkilometrage = 3\nfaulty = 0\nlocation = (r2,0)",
			v1report5 = "[vehicle_report]\nid = v1\ntime = 9\nspeed = 3\nkilometrage = 6\nfaulty = 0\nlocation = (r2,3)",
			v1report6 = "[vehicle_report]\nid = v1\ntime = 10\nspeed = 0\nkilometrage = 8\nfaulty = 0\nlocation = arrived";
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
		Road r1 = new Road("r1", 3, 2, j1, j2);
		Road r2 = new Road("r2", 5, 3, j2, j3);
		ArrayList<Junction> it1 = new ArrayList<>();

		j1.addRoad(r1);
		j2.addRoad(r1);
		j2.addRoad(r2);
		
		it1.add(j1);
		it1.add(j2);
		it1.add(j3);

		Vehicle v1 = new Vehicle("v1", 3, it1);
		assertEquals("Report does not match", v1report1, v1.generateReport(5));

		r1.move();
		assertEquals("Report does not match", v1report2, v1.generateReport(6));

		r1.move();
		assertEquals("Report does not match", v1report3, v1.generateReport(7));

		j2.move();
		assertEquals("Report does not match", v1report4, v1.generateReport(8));
		
		r2.move();
		assertEquals("Report does not match", v1report5, v1.generateReport(9));
		
		r2.move();

		assertEquals("Report does not match", v1report6, v1.generateReport(10));
		
	}

}

