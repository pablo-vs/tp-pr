package es.ucm.fdi.sim.events;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.sim.objects.RoadMap;
import java.lang.IllegalArgumentException;

public class NewJunctionEventTest implements EventTest {
	
	@Test
	public void parseTest(){
		NewJunctionEvent event, aux;
		
		//Wrong section 1 - Wrong title
		IniSection sec = new IniSection("wrong_title");
		sec.setValue("time", "0");
		sec.setValue("id", "j");
		event = new NewJunctionEvent.Builder().build(sec);
		if(event != null){
			Assert.fail("Invalid title not caught.");
		}else{
			System.err.println("Invalid title successfully caught!");
		}
		
		//Wrong section 2 - Not all fields
		for(int i = 0; i < 2; i++){
			sec = new IniSection("new_junction");
			if(i != 0) sec.setValue("time", "0");
			if(i != 1) sec.setValue("id", "j");
			
			try{
				event = new NewJunctionEvent.Builder().build(sec);
				Assert.fail("Missing field ignored.");
			}catch(IllegalArgumentException e){
				System.err.println("Missing field caught successfully on step " + i + "!");
			}
		}
		
		//Wrong section 3 - Incorrect format of field
		for(int i = 0; i < 2; i++){
			sec = new IniSection("new_junction");
			if(i != 0) sec.setValue("time", "0");
			else sec.setValue("time", "ff");
			
			if(i != 1) sec.setValue("id", "j");
			else sec.setValue("id", ",,j");
			
			try{
				event = new NewJunctionEvent.Builder().build(sec);
				Assert.fail("Invalidly formatted field ignored.");
			}catch(IllegalArgumentException e){
				System.err.println("Invalidly formatted field caught successfully "
						+ "on step " + i + "!");
			}
		}
		
		//Correct section
		sec = new IniSection("new_junction");
		sec.setValue("time", "0");
		sec.setValue("id", "j");
		aux = new NewJunctionEvent(0, "j");
		event = new NewJunctionEvent.Builder().build(sec);
		assertEquals("NewJunctionEvent not correctly instantiated.", aux, event);
	}
	
	@Test
	public void runTest(){
		IniSection sec, report;
		NewJunctionEvent event;
		RoadMap r;
		
		r = new RoadMap();
		
		sec = new IniSection("new_junction");
		sec.setValue("time", "0");
		sec.setValue("id", "j");
			
		event =  new NewJunctionEvent.Builder().build(sec);
		event.execute(r);
		r.addJunction(new Junction("j1"));
		r.addRoad(new Road("r1", 100, 20, r.getJunction("j1"), r.getJunction("j")));
		r.addRoad(new Road("r2", 100, 20, r.getJunction("j"), r.getJunction("j1")));
		
		report = new IniSection("junction_report");
		report.setValue("time", "0");
		report.setValue("id", "j");
		report.setValue("queues", "(r1,red,[])");
		
		assertEquals("Report does not match.", report, r.getJunction("j").report(0));
	}
}
