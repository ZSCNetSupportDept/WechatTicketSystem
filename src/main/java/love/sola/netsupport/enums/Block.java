/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.sola.netsupport.enums;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sola {@literal <dev@sola.love>}
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
	public static final int DM_20 = 34;
	public static final int DM_21 = 35;
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
	public static final int BS_1 = 60;
	public static final int BS_2 = 61;
	public static final int BS_3 = 62;
	public static final int BS_4 = 63;
	public static final int BS_5 = 64;
	public static final int BS_6 = 65;
	public static final int BS_7 = 66;
	public static final int BS_8 = 67;
	public static final int BS_9 = 68;
	public static final int ZH = 80;

	public static final Map<Integer, String> inverseMap = new HashMap<>();

	static {
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

	private static final int[][] AVAILABLE = new int[100][0];

	static {
		// @formatter:off
		// -------------------------------------------- //
		// THANKS DATA PROVIDED BY Lai Juncheng
		// -------------------------------------------- //
		AVAILABLE[FX_1]     = new int[]{108, 208, 308, 408, 508};
		AVAILABLE[FX_2]     = new int[]{110, 210, 310, 410, 510, 610};
		AVAILABLE[FX_3]     = new int[]{110, 210, 310, 410, 510, 610};
		AVAILABLE[FX_4]     = new int[]{110, 210, 310, 410, 510, 610, 710};
		AVAILABLE[FX_5]     = new int[]{108, 208, 308, 408, 508, 608, 708};
		AVAILABLE[BM_7]     = new int[]{100, 216, 317, 417, 517, 617, 717};
		AVAILABLE[BM_8]     = new int[]{100, 221, 321, 421, 521, 621, 721};
		AVAILABLE[BM_9]     = new int[]{100, 221, 321, 421, 521, 621};
		AVAILABLE[BM_10]    = new int[]{111, 239, 354, 454, 564, 664, 764, 864};
		AVAILABLE[BM_11]    = new int[]{100, 213, 321, 421, 521, 621, 721, 821};
		AVAILABLE[DM_12]    = new int[]{119, 221, 321, 421, 521, 621, 720};
		AVAILABLE[DM_13]    = new int[]{120, 222, 322, 422, 522, 622, 722};
		AVAILABLE[DM_14]    = new int[]{100, 230, 330, 430, 530, 630, 730};
		AVAILABLE[DM_15]    = new int[]{119, 219, 319, 419, 519, 619};
		AVAILABLE[QT_16]    = new int[]{154, 257, 357, 457, 557, 657, 757};
		AVAILABLE[QT_17]    = new int[]{154, 257, 357, 457, 557, 657, 757};
		AVAILABLE[QT_18]    = new int[]{139, 239, 339, 439, 539, 639, 739};
		AVAILABLE[QT_19]    = new int[]{100, 200, 332, 432, 532, 632, 732};
		AVAILABLE[DM_20]    = new int[]{109, 209, 309, 409, 509, 609, 709};
		AVAILABLE[DM_21]    = new int[]{109, 209, 309, 409, 509, 609, 709};
		AVAILABLE[XH_A]     = new int[]{129, 231, 331, 431, 531, 631, 731, 831, 931, 1031, 1131, 1231};
		AVAILABLE[XH_B]     = new int[]{129, 229, 329, 429, 529, 629, 729, 829, 929, 1029, 1129, 1229};
		AVAILABLE[XH_C]     = new int[]{126, 226, 326, 426, 526, 626, 726, 826, 926, 1026, 1126, 1226};
		AVAILABLE[XH_D]     = new int[]{128, 228, 328, 428, 528, 628, 728, 828, 928, 1028, 1128, 1228};
		AVAILABLE[FX_6]     = new int[0];
		AVAILABLE[BS_1]     = new int[]{102, 203, 301};
		AVAILABLE[BS_2]     = new int[]{102, 203, 301};
		AVAILABLE[BS_3]     = new int[]{103, 203, 302};
		AVAILABLE[BS_4]     = new int[]{102, 203, 301};
		AVAILABLE[BS_5]     = new int[]{102, 203, 301};
		AVAILABLE[BS_6]     = new int[]{102, 203, 302};
		AVAILABLE[BS_7]     = new int[]{102, 203, 301};
		AVAILABLE[BS_8]     = new int[]{102, 203, 301};
		AVAILABLE[BS_9]     = new int[]{103, 203, 302};
		AVAILABLE[ZH]       = new int[]{199, 299, 399, 499, 599, 699, 799, 899, 999, 1099, 1199, 1299, 1399};
		// @formatter:on
	}

	public static boolean checkRoom(int block, int room) {
		int floor = room / 100;
		if (floor == 0 || room % 100 == 0) return false;
		if (block < 0 || block >= AVAILABLE.length) return false;
		if (AVAILABLE[block].length < floor) return false;
		return room <= AVAILABLE[block][floor - 1];
	}

}
