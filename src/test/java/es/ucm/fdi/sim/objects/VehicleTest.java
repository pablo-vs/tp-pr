package es.ucm.fdi.sim.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.control.Controller;

/**
 * Contains unit tests for es.ucm.fdi.sim.objects.Vehicle
 */
public class VehicleTest {

	/**
	 * Tests building and reporting two different vehicles
	 */
	@Test
	public void buildReportTest() throws Exception {
		IniSection v1Report, v2Report;
		IniSection report;
		Junction j1 = new Junction("j1"), j2 = new Junction("j2"), j3 = new Junction("j3");
		Road r1 = new Road("r1", 2, 2, j1, j2);
		Road r2 = new Road("r2", 5, 3, j2, j3);
		ArrayList<Junction> it1 = new ArrayList<>(), it2 = new ArrayList<>();
		
		it1.add(j1);
		it1.add(j2);
		it2.add(j2);
		it2.add(j3);
		it1.add(j1);
		
		v1Report = new IniSection("vehicle_report");
		v1Report.setValue("id", "v1");
		v1Report.setValue("time", "5");
		v1Report.setValue("speed", "0");
		v1Report.setValue("kilometrage", "0");
		v1Report.setValue("faulty", "0");
		v1Report.setValue("location", "(r1,0)");
		v2Report = new IniSection("vehicle_report");
		v2Report.setValue("id", "v2");
		v2Report.setValue("time", "8");
		v2Report.setValue("speed", "0");
		v2Report.setValue("kilometrage", "0");
		v2Report.setValue("faulty", "0");
		v2Report.setValue("location", "(r2,0)");
		
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
	public void completeItineraryTest() throws Exception {
		IniSection report;
		IniSection v1Report1, v1Report2, v1Report3, v1Report4, v1Report5, v1Report6;
		
		v1Report1 = new IniSection("vehicle_report");
		v1Report1.setValue("id", "v1");
		v1Report1.setValue("time", "5");
		v1Report1.setValue("speed", "0");
		v1Report1.setValue("kilometrage", "0");
		v1Report1.setValue("faulty", "0");
		v1Report1.setValue("location", "(r1,0)");
		v1Report2 = new IniSection("vehicle_report");
		v1Report2.setValue("id", "v1");
		v1Report2.setValue("time", "6");
		v1Report2.setValue("speed", "2");
		v1Report2.setValue("kilometrage", "2");
		v1Report2.setValue("faulty", "0");
		v1Report2.setValue("location", "(r1,2)");
		v1Report3 = new IniSection("vehicle_report");
		v1Report3.setValue("id", "v1");
		v1Report3.setValue("time", "7");
		v1Report3.setValue("speed", "0");
		v1Report3.setValue("kilometrage", "3");
		v1Report3.setValue("faulty", "0");
		v1Report3.setValue("location", "(r1,3)");
		v1Report4 = new IniSection("vehicle_report");
		v1Report4.setValue("id", "v1");
		v1Report4.setValue("time", "8");
		v1Report4.setValue("speed", "0");
		v1Report4.setValue("kilometrage", "3");
		v1Report4.setValue("faulty", "0");
		v1Report4.setValue("location", "(r2,0)");
		v1Report5 = new IniSection("vehicle_report");
		v1Report5.setValue("id", "v1");
		v1Report5.setValue("time", "9");
		v1Report5.setValue("speed", "3");
		v1Report5.setValue("kilometrage", "6");
		v1Report5.setValue("faulty", "0");
		v1Report5.setValue("location", "(r2,3)");
		v1Report6 = new IniSection("vehicle_report");
		v1Report6.setValue("id", "v1");
		v1Report6.setValue("time", "10");
		v1Report6.setValue("speed", "0");
		v1Report6.setValue("kilometrage", "8");
		v1Report6.setValue("faulty", "0");
		v1Report6.setValue("location", "arrived");
		
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
		report = v1.report(8);
		assertEquals("Report does not match", v1Report4, report);
		
		r2.move();
		report = v1.report(9);
		assertEquals("Report does not match", v1Report5, report);
		
		r2.move();
		j3.move();
		report = v1.report(10);
		assertEquals("Report does not match", v1Report6, report);
		
	}

}

