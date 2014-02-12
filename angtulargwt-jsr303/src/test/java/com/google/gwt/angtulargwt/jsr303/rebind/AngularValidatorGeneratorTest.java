package com.google.gwt.angtulargwt.jsr303.rebind;

import static org.junit.Assert.assertEquals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gwt.angtulargwt.jsr303.rebind.test.TipModel;
import com.google.gwt.angtulargwt.jsr303.rebind.test.TopModel;
import com.google.gwt.dev.util.collect.Lists;

public class AngularValidatorGeneratorTest {

	private AngularValidatorGenerator fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new AngularValidatorGenerator();
	}

	@Test
	public void testCreateAnnotation() throws Exception {
		List<Class<?>> input = new LinkedList<Class<?>>();
		assertEquals("@GwtValidation({})", fixture.createAnnotation(input));
		input.add(TipModel.class);
		assertEquals("@GwtValidation({" + "TipModel.class" + "})", fixture.createAnnotation(input));
		input.add(TopModel.class);
		assertEquals("@GwtValidation({TipModel.class,TopModel.class})", fixture.createAnnotation(input));
	}

	@Test
	public void testWriteImpl() throws Exception {
		String expected="package com.google.gwt.angtulargwt.jsr303;\n" + 
				"\n" + 
				"import javax.validation.Validator;\n" + 
				"import com.google.gwt.validation.client.GwtValidation;\n" + 
				"import com.google.gwt.angtulargwt.jsr303.rebind.test.TipModel;\n" + 
				"import com.google.gwt.angtulargwt.jsr303.rebind.test.TopModel;\n" + 
				"\n" + 
				"@GwtValidation({TipModel.class,TopModel.class})\n" + 
				"public interface AngularValidatorImpl extends javax.validation.Validator {\n" + 
				"}";
		StringWriter sw = new StringWriter(); 
		PrintWriter pw = new PrintWriter(sw);
		
		List<Class<?>> input = new LinkedList<Class<?>>();
		input.add(TipModel.class);
		input.add(TopModel.class);
		
		fixture.writeImpl(null, pw, input);
		String generated = sw.toString();
		
		System.out.println(generated);
		assertEquals(expected, generated);
		
	}
}
