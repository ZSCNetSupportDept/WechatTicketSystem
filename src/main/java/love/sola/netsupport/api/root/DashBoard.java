package love.sola.netsupport.api.root;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.InternalSession;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * ***********************************************
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class DashBoard extends API {

	public DashBoard() {
		url = "/api/root/dashboard";
		access = Access.ROOT;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (InternalSession s : WechatSession.list()) {
			sb.append("=====").append(s.getIdInternal()).append("=====\n");
			WxSession ws = s.getSession();
			Enumeration<String> e = ws.getAttributeNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				sb.append(key).append(": ").append(ws.getAttribute(key)).append("\n");
			}
		}
		return sb.toString();
	}

}
