package com.thanico.rapidcoderunner.processing.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RapidCodeRunnerUtilsTest {

	@Test
	public void testFormatTime60000ms() {
		String result = RapidCodeRunnerUtils.formatTime(60000);
		assertEquals("Invalid formatting inside method formatTime", "1mn 00sec", result);
	}

	@Test
	public void testFormatTime59999ms() {
		String result = RapidCodeRunnerUtils.formatTime(59999);
		assertEquals("Invalid formatting inside method formatTime", "59sec 999ms", result);
	}

	@Test
	public void testFormatTime10000ms() {
		String result = RapidCodeRunnerUtils.formatTime(10000);
		assertEquals("Invalid formatting inside method formatTime", "10sec 0ms", result);
	}

	@Test
	public void testFormatTime9999ms() {
		String result = RapidCodeRunnerUtils.formatTime(9999);
		assertEquals("Invalid formatting inside method formatTime", "9999ms", result);
	}

	@Test
	public void testFormatTime1000ms() {
		String result = RapidCodeRunnerUtils.formatTime(1000);
		assertEquals("Invalid formatting inside method formatTime", "1000ms", result);
	}
}
