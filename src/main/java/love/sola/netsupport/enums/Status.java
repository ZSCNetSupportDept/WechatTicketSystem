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
public class Status {

	public static final int UNCHECKED = 0;
	public static final int ARRANGED = 1;
	public static final int PUTOFF = 2;
	public static final int REPORTED = 4;
	public static final int ISP_HANDLED = 7;
	public static final int SOLVED = 9;

	public static final Map<Integer, String> inverseMap = new HashMap<>();

	static{
		System.out.println("Loading Status...");
		for (Field field : Status.class.getDeclaredFields()) {
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				try {
					inverseMap.put((Integer) field.get(null), field.getName());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getLocalized(int status) {
		if (inverseMap.containsKey(status)) {
			return lang("STATUS_" + inverseMap.get(status));
		}
		return null;
	}

}
