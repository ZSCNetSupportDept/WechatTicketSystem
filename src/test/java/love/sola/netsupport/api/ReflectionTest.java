package love.sola.netsupport.api;

import org.junit.Test;
import org.reflections.Reflections;

import java.util.Set;

/**
 * ***********************************************
 * Created by Sola on 2016/3/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class ReflectionTest {

	@Test
	public void test() {
		Reflections reflections = new Reflections(getClass().getPackage().getName());
		Set<Class<? extends API>> set = reflections.getSubTypesOf(API.class);
		assert set.size() == 14;
	}

}
