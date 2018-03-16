package es.ucm.fdi.sim.events;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.Vehicle;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;
import java.lang.IllegalArgumentException;

public class MakeVehicleFaultyEventTest implements EventTest {
	
	@Test
	public void parseTest(){
		MakeVehicleFaultyEvent event, aux;
		List<String> vehicles = new ArrayList<String>();
		
		
		//Wrong section 1 - Wrong title
		IniSection sec = new IniSection("wrong_title");
		sec.setValue("time", "0");
		sec.setValue("vehicles", "v1");
		sec.setValue("duration", "5");
		event = new MakeVehicleFaultyEvent.Builder().build(sec);
		if(event != null){
			Assert.fail("Invalid title ignored.");
		}else{
			System.err.println("Invalid title successfully caught");
		}
		
		//Wrong section 2 - Not all fields
		for(int i = 0; i < 3; i++){
			sec = new IniSection("make_vehicle_faulty");
			if(i != 0) sec.setValue("time", "0");
			if(i != 1) sec.setValue("vehicles", "v1");
			if(i != 2) sec.setValue("duration", "5");
			
			try{
				event = new MakeVehicleFaultyEvent.Builder().build(sec);	
				Assert.fail("Missing field ignored.");
			}catch(IllegalArgumentException e){
				System.err.println("Missing field caught successfully on step " + i + "!");
			}
		}
		
		//Wrong section 3 - Incorrect format of field
		for(int i = 0; i < 3; i++){
			sec = new IniSection("make_vehicle_faulty");
			if(i != 0) sec.setValue("time", "0");
			else sec.setValue("time", "..0");
			
			if(i != 1) sec.setValue("vehicles", "v1");
			else sec.setValue("vehicles", "v1,;");
			//Needs to be tested with ,,. Check also pattern compiler
			
			if(i != 2) sec.setValue("duration", "5");
			else sec.setValue("duration", "5,");
			
			try{
				event = new MakeVehicleFaultyEvent.Builder().build(sec);	
				Assert.fail("Invalidly formatted field ignored.");
			}catch(IllegalArgumentException e){
				System.err.println("Invalidly formatted field caught successfully on step " 
									+ i + "!");
			}
		}
		
		//Correct section
		sec = new IniSection("make_vehicle_faulty");
		sec.setValue("time", "0");
		sec.setValue("vehicles", "v1");
		sec.setValue("duration", "5");
		event = new MakeVehicleFaultyEvent.Builder().build(sec);
		vehicles.add("v1");
		aux = new MakeVehicleFaultyEvent(0, vehicles, 5);
		assertEquals("MakeVehicleFaultyEvent not properly instantiated.", aux, event);
	}
	
	@Test
	public void runTest(){
		IniSection sec, report;
		MakeVehicleFaultyEvent event;
		RoadMap r;
		List<Junction> it;
		
		r = new RoadMap();
		r.addJunction(new Junction("j"));
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		
		r.addRoad(new Road("r1", 200, 20, r.getJunction("j1"), r.getJunction("j")));
		r.addRoad(new Road("r2", 200, 20, r.getJunction("j2"), r.getJunction("j")));
		
		it = new ArrayList<Junction>();
		it.add(r.getJunction("j1"));
		it.add(r.getJunction("j"));
		r.addVehicle(new Vehicle("v1", 40, it));
		r.getRoad("r1").move();
		r.getRoad("r1").move();
		r.addVehicle(new Vehicle("v2", 40, it));
		r.getRoad("r1").move();
		
		it = new ArrayList<Junction>();
		it.add(r.getJunction("j2"));
		it.add(r.getJunction("j"));
		r.addVehicle(new Vehicle("v3", 200, it));
		
		sec = new IniSection("make_vehicle_faulty");
		sec.setValue("time", "4");
		sec.setValue("vehicles", "v1,v3");
		sec.setValue("duration", "2");
		event = new MakeVehicleFaultyEvent.Builder().build(sec);
		event.execute(r);
		r.getRoad("r1").move();
		
		report = new IniSection("vehicle_report");
		report.setValue("id", "v1");
		report.setValue("time", "4");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "51"); //SHOULD BE 50 ¿?¿?
		report.setValue("faulty", "1");
		report.setValue("location", "(r1,51)");
		assertEquals("Report does not match.", report, r.getVehicle("v1").report(4));
		
		report = new IniSection("vehicle_report");
		report.setValue("id", "v2");
		report.setValue("time", "4");
		report.setValue("speed", "5");
		report.setValue("kilometrage", "16");
		report.setValue("faulty", "0");
		report.setValue("location", "(r1,16)");
		assertEquals("Report does not match.", report, r.getVehicle("v2").report(4));
		
		
		report = new IniSection("vehicle_report");
		report.setValue("id", "v3");
		report.setValue("time", "4");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "0");
		report.setValue("faulty", "2");
		report.setValue("location", "(r2,0)");
		assertEquals("Report does not match.", report, r.getVehicle("v3").report(4));
		
		sec = new IniSection("make_vehicle_faulty");
		sec.setValue("time", "4");
		sec.setValue("vehicles", "v2");
		sec.setValue("duration", "4");
		event = new MakeVehicleFaultyEvent.Builder().build(sec);
		event.execute(r);
		
		report = new IniSection("vehicle_report");
		report.setValue("id", "v2");
		report.setValue("time", "4");
		report.setValue("speed", "0");
		report.setValue("kilometrage", "16");
		report.setValue("faulty", "4");
		report.setValue("location", "(r1,16)");
		assertEquals("Report does not match.", report, r.getVehicle("v2").report(4));
	}
}
