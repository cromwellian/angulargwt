package com.github.h0ru5.gwt.SomeModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SampleServiceTest {

	private SampleService fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new SampleService();
	}

	@Test
	public void testGreet() throws Exception {
		Assert.assertEquals("Hello world",fixture.greet("world"));
	}
	

	@Test
	public void testDismiss() throws Exception {
		Assert.assertEquals("Goodbye world",fixture.dismiss("world"));
	}

}
