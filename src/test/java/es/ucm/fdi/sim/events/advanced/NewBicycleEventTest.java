package es.ucm.fdi.sim.events.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.events.EventTest;
import es.ucm.fdi.sim.events.advanced.NewBicycleEvent;

public class NewBicycleEventTest implements EventTest{
	
	@Test
	public void parseTest(){
		//We will just test the correct section and the type field missing
		NewBicycleEvent event;
		IniSection sec = new IniSection("new_vehicle");
		List<String> it = new ArrayList<String>();
		sec.setValue("time", "5");
		sec.setValue("id", "v1");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2,j3");

		assertNull("Parameter not null", new NewBicycleEvent.Builder().build(sec));
		//assertEqual with null
		
		sec.setValue("type", "bike");
		it.addAll(Arrays.asList("j1", "j2", "j3"));
		event = new NewBicycleEvent(5, 20, "v1", it);
		
		assertEquals("Events do not match", event, new NewBicycleEvent.Builder().build(sec));
	}
	
	@Test
	public void runTest(){
		RoadMap r = new RoadMap();
		IniSection sec = new IniSection("new_vehicle"),
				report = new IniSection("vehicle_report");	
		
		EventTest.buildAdvancedEventRoadMap(r);
		sec.setValue("time", "0");
		sec.setValue("id", "b");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2");
		sec.setValue("type", "bike");
		new NewBicycleEvent.Builder().build(sec).execute(r);
		report.setValue("id", "b");
		report.setValue("time", "0");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "0");
		report.setValue("faulty", "0");
		report.setValue("location", "(r1,0)");
		report.setValue("type", "bike");
		assertEquals("Reports do not match", report, r.getVehicle("b").report(0));
	}
}
