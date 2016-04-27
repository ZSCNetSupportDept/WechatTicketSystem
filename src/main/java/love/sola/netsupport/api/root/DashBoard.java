package love.sola.netsupport.api.root;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class DashBoard extends API {

	public DashBoard() {
		url = "/root/dashboard";
		access = Access.ROOT;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (love.sola.netsupport.session.WxSession ws : WechatSession.list()) {
			sb.append("=====").append(ws.getId()).append("=====\n");
			Set<String> e = ws.getAttributeNames();
			for (String key : e) {
				sb.append(key).append(": ").append(ws.getAttribute(key).toString()).append("\n");
			}
		}
		return sb.toString();
	}

}
