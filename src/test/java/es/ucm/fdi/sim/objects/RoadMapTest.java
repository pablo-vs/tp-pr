package es.ucm.fdi.sim.objects;

import java.util.List;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.sim.objects.RoadMap;

public class RoadMapTest {

	@Test
	public void insertGetTest() {
		RoadMap map = new RoadMap();
		Junction j1 = new Junction("j1"), j2 = new Junction("j2");
		ArrayList<Junction> it = new ArrayList<>();
		String[] exp = new String[5];
		it.add(j1); it.add(j2);
		map.addJunction(j1);
		map.addJunction(j2);
		for(int i = 0; i < 5; ++i) {
			Road r = new Road("r" + i, 5, 5, j1, j2);
			exp[i] = "r"+i;
			map.addRoad(r);
			Vehicle v = new Vehicle("v" + i, 5, it);
			map.addVehicle(v);
		}

		assertEquals("Road ID not equal to expected", "r3", map.getSimObject("r3").getID());
		assertEquals("Junction ID not equal to expected", "j2", map.getSimObject("j2").getID());
		assertEquals("Vehicle ID not equal to expected", "v3", map.getSimObject("v3").getID());
		assertEquals("Road ID not equal to expected", "r1", map.getRoad("r1").getID());
		assertEquals("Junction ID not equal to expected", "j1", map.getJunction("j1").getID());
		assertEquals("Vehicle ID not equal to expected", "v1", map.getVehicle("v1").getID());

		assertNull(map.getVehicle("j1"));
		assertNull(map.getJunction("j3"));
		
		String[] ids = new String[map.getRoads().size()];
		ArrayList<String> listIds = new ArrayList<>();
		map.getRoads().forEach((o) -> listIds.add(o.getID()));
		ids = listIds.toArray(ids);
		assertArrayEquals("Array not equal to expected", exp, ids);
		
	}

	@Test
	public void errorTest() {
		RoadMap map = new RoadMap();
		Junction j1 = new Junction("j1"), j2 = new Junction("j1");
		map.addJunction(j1);
		try {
			map.addJunction(j2);
			fail("Undetected duplicated ID");
		} catch(IllegalArgumentException e) {
			//So far so good
		}
		try {
			List<Junction> juncts = map.getJunctions();
			juncts.add(j2);
			fail("Modified RO list");
		} catch (UnsupportedOperationException e) {
			//Great
		}
		
	}
}
