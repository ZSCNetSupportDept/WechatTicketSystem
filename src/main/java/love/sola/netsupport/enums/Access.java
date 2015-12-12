package love.sola.netsupport.enums;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Access {

	public static final int ROOT = 0;
	public static final int NOLOGIN = 9;

	public static final Map<Integer, String> inverseMap = new HashMap<>();

	static{
		System.out.println("Loading Access...");
		for (Field field : Access.class.getDeclaredFields()) {
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				try {
					inverseMap.put((Integer) field.get(null), field.getName());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getLocalized(int access) {
		if (inverseMap.containsKey(access)) {
			return lang("ACCESS_" + inverseMap.get(access));
		}
		return null;
	}

}
