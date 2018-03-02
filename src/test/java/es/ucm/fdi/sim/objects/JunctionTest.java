package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.control.Controller;

public class JunctionTest {
	
	//NEEDS DYNAMICS TEST (?)
	
	@Test
	public void buildReportTest() throws Exception {
		Junction j, j2, j3;
		IniSection sec, report;
		Vehicle v1,v2,v3;
		Road r1, r2, r3, r4;
		List<Junction> it1, it2;
		
		j = new Junction("j");
		j2 = new Junction("j2");
		j3 = new Junction("j3");
		
		it1 = new ArrayList<Junction>(); //= it3
		it1.add(j2);
		it1.add(j);
		it1.add(j3);
		it2 = new ArrayList<Junction>();
		it2.add(j3);
		it2.add(j);
		it2.add(j2);
		
		r1 = new Road("r1", 50, 50, j, j2);
		r2 = new Road("r2", 50, 100, j2, j);
		r3 = new Road("r3", 50, 50, j3, j);
		r4 = new Road("r4", 50, 50, j, j3);
		
		j.addIncomingRoad(r2);
		j.addIncomingRoad(r3);
		j.addOutgoingRoad(r1);
		j.addOutgoingRoad(r4);
		j2.addIncomingRoad(r1);
		j2.addOutgoingRoad(r2);
		j3.addIncomingRoad(r4);
		j3.addOutgoingRoad(r3);
		
		v1 = new Vehicle("v1", 100, it1);
		r2.move();
		v2 = new Vehicle("v2", 50, it2);
		r3.move();
		v3 = new Vehicle("v3", 100, it1);
		r2.move();
			
		sec = new IniSection("junction_report");
		sec.setValue("id", "j");
		sec.setValue("time", "4");
		sec.setValue("incomingRoads", "(r2,green,[v1,v3]),(r3,red,[v2])");
		
		report = j.report(4);
		assertEquals("Report does not match", sec, report);
	}
}
