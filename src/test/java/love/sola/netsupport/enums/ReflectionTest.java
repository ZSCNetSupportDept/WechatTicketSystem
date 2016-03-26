package love.sola.netsupport.enums;

import org.junit.Test;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class ReflectionTest {

	@Test
	public void testBlock() {
		assert Block.inverseMap != null;
	}

	@Test
	public void testAccess() {
		assert Access.inverseMap != null;
	}

	@Test
	public void testStatus() {
		assert Status.inverseMap != null;
	}

}
