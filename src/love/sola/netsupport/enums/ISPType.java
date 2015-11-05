package love.sola.netsupport.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public enum ISPType {


	TELECOM("Telecom", 1),
	UNICOM("Unicom", 2),
	CHINAMOBILE("ChinaMobile", 3),;

	private static final Map<String, ISPType> NAME_MAP = new HashMap<>();
	private static final Map<Integer, ISPType> ID_MAP = new HashMap<>();

	static {
		for (ISPType type : values()) {
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

	ISPType(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public static ISPType fromName(String name) {
		if (name == null) {
			return null;
		}
		return NAME_MAP.get(name.toLowerCase());
	}

	public static ISPType fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() {
		return name;
	}
}
