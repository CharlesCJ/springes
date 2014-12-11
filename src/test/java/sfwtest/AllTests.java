package sfwtest;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class AllTests extends TestSuite {
	public static Test suite(){
		TestSuite suite = new TestSuite("Test for default package");
		suite.addTestSuite(TestesTemplate.class);
		return suite;
	}
	
}
