package es.ucm.fdi.sim.events;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.exceptions.InvalidEventException;

public class NewRoadEventTest implements EventTest {
	
	@Test
	public void parseTest(){
		NewRoadEvent event, aux;
		
		
		//Wrong section 1 - Wrong title
		IniSection sec = new IniSection("wrong_title");
		sec.setValue("time", "0");
		sec.setValue("id", "r");
		sec.setValue("src", "j1");
		sec.setValue("dest", "j2");
		sec.setValue("max_speed", "10");
		sec.setValue("length", "100");
		event = new NewRoadEvent.Builder().build(sec);
		if(event != null){
			Assert.fail("Invalid title not caught.");
		}else{
			System.err.println("Invalid title successfully caught!");
		}
		
		//Wrong section 2 - Not all fields
		for(int i = 0; i < 6; i++){
			sec = new IniSection("new_road");
			if(i != 0) sec.setValue("time", "0");
			if(i != 1) sec.setValue("id", "r");
			if(i != 2) sec.setValue("src", "j1");
			if(i != 3) sec.setValue("dest", "j2");
			if(i != 4) sec.setValue("max_speed", "10");
			if(i != 5) sec.setValue("length", "100");
			
			try{
				event = new NewRoadEvent.Builder().build(sec);
				Assert.fail("Missing field ignored in step " + i + ".");
			} catch(InvalidEventException e){
				System.err.println("[Test 1] - "
						+ "Missing field caught successfully on step " + i + "!");
			}
		}
		
		//Wrong section 3 - Incorrect format of field
		for(int i = 0; i < 6; i++){
			sec = new IniSection("new_road");
			if(i != 0) sec.setValue("time", "0");
			else sec.setValue("time", "ff");
			
			if(i != 1) sec.setValue("id", "r");
			else sec.setValue("id", ",r,");
			
			if(i != 2) sec.setValue("src", "j1");
			else sec.setValue("src", ",,r");
			
			if(i != 3) sec.setValue("dest", "j2");
			else sec.setValue("dest", ",,j2");
			
			if(i != 4) sec.setValue("max_speed", "10");
			else sec.setValue("max_speed", "ff");
			
			if(i != 5) sec.setValue("length", "100");
			else sec.setValue("length", "ff");
			
			try{
				event = new NewRoadEvent.Builder().build(sec);
				Assert.fail("Invalidly formatted field ignored.");
			} catch(InvalidEventException e){
				System.err.println("[Test 1] - "
						+ "Missing field caught successfully on step " + i + "!");
			}
		}
		
		//Correct section
		sec = new IniSection("new_road");
		sec.setValue("time", "0");
		sec.setValue("id", "r");
		sec.setValue("src", "j1");
		sec.setValue("dest", "j2");
		sec.setValue("max_speed", "10");
		sec.setValue("length", "100");
		event = new NewRoadEvent.Builder().build(sec);
		aux = new NewRoadEvent(0, "r", "j1", "j2", 10, 100);
		assertEquals("NewVehicleEvent not correctly instantiated.", aux, event);
	}
	
	@Test
	public void runTest(){
		IniSection sec, report;
		NewRoadEvent event;
		RoadMap r;
		
		sec = new IniSection("new_road");
		sec.setValue("time", "0");
		sec.setValue("id", "r");
		sec.setValue("src", "j1");
		sec.setValue("dest", "j2");
		sec.setValue("max_speed", "10");
		sec.setValue("length", "100");
		
		r = new RoadMap();
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		
		report = new IniSection("road_report");
		report.setValue("id", "r");
		report.setValue("time", "0");
		report.setValue("state", "");
		event = new NewRoadEvent.Builder().build(sec);
		event.execute(r);
		
		assertEquals("Report does not match.", report, r.getRoad("r").report(0));
	}
}
