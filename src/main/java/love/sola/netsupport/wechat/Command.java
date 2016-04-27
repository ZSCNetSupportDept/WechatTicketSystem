package love.sola.netsupport.wechat;

import love.sola.netsupport.wechat.handler.*;
import love.sola.netsupport.wechat.handler.admin.LoginHandler;
import love.sola.netsupport.wechat.handler.admin.OperatorInfoHandler;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public enum Command {

	REGISTER(0, RegisterHandler.class),
	QUERY(1, QueryHandler.class),
	SUBMIT(2, SubmitHandler.class),
	CANCEL(3, CancelHandler.class),
	PROFILE(4, ProfileHandler.class),
	LOGIN(10, LoginHandler.class),
	OPERATOR_INFO(11, OperatorInfoHandler.class),
	;

	private static final Map<Integer, Command> ID_MAP = new HashMap<>();

	static {
		for (Command type : values()) {
			if (type.id >= 0) {
				ID_MAP.put(type.id, type);
			}
		}
	}

	public final String regex;
	public final Class<? extends WxMpMessageHandler> handler;
	public final int id;

	Command(int id, Class<? extends WxMpMessageHandler> handler) {
		this.id = id;
		this.regex = lang("REGEX_" + name());
		this.handler = handler;
	}

	public static Command fromId(int id) {
		return ID_MAP.get(id);
	}

	@Override
	public String toString() {
		return name();
	}

}
