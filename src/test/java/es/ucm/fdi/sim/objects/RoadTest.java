package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;

public class RoadTest {
	
	//NEED A MOVEMENT TEST
	
	@Test
	public void buildReportTest() throws Exception {
		Road r;
		Junction ini, end;
		Vehicle v1,v2;
		IniSection report;
		//String reportModel; Might be unnecessary?
		List<Junction> l = new ArrayList<Junction>();
		
		//Weird, for initializing the junction, we need to initialize the road and viceversa
		ini = new Junction("j1");
		end = new Junction("j2");
		l.add(ini);
		l.add(end);
		
		r = new Road("r1",200, 20, ini, end);
		ini.addOutgoingRoad(r);
		v1 = new Vehicle("v1", 20, l);
		r.move();
		v2 = new Vehicle("v2", 10, l);
		r.move(); //BASE SPEED 11 
		
		//We will check the report after 4 steps of the simulation
		report = new IniSection("");
		report.setValue("id", "r1");
		report.setValue("time", "4");
		report.setValue("state", "(v1,31),(v2,10)");
		
		assertEquals("Report does not match", report, r.generateReport(4));
	}
}
