package love.sola.netsupport.wechat;

import love.sola.netsupport.config.Settings;
import me.chanjar.weixin.common.session.StandardSessionManager;
import me.chanjar.weixin.common.session.WxSession;

import java.util.UUID;

/**
 * ***********************************************
 * Created by Sola on 2015/12/14.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class WechatSession {

	private static StandardSessionManager manager;

	static{
		manager = new StandardSessionManager();
		manager.setMaxInactiveInterval(Settings.I.User_Session_Max_Inactive);
	}

	public static WxSession get(String id, boolean create) {
		return manager.getSession(id, create);
	}

	public static WxSession get(String id) {
		return manager.getSession(id);
	}

	public static String genId() {
		return UUID.randomUUID().toString();
	}
}
