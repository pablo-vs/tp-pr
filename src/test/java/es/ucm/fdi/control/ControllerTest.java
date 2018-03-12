package es.ucm.fdi.control;

import java.lang.Exception;
import java.util.List;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;


import org.junit.Test;
import static org.junit.Assert.*;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.ini.Ini;

public class ControllerTest {

    @Test
    public void constructorTest() throws Exception {
		//File path constructors
		String inputPathCorrect = "resources/examples/sim/input1.ini", inputPathIncorrect = "thisshouldnotexist.ini",
		    outputPath = "input.ini.out";

		Controller c;
		c = new Controller(inputPathCorrect);
		c = new Controller(inputPathCorrect, outputPath);

		try {
		    //This should throw an exception
		    c = new Controller(inputPathIncorrect);
		    throw new Exception("Either the incorrect path " + inputPathIncorrect +
					" does exist, or there are errors in the code");
		} catch (IOException e) {
		    assertTrue(e instanceof FileNotFoundException);
		}
    }

    @Test
    public void simulatorIOTest() throws Exception {
		String inputPath = "resources/examples/sim/input1.ini", expectedOutputPath = inputPath + ".eout";
		FileInputStream inputStream = new FileInputStream(inputPath);
		Ini output, expectedOutput = new Ini(expectedOutputPath);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		Controller c = new Controller(inputStream, outputStream);

		c.run(3);

		ByteArrayInputStream redirectToIniStream = new ByteArrayInputStream(outputStream.toByteArray());
		
		output = new Ini(redirectToIniStream);
		/*
		List<IniSection> outSections = output.getSections(), expSections = expectedOutput.getSections();
		for(int i = 0; i < outSections.size(); ++i) {
		    System.err.println(outSections.get(i));
		    System.err.println(expSections.get(i));
		    System.err.println(outSections.get(i).equals(expSections.get(i)));
		}*/
		
		assertEquals("Output does not match expected", output, expectedOutput);

		System.out.println("God's in his heaven, all's right with the world");
    }

}
