package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;

public class JunctionTest {
	
	//NEEDS DYNAMICS TEST (?)
	
	@Test
	public void buildReportTest() throws Exception {
		Junction j, j2, j3;
		IniSection sec;
		Vehicle v1,v2,v3;
		Road r1, r2, r3, r4, r5;
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
		r3 = new Road("r3", 50, 50, j2, j3);
		r4 = new Road("r4", 50, 50, j3, j);
		r5 = new Road("r5", 50, 50, j, j3);
		
		j.addRoad(r1);
		j.addRoad(r2);
		j.addRoad(r4);
		j.addRoad(r5);
		j2.addRoad(r1);
		j2.addRoad(r2);
		j2.addRoad(r3);
		j3.addRoad(r3);
		j3.addRoad(r4);
		j3.addRoad(r5);
		
		v1 = new Vehicle("v1", 100, it1);
		//System.err.println(r2.generateReport(0));
		r2.move();
		//System.err.println(r2.generateReport(0));
		v2 = new Vehicle("v2", 50, it2);
		r4.move();
		v3 = new Vehicle("v3", 100, it1);
		//System.err.println(r2.generateReport(0));
		r2.move();
		//System.err.println(r2.generateReport(0));
			
		sec = new IniSection("junction_report");
		sec.setValue("id", "j");
		sec.setValue("time", "4");
		sec.setValue("incomingRoads", "(r1,green,[]),(r2,red,[v1,v3]),(r4,red,[v2]),(r5,red,[])");
		
		assertEquals("Report does not match", sec, j.generateReport(4));
	}
}
