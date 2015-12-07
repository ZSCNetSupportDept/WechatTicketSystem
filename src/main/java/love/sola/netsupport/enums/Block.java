package love.sola.netsupport.enums;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/30.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Block {

	public static final int QT_18 = 10;
	public static final int QT_19 = 11;
	public static final int QT_16 = 12;
	public static final int QT_17 = 13;
	public static final int BM_7 = 20;
	public static final int BM_8 = 21;
	public static final int BM_9 = 22;
	public static final int BM_10 = 23;
	public static final int BM_11 = 24;
	public static final int DM_12 = 30;
	public static final int DM_13 = 31;
	public static final int DM_14 = 32;
	public static final int DM_15 = 33;
	public static final int XH_A = 40;
	public static final int XH_B = 41;
	public static final int XH_C = 42;
	public static final int XH_D = 43;
	public static final int FX_1 = 50;
	public static final int FX_2 = 51;
	public static final int FX_3 = 52;
	public static final int FX_4 = 53;
	public static final int FX_5 = 54;
	public static final int FX_6 = 55;
	public static final int DM_20 = 60;
	public static final int DM_21 = 61;

	public static final Map<Integer, String> inverseMap = new HashMap<>();

	static{
		System.out.println("Loading Blocks...");
		for (Field field : Block.class.getDeclaredFields()) {
			if (field.getType().isAssignableFrom(Integer.TYPE)) {
				try {
					inverseMap.put((Integer) field.get(null), field.getName());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
