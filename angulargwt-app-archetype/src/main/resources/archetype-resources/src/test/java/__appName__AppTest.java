package ${package};

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.angular.client.AngularModule;

public class ${appName}AppTest {

	private ${appName}App fixture;

	@Before
	public void setUp() throws Exception {
		fixture=new ${appName}App();
	}

	@Test
	public void testMain() throws Exception {
		// needs GWTMockito as test-time dep
		//AngularModule[] res = fixture.main();
		//assertNotNull(res);
		//assertArrayEquals(new AngularModule[]{${appName}Module.class}, res);
	}

}
