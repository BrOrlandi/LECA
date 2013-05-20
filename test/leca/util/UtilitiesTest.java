package leca.util;

import junit.framework.TestCase;

public class UtilitiesTest extends TestCase {

	public void testCrypt() {
		String str = "Bruno";
		int x = 7654;
		int code = Utilities.encrypt(str, x);
		int x2 = Utilities.decrypt(str, code);
		assertEquals(x,x2);
	}
}
