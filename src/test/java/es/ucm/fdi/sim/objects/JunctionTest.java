package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.control.Controller;

public class JunctionTest {
	
	@Test
	public void completeMovementTest() throws Exception {
		Junction j1, j2, j3, j4, j;
		Road r1,r2,r3,r4;
		Road or1,or2,or3,or4;
		Vehicle v1_r1, v2_r1, v1_r2, v1_r3, v1_r4;
		List<Junction> itinerary;
		IniSection report;
		String aux = "";
		
		j1 = new Junction("j1");
		j2 = new Junction("j2");
		j3 = new Junction("j3");
		j4 = new Junction("j4");
		j = new Junction("j");
		
		r1 = new Road("r1", 20, 50, j1, j);
		r2 = new Road("r2", 50, 100, j2, j);
		r3 = new Road("r3", 50, 50, j3, j);
		r4 = new Road("r4", 50, 50, j4, j);
		
		or1 = new Road("or1", 50, 50, j, j1);
		or2 = new Road("or2", 50, 100, j, j2);
		or3 = new Road("or3", 50, 50, j, j3);
		or4 = new Road("or4", 50, 50, j, j4);
		
		//it v1_r1
		itinerary = new ArrayList<Junction>();
		itinerary.add(j1);
		itinerary.add(j);
		itinerary.add(j3);
		v1_r1 = new Vehicle("v1_r1", 100, itinerary);
		//it v2_r1
		v2_r1 = new Vehicle("v2_r1", 50, itinerary);		
		//it v1_r2
		itinerary = new ArrayList<Junction>();
		itinerary.add(j2);
		itinerary.add(j);
		itinerary.add(j1);
		v1_r2 = new Vehicle("v1_r2", 50, itinerary);
		//it v1_r3
		itinerary = new ArrayList<Junction>();
		itinerary.add(j3);
		itinerary.add(j);
		itinerary.add(j4);
		v1_r3 = new Vehicle("v1_r3", 50, itinerary);
		//it v1_r4
		itinerary = new ArrayList<Junction>();
		itinerary.add(j4);
		itinerary.add(j);
		itinerary.add(j2);
		v1_r4 = new Vehicle("v1_r4", 50, itinerary);

		String [] reportString =
			{"[junction_report]",
			 "id = j",
			 "time = ",
			 "queues = "},
			queuesString =
			{"(r1,red,[v1_r1,v2_r1]),(r2,red,[v1_r2]),(r3,red,[v1_r3]),(r4,red,[v1_r4])",
			 "(r1,green,[v1_r1,v2_r1]),(r2,red,[v1_r2]),(r3,red,[v1_r3]),(r4,red,[v1_r4])",
			 "(r1,red,[v2_r1]),(r2,green,[v1_r2]),(r3,red,[v1_r3]),(r4,red,[v1_r4])",
			 "(r1,red,[v2_r1]),(r2,red,[]),(r3,green,[v1_r3]),(r4,red,[v1_r4])"};
		
		r1.move();
		r2.move();
		r3.move();
		r4.move();

		for(int i = 0; i < 4; ++i) {
			reportString[2] = "time = " + i;
			reportString[3] = "queues = " + queuesString[i];
			report = new Ini(IOUtils.toInputStream(String.join("\n", reportString), "UTF-8"))
				.getSections().get(0);
			
			assertEquals("Report does not match", report, j.report(i));
			j.move();
		}
	}
	
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

		v1 = new Vehicle("v1", 100, it1);
		r2.move();
		v2 = new Vehicle("v2", 50, it2);
		r3.move();
		v3 = new Vehicle("v3", 100, it1);
		r2.move();
			
		sec = new IniSection("junction_report");
		sec.setValue("id", "j");
		sec.setValue("time", "4");
		sec.setValue("queues", "(r2,red,[v1,v3]),(r3,red,[v2])");
		
		report = j.report(4);
		assertEquals("Report does not match", sec, report);
	}
}
