package love.sola.netsupport.api;

import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class ReflectionTest {

	@Test
	public void test() throws IOException, IllegalAccessException, InstantiationException {
		int count = 0;
		ClassPath path = ClassPath.from(getClass().getClassLoader());
		Set<ClassPath.ClassInfo> classes = path.getTopLevelClassesRecursive(getClass().getPackage().getName());
		for (ClassPath.ClassInfo info : classes) {
			Class<?> clz = info.load();
			if (!API.class.equals(clz) && API.class.isAssignableFrom(clz)) {
				System.out.println("Loading API: " + clz.getName());
				API obj = (API) clz.newInstance();
				System.out.println("Registered API: " + obj);
				count++;
			}
		}
		System.out.println("Total " + count + " API(s) loaded.");
	}

}
