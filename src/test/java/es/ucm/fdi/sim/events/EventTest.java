package es.ucm.fdi.sim.events;

import org.junit.Test;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.sim.objects.Road;
import es.ucm.fdi.sim.objects.RoadMap;
import es.ucm.fdi.sim.objects.Junction;

public interface EventTest{ //Package-protected
	@Test
	public void parseTest();
	
	@Test
	public void runTest();
	
	public static void buildAdvancedEventRoadMap(RoadMap r){
		r.addJunction(new Junction("j1"));
		r.addJunction(new Junction("j2"));
		r.addJunction(new Junction("j3"));
		r.addRoad(new Road("r1", 50, 50, r.getJunction("j1"), r.getJunction("j2")));
	}
}
