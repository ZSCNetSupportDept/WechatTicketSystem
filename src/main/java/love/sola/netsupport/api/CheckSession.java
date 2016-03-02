package love.sola.netsupport.api;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/12/21.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class CheckSession extends API {

	public CheckSession() {
		url = "/api/checksession";
		access = Access.GUEST;
		authorize = null;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String more = req.getParameter("more");
		Map<String, Object> result = new HashMap<>();
		result.put(Attribute.AUTHORIZED, session.getAttribute(Attribute.AUTHORIZED));
		if (more != null) {
			switch (more) {
				case "1":
					result.put(Attribute.USER, session.getAttribute(Attribute.USER));
					result.put(Attribute.OPERATOR, session.getAttribute(Attribute.OPERATOR));
					break;
			}
		}
		return result;
	}

}
