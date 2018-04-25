package es.ucm.fdi.control;

import java.io.IOException;
import java.lang.Exception;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;


import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.Ini;

public class ControllerTest {

	@Test
	public void constructorTest() throws Exception {
		//File path constructors
		String inputPathCorrect = "src/main/resources/examples/sim/input1.ini",
			inputPathIncorrect = "thisshouldnotexist.ini",
			outputPath = "input.ini.out";

		Controller c = new Controller(inputPathCorrect);
		c = new Controller(inputPathCorrect, outputPath);

		try {
			//This should throw an exception
			c = new Controller(inputPathIncorrect);
			Assert.fail("Either the incorrect path " + inputPathIncorrect
				    + " does exist, or there are errors in the code");
		} catch (IOException e) {
			System.err.println("Exception caught correctly, the path is not valid.");
		}
	}

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
