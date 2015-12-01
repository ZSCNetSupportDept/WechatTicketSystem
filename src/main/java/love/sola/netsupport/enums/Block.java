package love.sola.netsupport.enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/30.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public enum Block {

	@SerializedName("10")
	QT_18(10),
	@SerializedName("11")
	QT_19(11),
	@SerializedName("12")
	QT_16(12),
	@SerializedName("13")
	QT_17(13),
	@SerializedName("20")
	BM_7(20),
	@SerializedName("21")
	BM_8(21),
	@SerializedName("22")
	BM_9(22),
	@SerializedName("23")
	BM_10(23),
	@SerializedName("24")
	BM_11(24),
	@SerializedName("30")
	DM_12(30),
	@SerializedName("31")
	DM_13(31),
	@SerializedName("32")
	DM_14(32),
	@SerializedName("33")
	DM_15(33),
	@SerializedName("40")
	XH_A(40),
	@SerializedName("41")
	XH_B(41),
	@SerializedName("42")
	XH_C(42),
	@SerializedName("43")
	XH_D(43),
	@SerializedName("50")
	FX_1(50),
	@SerializedName("51")
	FX_2(51),
	@SerializedName("52")
	FX_3(52),
	@SerializedName("53")
	FX_4(53),
	@SerializedName("54")
	FX_5(54),
	@SerializedName("55")
	FX_6(55),
	@SerializedName("60")
	DM_20(60),
	@SerializedName("61")
	DM_21(61),
	;

	private static final Map<String, Block> NAME_MAP = new HashMap<>();
	private static final Map<Integer, Block> ID_MAP = new HashMap<>();

	static {
		for (Block type : values()) {
			if (type.name != null) {
				NAME_MAP.put(type.name.toLowerCase(), type);
			}
			if (type.id > 0) {
				ID_MAP.put(type.id, type);
			}
		}
	}

	public final String name;
	public final int id;

	Block(int id) {
		this.name = lang("BLOCK_" + name());
		this.id = id;
	}

	public static Block fromName(String name) {
		if (name == null) {
			return null;
		}
		return NAME_MAP.get(name.toLowerCase());
	}

	public static Block fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() { return name; }

}
