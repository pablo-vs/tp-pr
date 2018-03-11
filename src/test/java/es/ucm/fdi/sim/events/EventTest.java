package es.ucm.fdi.sim.events;

import org.junit.Test;

import es.ucm.fdi.ini.IniSection;


interface EventTest{ //Package-protected
	@Test
	public void parseTest();
	
	@Test
	public void runTest();
}
