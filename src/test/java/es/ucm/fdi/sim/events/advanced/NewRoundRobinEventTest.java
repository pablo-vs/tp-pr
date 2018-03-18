package es.ucm.fdi.sim.events.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.events.EventTest;
import es.ucm.fdi.sim.objects.RoadMap;

public class NewRoundRobinEventTest implements EventTest{

	@Test
	public void parseTest(){
		NewRoundRobinEvent event;
		IniSection sec = new IniSection("new_junction");
		sec.setValue("time", "0");
		sec.setValue("id", "round");
		sec.setValue("min_time_slice", "1");
		sec.setValue("max_time_slice", "2");

		assertNull("Parameter not null", new NewRoundRobinEvent.Builder().build(sec));
		//assertEqual with null
		
		sec.setValue("type", "rr");
		event = new NewRoundRobinEvent(0, "round", 1, 2);
		
		assertEquals("Events do not match", event, new NewRoundRobinEvent.Builder().build(sec));
	}
	
	@Test
	public void runTest(){
		RoadMap r = new RoadMap();
		IniSection sec = new IniSection("new_junction"),
				report = new IniSection("junction_report");	
		
		EventTest.buildAdvancedEventRoadMap(r);
		sec.setValue("time", "0");
		sec.setValue("id", "round");
		sec.setValue("type", "rr");
		sec.setValue("min_time_slice", "1");
		sec.setValue("max_time_slice", "2");
		new NewRoundRobinEvent.Builder().build(sec).execute(r);
		report.setValue("id", "round");
		report.setValue("time", "0");
		report.setValue("queues", "");
		report.setValue("type", "rr");
		assertEquals("Reports do not match", report, r.getJunction("round").report(0));
	}
	
}
