package es.ucm.fdi.sim;

import java.lang.Exception;
import java.lang.IllegalArgumentException;

import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.launcher.ExampleMain;

public class SimulatorTest{

	@Test
	public void basicTest() throws Exception {
		ExampleMain.test("resources/examples/basic");
	}

	//@Test
	public void advancedTest() throws Exception {
		ExampleMain.test("resources/examples/advanced");
	}

	@Test(expected = IllegalArgumentException.class)
	public void errorTest() throws Exception {
		ExampleMain.test("resources/examples/err");
	}
}
