package es.ucm.fdi.control;

import java.lang.Exception;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;


import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.Ini;

public class ControllerTest {

	@Test
	public void simulatorIOTest() throws Exception {
		String inputPath = "src/main/resources/examples/sim/input1.ini",
			expectedOutputPath = inputPath + ".eout";
		
		FileInputStream inputStream = new FileInputStream(inputPath);
		Ini output, expectedOutput = new Ini(expectedOutputPath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Controller c = new Controller(inputStream, outputStream);

		c.run(3);

		ByteArrayInputStream redirectToIniStream = new ByteArrayInputStream(outputStream.toByteArray());
		
		output = new Ini(redirectToIniStream);
		
		assertEquals("Output does not match expected", output, expectedOutput);
	}

}
