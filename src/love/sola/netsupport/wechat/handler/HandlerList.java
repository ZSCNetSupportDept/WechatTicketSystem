package love.sola.netsupport.wechat.handler;

import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/5.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class HandlerList {

	public static Map<String, Class<? extends WxMpMessageHandler>> handlers = new HashMap<>();

	static {
		handlers.put("Register", RegisterHandler.class);
	}

	public void init(WxMpMessageRouter router) {

	}


}
