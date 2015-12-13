package love.sola.netsupport.enums;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public enum ISP {

	TELECOM(1, "^1[3|4|5|7|8][0-9]{9}$"),
	UNICOM(2, "ZSZJLAN[0-9]{10}@16900\\.gd"),
	CHINAMOBILE(3, "^1[3|4|5|7|8][0-9]{9}@139\\.gd$"),
	OTHER(4, ".*"),
	;

	private static final Map<String, ISP> NAME_MAP = new HashMap<>();
	private static final Map<Integer, ISP> ID_MAP = new HashMap<>();

	static {
		for (ISP type : values()) {
			if (type.name != null) {
				NAME_MAP.put(type.name.toLowerCase(), type);
			}
			if (type.id > 0) {
				ID_MAP.put(type.id, type);
			}
		}
	}

	public final int id;
	public final String name;
	public final String accountRegex;

	ISP(int id, String accountRegex) {
		this.id = id;
		this.name = lang("ISP_" + name());
		this.accountRegex = accountRegex;
	}

	public static ISP fromName(String name) {
		if (name == null) {
			return null;
		}
		return NAME_MAP.get(name.toLowerCase());
	}

	public static ISP fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() { return name; }

}
