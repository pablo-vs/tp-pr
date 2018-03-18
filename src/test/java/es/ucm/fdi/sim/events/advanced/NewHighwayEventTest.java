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

public class NewHighwayEventTest implements EventTest{

	@Test
	public void parseTest(){
		NewHighwayEvent event;
		IniSection sec = new IniSection("new_road");
		List<String> it = new ArrayList<String>();
		sec.setValue("time", "5");
		sec.setValue("id", "dr");
		sec.setValue("src", "j1");
		sec.setValue("dest", "j2");
		sec.setValue("max_speed","20");
		sec.setValue("length","40");
		sec.setValue("lanes","3");
		
		assertNull("Parameter not null", new NewHighwayEvent.Builder().build(sec));
		//assertEqual with null
		sec.setValue("type", "lanes");
		
		it.addAll(Arrays.asList("j1", "j2", "j3"));
		event = new NewHighwayEvent(0, "dr", "j1", "j2", 20, 40, 3);
		
		assertEquals("Events do not match", event, new NewHighwayEvent.Builder().build(sec));
	}
	
	@Test
	public void runTest(){
		RoadMap r = new RoadMap();
		IniSection sec = new IniSection("new_road"), 
				report = new IniSection("road_report");
		
		EventTest.buildAdvancedEventRoadMap(r);
		sec.setValue("time", "5");
		sec.setValue("id", "dr");
		sec.setValue("src", "j1");
		sec.setValue("dest", "j2");
		sec.setValue("max_speed","20");
		sec.setValue("length","40");
		sec.setValue("type", "lanes");
		sec.setValue("lanes", "3");
		new NewHighwayEvent.Builder().build(sec).execute(r);
		
		report.setValue("time", "0");
		report.setValue("id", "dr");
		report.setValue("state", "");
		report.setValue("type", "lanes");
		
		assertEquals("Reports do not match", report, r.getRoad("dr").report(0));
	}
	
}
