package com.google.gwt.angular.rebind;

import org.junit.Test;

import static org.junit.Assert.*;

public class FilterGeneratorTest {

	@Test
	public void testDetermineName() throws Exception {
		assertEquals("Bla",FilterGenerator.determineName("BlaFilter", null));
		assertEquals("Bla",FilterGenerator.determineName("Bla", null));
		assertNull(FilterGenerator.determineName(null, null));
	}
	

}
