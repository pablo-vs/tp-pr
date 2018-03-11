package es.ucm.fdi.sim.events;

import org.junit.Test;

import es.ucm.fdi.ini.IniSection;

public class NewJunctionEventTest implements EventTest {
	
	@Test
	public void parseTest(){
		NewJunctionEvent event;
		
		//Wrong section 1 - Wrong title
		IniSection sec = new IniSection("wrong_title");
		
		event = new NewJunctionEvent.Builder().build(sec);
		
		
		//Wrong section 2 - Not all fields
		sec = new IniSection("new_vehicle");
		
		event = new NewJunctionEvent.Builder().build(sec);
		
		//Wrong section 3 - Incorrect format of field
		sec = new IniSection("new_vehicle");
		
		event = new NewJunctionEvent.Builder().build(sec);
		
		//Correct section
		sec = new IniSection("new_vehicle");
	}
	
	@Test
	public void runTest(){
		
	}
}
