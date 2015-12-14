package love.sola.netsupport.util;

import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/12.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Checker {

	public static boolean hasNull(Object... v) {
		for (Object o : v) if (o == null) return true;
		return false;
	}

	public static WxSession isAuthorized(HttpServletRequest r, Command c) {
		String t = r.getParameter("token");
		if (t == null || t.isEmpty()) return null;
		WxSession s = WechatSession.get(t, false);
		return s == null ? null : s.getAttribute(Attribute.AUTHORIZED) == c ? s : null;
	}

	public static WxSession isOperator(HttpServletRequest r) {
		String t = r.getParameter("token");
		if (t == null || t.isEmpty()) return null;
		WxSession s = WechatSession.get(t, false);
		return s == null ? null : s.getAttribute(Attribute.OPERATOR) == null ? null : s;
	}

}
