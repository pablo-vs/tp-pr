package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;

/**
 * Contains unit tests for es.ucm.fdi.sim.objects.Vehicle
 */
public class VehicleTest {

	/**
	 * Tests building and reporting two different vehicles
	 */
	@Test
	public void buildReportTest() throws Exception {
		String [] expReportStr =
			{"[vehicle_report]",
			 "id = v1",
			 "time = 5",
			 "speed = 0",
			 "kilometrage = 0",
			 "faulty = 0",
			 "location = (r1,0)",
			 "",
			 "[vehicle_report]",
			 "id = v2",
			 "time = 8",
			 "speed = 0",
			 "kilometrage = 0",
			 "faulty = 0",
			 "location = (r2,0)"};
			
		List<IniSection> expReport =
			new Ini(IOUtils.toInputStream(String.join("\n", expReportStr) ,"UTF-8"))
			.getSections();
		
		IniSection v1Report = expReport.get(0), v2Report = expReport.get(1), report;
		
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
		new Road("r1", 2, 2, j1, j2);
		new Road("r2", 5, 3, j2, j3);
		ArrayList<Junction> it1 = new ArrayList<>(), it2 = new ArrayList<>();
		
		it1.add(j1);
		it1.add(j2);
		it2.add(j2);
		it2.add(j3);
		it1.add(j1);
		
		Vehicle v1 = new Vehicle("v1", 2, it1), v2 = new Vehicle("v2", 30, it2);
		report = v1.report(5);
		assertEquals("Report does not match", v1Report, report);

		report = v2.report(8);
		assertEquals("Report does not match", v2Report, report);
	}

	/**
	 * Tests executing and reporting a whole vehicle itinerary
	 */
	
	@Test
	public void dynamicsTest() throws Exception {
		String [] expReportStr =
			{"[vehicle_report]",
			 "id = v1",
			 "time = 5",
			 "speed = 0",
			 "kilometrage = 0",
			 "faulty = 0",
			 "location = (r1,0)",
			 "",
			 "[vehicle_report]",
			 "id = v1",
			 "time = 6",
			 "speed = 2",
			 "kilometrage = 2",
			 "faulty = 0",
			 "location = (r1,2)",
			 "",
			 "[vehicle_report]",
			 "id = v1",
			 "time = 7",
			 "speed = 0",
			 "kilometrage = 3",
			 "faulty = 0",
			 "location = (r1,3)",
			 "",
			 "[vehicle_report]",
			 "id = v1",
			 "time = 8",
			 "speed = 0",
			 "kilometrage = 3",
			 "faulty = 0",
			 "location = (r2,0)",
			 "",
			 "[vehicle_report]",
			 "id = v1",
			 "time = 9",
			 "speed = 3",
			 "kilometrage = 6",
			 "faulty = 0",
			 "location = (r2,3)",
			 "",
			 "[vehicle_report]",
			 "id = v1",
			 "time = 10",
			 "speed = 0",
			 "kilometrage = 8",
			 "faulty = 0",
			 "location = arrived"};

		List<IniSection> expReport =
			new Ini(IOUtils.toInputStream(String.join("\n", expReportStr),"UTF-8"))
			.getSections();
		
		IniSection report;
		IniSection v1Report1 = expReport.get(0), v1Report2 = expReport.get(1),
			v1Report3 = expReport.get(2), v1Report4 = expReport.get(3),
			v1Report5 = expReport.get(4), v1Report6 = expReport.get(5);
		
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
		Road r1 = new Road("r1", 3, 2, j1, j2);
		Road r2 = new Road("r2", 5, 3, j2, j3);
		ArrayList<Junction> it1 = new ArrayList<>();
		
		it1.add(j1);
		it1.add(j2);
		it1.add(j3);

		Vehicle v1 = new Vehicle("v1", 3, it1);

		report = v1.report(5);
		assertEquals("Report does not match", v1Report1, report);

		r1.move();
		report = v1.report(6);
		assertEquals("Report does not match", v1Report2, report);

		r1.move();
		report = v1.report(7);
		assertEquals("Report does not match", v1Report3, report);

		j2.move();
		j2.move();
		report = v1.report(8);
		assertEquals("Report does not match", v1Report4, report);
		
		r2.move();
		report = v1.report(9);
		assertEquals("Report does not match", v1Report5, report);
		
		r2.move();
		j3.move();
		j3.move();
		report = v1.report(10);
		assertEquals("Report does not match", v1Report6, report);
		
	}

}

