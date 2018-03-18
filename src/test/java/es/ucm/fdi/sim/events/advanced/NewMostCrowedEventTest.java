package es.ucm.fdi.sim.events.advanced;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.events.EventTest;
import es.ucm.fdi.sim.objects.RoadMap;

public class NewMostCrowedEventTest implements EventTest{
	
	@Test
	public void parseTest(){
		NewMostCrowedEvent event;
		IniSection sec = new IniSection("new_junction");
		sec.setValue("time", "0");
		sec.setValue("id", "mc");

		assertNull("Parameter not null", new NewMostCrowedEvent.Builder().build(sec));
		//assertEqual with null
		
		sec.setValue("type", "mc");
		event = new NewMostCrowedEvent(0, "mc");
		
		assertEquals("Events do not match", event, new NewMostCrowedEvent.Builder().build(sec));
	}
	
	@Test
	public void runTest(){
		RoadMap r = new RoadMap();
		IniSection sec = new IniSection("new_junction"),
				report = new IniSection("junction_report");	
		
		EventTest.buildAdvancedEventRoadMap(r);
		sec.setValue("time", "0");
		sec.setValue("id", "mc");
		sec.setValue("type", "mc");
		new NewMostCrowedEvent.Builder().build(sec).execute(r);
		report.setValue("id", "mc");
		report.setValue("time", "0");
		report.setValue("queues", "");
		report.setValue("type", "mc");
		assertEquals("Reports do not match", report, r.getJunction("mc").report(0));
	}

}
