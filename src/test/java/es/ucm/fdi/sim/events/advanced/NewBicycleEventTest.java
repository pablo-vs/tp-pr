package es.ucm.fdi.sim.events.advanced;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Assert;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.events.EventTest;
//import es.ucm.fdi.sim.events.adanced.NewBicycleEvent;

public class NewBicycleEventTest implements EventTest{
	
	@Test
	public void parseTest(){
		//We will just test the correct section and the type field missing
		IniSection sec = new IniSection("new_vehicle");
		sec.setValue("time", "5");
		sec.setValue("id", "v1");
		sec.setValue("max_speed", "20");
		sec.setValue("itinerary", "j1,j2,j3");

		//assertEqual with null
		
		sec.setValue("type", "bike");
		
	}
	
	@Test
	public void runTest(){
		//NewBicycleEvent event;	
		IniSection sec = new IniSection("new_vehicle"),
				report = new IniSection("vehicle_report");
	}
}
