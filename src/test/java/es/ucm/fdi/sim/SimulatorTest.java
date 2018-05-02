package es.ucm.fdi.sim;

import java.lang.Exception;	
import java.lang.IllegalArgumentException;
import es.ucm.fdi.exceptions.SimulatorException;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.launcher.Main;

public class SimulatorTest{

	@Test
	public void basicTest() throws Exception {
		assertTrue(Main.test("src/main/resources/examples/basic"));
	}

	@Test
	public void advancedTest() throws Exception {
		assertTrue(Main.test("src/main/resources/examples/advanced"));
	}

	@Test(expected = SimulatorException.class )
	public void errorTest() throws Exception {
		Main.test("src/main/resources/examples/err");
	}
}
