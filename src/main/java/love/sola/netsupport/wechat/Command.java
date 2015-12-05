package love.sola.netsupport.wechat;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public enum Command {

	REGISTER(0, ".*"),
	QUERY(1, "Query"),
	SUBMIT(1, "Submit"),
	;

	private static final Map<Integer, Command> ID_MAP = new HashMap<>();

	static {
		for (Command type : values()) {
			if (type.id > 0) {
				ID_MAP.put(type.id, type);
			}
		}
	}

	public final String name;
	public final String regex;
	public final int id;

	Command(int id, String regex) {
		this.name = lang("CMD_" + name());
		this.id = id;
		this.regex = regex;
	}

	public static Command fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() {
		return name;
	}

}
