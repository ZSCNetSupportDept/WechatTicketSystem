package love.sola.netsupport.api;

import org.junit.Test;
import org.reflections.Reflections;

import java.util.Set;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class ReflectionTest {

	@Test
	public void test() {
		Reflections reflections = new Reflections(getClass().getPackage().getName());
		Set<Class<? extends API>> set = reflections.getSubTypesOf(API.class);
		assert set.size() == 15;
	}

}
