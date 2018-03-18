package es.ucm.fdi.sim.events.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.events.EventTest;
import es.ucm.fdi.sim.objects.RoadMap;

public class NewCarEventTest implements EventTest {
	
	@Test
	public void parseTest(){
		NewCarEvent event;
		IniSection sec = new IniSection("new_vehicle");
		List<String> it = new ArrayList<String>();
		sec.setValue("time", "5");
		sec.setValue("id", "v1");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2,j3");
		sec.setValue("resistance", "100");
		sec.setValue("max_fault_duration", "2");
		sec.setValue("fault_probability", "0.1");
		sec.setValue("seed", "12039");
		
		assertNull("Parameter not null", new NewCarEvent.Builder().build(sec));
		//assertEqual with null
		
		sec.setValue("type", "car");
		it.addAll(Arrays.asList("j1", "j2", "j3"));
		event = new NewCarEvent(5, 20, "v1", it, 100, 0.1f, 2, 12039);
		
		assertEquals("Events do not match", event, new NewCarEvent.Builder().build(sec));
	}
	
	@Test
	public void runTest(){
		RoadMap r = new RoadMap();
		IniSection sec = new IniSection("new_vehicle"),
				report = new IniSection("vehicle_report");	
		
		EventTest.buildAdvancedEventRoadMap(r);
		sec.setValue("time", "0");
		sec.setValue("id", "c");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2");
		sec.setValue("resistance", "100");
		sec.setValue("max_fault_duration", "2");
		sec.setValue("fault_probability", "0.1");
		sec.setValue("seed", "12039");
		sec.setValue("type", "car");
		
		new NewCarEvent.Builder().build(sec).execute(r);
		report.setValue("id", "c");
		report.setValue("time", "0");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "0");
		report.setValue("faulty", "0");
		report.setValue("location", "(r1,0)");
		report.setValue("type", "car");
		assertEquals("Reports do not match", report, r.getVehicle("c").report(0));
	}
}
