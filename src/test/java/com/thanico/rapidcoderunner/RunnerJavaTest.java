package com.thanico.rapidcoderunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.thanico.rapidcoderunner.ui.lang.RapidCodeRunnerMessages;

public class RunnerJavaTest {

	@Test
	public void testRunCode_withCompiledFalse() {
		RunnerJava runner = new RunnerJava();
		try {
			runner.setCompiled(false);
			runner.runcode();
			fail("This call should fail.");
		} catch (RuntimeException e) {
			assertNotNull("Correct exception but message should not be null.", e.getMessage());
			assertEquals("Correct exception but invalid error message.", RapidCodeRunnerMessages.ERR_CODE_NOT_COMPILED,
					e.getMessage().toString());
		} catch (Exception e) {
			fail("Invalid exception thrown.");
		}
	}

	@Test
	public void testRunCode_withCompiledTrue() {
		RunnerJava runner = new RunnerJava();
		try {
			runner.setCompiled(true);
			runner.runcode();
			fail("This call should fail.");
		} catch (RuntimeException e) {
			assertNotNull("Correct exception but message should not be null.", e.getMessage());
			assertEquals("Correct exception but invalid error message.", RapidCodeRunnerMessages.ERR_CLASS_FILE_MISSING,
					e.getMessage().toString());
		} catch (Exception e) {
			fail("Invalid exception thrown.");
		}
	}

	@Test
	public void testGetJavaRunCommand() {
		RunnerJava runner = new RunnerJava();
		runner.setJavaHome("dummyHome");
		runner.setWorkingDirectory("dummyWd");

		String[] javaRunCommand = runner.getJavaRunCommand();
		assertNotNull("Run command array should not be null", javaRunCommand);
		assertEquals("Run command array size is not valid", 4, javaRunCommand.length);

		assertEquals("Invalid run command parameter", "dummyHome" + RunnerJava.JAVA_PATH, javaRunCommand[0]);
		assertEquals("Invalid run command parameter", "-cp", javaRunCommand[1]);
		assertEquals("Invalid run command parameter", "dummyWd", javaRunCommand[2]);
		assertEquals("Invalid run command parameter", RunnerJava.CODEFILE_NAME, javaRunCommand[3]);
	}

	@Test
	public void testGetJavaCompileCommand() {
		RunnerJava runner = new RunnerJava();
		runner.setJavaHome("dummyHome");
		runner.setWorkingDirectory("dummyWd");

		String[] javaCompileCommand = runner.getJavaCompileCommand();
		assertNotNull("Run command array should not be null", javaCompileCommand);
		assertEquals("Run command array size is not valid", 2, javaCompileCommand.length);

		assertEquals("Invalid run command parameter", "dummyHome" + RunnerJava.JAVAC_PATH, javaCompileCommand[0]);
		assertEquals("Invalid run command parameter",
				"dummyWd" + "/" + RunnerJava.CODEFILE_NAME + RunnerJava.CODEFILE_EXT, javaCompileCommand[1]);
	}

	@Test
	public void testCompileNoCode() {
		RunnerJava runner = new RunnerJava();
		assertFalse("Compilation flag should be false before compilation", runner.isCompiled());

		try {
			runner.compile();
			fail("This call should fail.");
		} catch (RuntimeException e) {
			assertNotNull("Correct exception but message should not be null.", e.getMessage());
			assertTrue("Exception should encapsulate IOException",
					e.getCause() instanceof IOException && e.getCause().getMessage() != null);
			assertTrue("Encapsulated exception should have the correct message",
					e.getCause().getMessage().startsWith("Cannot run program \"null" + RunnerJava.JAVAC_PATH));
		} catch (Exception e) {
			fail("Invalid exception thrown.");
		}

		assertFalse("Compilation flag should be false after compilation failed", runner.isCompiled());
	}
}
