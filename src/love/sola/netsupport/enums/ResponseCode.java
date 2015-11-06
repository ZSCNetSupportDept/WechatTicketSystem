package love.sola.netsupport.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public enum ResponseCode {

	OK(0, "OK"),
	PARAMETER_REQUIRED(-1, "Parameter Required"),
	ILLEGAL_PARAMETER(-2, "Illegal parameter"),
	USER_NOT_FOUND(-11, "User not found"),
	;

	private static final Map<Integer, ResponseCode> ID_MAP = new HashMap<>();

	static {
		for (ResponseCode type : values()) {
			if (type.id > 0) {
				ID_MAP.put(type.id, type);
			}
		}
	}

	public final String info;
	public final int id;

	ResponseCode(int id, String info) {
		this.info = info;
		this.id = id;
	}

	public static ResponseCode fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() {
		return info;
	}

}
