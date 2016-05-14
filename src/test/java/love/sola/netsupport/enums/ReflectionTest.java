package love.sola.netsupport.enums;

import org.junit.Test;

/**
 * @author Sola {@literal <dev@sola.love>}
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
