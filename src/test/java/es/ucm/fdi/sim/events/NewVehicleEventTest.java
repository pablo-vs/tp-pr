package es.ucm.fdi.sim.events;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Junction;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.exceptions.InvalidEventException;

public class NewVehicleEventTest implements EventTest{

	@Test
	public void parseTest(){
		NewVehicleEvent event, aux;
		List<String> it = new ArrayList<String>();
		
		//Wrong section 1 - Wrong title
		IniSection sec = new IniSection("wrong_title");
		sec.setValue("time", "5");
		sec.setValue("id", "v1");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2,j3");
		
		event = new NewVehicleEvent.Builder().build(sec);
		if(event != null){
			Assert.fail("Invalid title not caught.");
		}else{
			System.err.println("Invalid title successfully caught!");
		}
		
		
		//Wrong section 2 - Not all fields
		for(int i = 0; i < 3; ++i){
			sec = new IniSection("new_vehicle");
			if(i != 0) sec.setValue("time", "5");
			if(i != 1)sec.setValue("id", "v1");
			if(i != 2)sec.setValue("max_speed", "20");
			if(i != 3) sec.setValue("itinerary", "j1,j2,j3");
			
			try{
				event = new NewVehicleEvent.Builder().build(sec);	
				Assert.fail("Missing fields ignored.");
				
			}catch(InvalidEventException e){
				System.err.println("[Test 1] - "
						+ "Missing field caught successfully on step " + i + "!");
			}	
		}
		
		//Wrong section 3 - Incorrect format of field
		for(int i = 0; i < 3; ++i){
			sec = new IniSection("new_vehicle");
			if(i != 0) sec.setValue("time", "5");
			else sec.setValue("time","shouldFail");
			
			if(i != 1)sec.setValue("id", "v1");
			else sec.setValue("id", "");
			
			if(i != 2)sec.setValue("max_speed", "20");
			else sec.setValue("max_speed", "..invalid_id..");
			
			if(i != 3) sec.setValue("itinerary", "j1,j2,j3");
			else sec.setValue("itinerary", "j1 % %% ).),m");
			
			try{
				event = new NewVehicleEvent.Builder().build(sec);	
				Assert.fail("Missing fields ignored.");
				
			}catch(InvalidEventException e){
				System.err.println("[Test 2] - " 
			+ "Wrong format field caught successfully on step " + i + "!");
			}	
		}
		
		//Correct section
		sec = new IniSection("new_vehicle");
		sec.setValue("time", "5");
		sec.setValue("id", "v1");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2,j3");
		
		it.add("j1");
		it.add("j2");
		it.add("j3");
		event = new NewVehicleEvent.Builder().build(sec);
		aux = new NewVehicleEvent(5, 20, "v1", it);
		assertEquals("NewVehicleEvent not correctly instantiated.", aux, event);
	}
	
	@Test
	public void runTest(){
		NewVehicleEvent event;
		
		//Correct section
		IniSection sec = new IniSection("new_vehicle"),
				report = new IniSection("vehicle_report");
		sec.setValue("time", "0");
		sec.setValue("id", "v");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2");
		
		//Roadmap
		RoadMap r = new RoadMap();
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addRoad(new Road("r", 100, 40, r.getJunction("j1"), r.getJunction("j2")));
		event = new NewVehicleEvent.Builder().build(sec);
		event.execute(r);
		
		report.setValue("id", "v");
		report.setValue("time", "0");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "0");
		report.setValue("faulty", "0");
		report.setValue("location", "(r,0)");
		
		
		assertEquals("Vehicle not correctly instantiated, report does not match.", 
				r.getVehicle("v").report(0), report);
	}
}
