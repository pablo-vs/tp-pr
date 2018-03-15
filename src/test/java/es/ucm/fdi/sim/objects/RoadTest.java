package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.control.Controller;

public class RoadTest {
	
	@Test
	public void dynamicsTest() throws Exception {
		Road r;
		Junction ini, end;
		Vehicle v1,v2,v3,v4;
		IniSection sec, report;
		List<Junction> l = new ArrayList<Junction>();
		
		ini = new Junction("j1");
		end = new Junction("j2");
		l.add(ini);
		l.add(end);
		
		r = new Road("r1",35, 20, ini, end);

		v1 = new Vehicle("v1", 5, l);
		r.move();
		//v1 at 5
		v2 = new Vehicle("v2", 20, l);
		r.move();
		//v1 at 10, v2 at 11
		v1.setBrokenTime(2);
		r.move();
		//v1 at 10, v2 at 22
		v3 = new Vehicle("v3", 20, l);
		r.move();
		//v3 at 3, v1 at 10, v2 at 29
		v4 = new Vehicle("v4", 20, l);
		r.move();
		//v4 at 6, v3 at 9, v1 at 15, v2 out

		sec = new IniSection("");
		sec.setValue("id", "r1");
		sec.setValue("time", "5");
		sec.setValue("state", "(v2,35),(v1,15),(v3,9),(v4,6)");
		
		report = r.report(5);
		assertEquals("Report does not match", sec, report);
	}
	
	@Test
	public void buildReportTest() throws Exception {
		Road r;
		Junction ini, end;
		Vehicle v1,v2;
		IniSection sec, report;
		List<Junction> l = new ArrayList<Junction>();
		
		ini = new Junction("j1");
		end = new Junction("j2");
		l.add(ini);
		l.add(end);
		
		r = new Road("r1",200, 20, ini, end);
		v1 = new Vehicle("v1", 20, l);
		r.move();
		v2 = new Vehicle("v2", 10, l);
		r.move(); //BASE SPEED 11 
		
		//We will check the sec after 4 steps of the simulation
		sec = new IniSection("");
		sec.setValue("id", "r1");
		sec.setValue("time", "4");
		sec.setValue("state", "(v1,31),(v2,10)");
		
		report = r.report(4);
		assertEquals("sec does not match", sec, report);
	}
}
