package ${package};

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ${moduleName}SampleServiceTest {

	private ${moduleName}SampleService fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new  ${moduleName}SampleService();
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
