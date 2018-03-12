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
import es.ucm.fdi.exceptions.InvalidEventException;

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
			}catch(InvalidEventException e){
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
			}catch(InvalidEventException e){
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
		MakeVehicleFaultyEventTest event;
		RoadMap r;
		
		
	}
}
