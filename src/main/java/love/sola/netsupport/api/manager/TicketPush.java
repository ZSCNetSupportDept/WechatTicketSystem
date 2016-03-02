package love.sola.netsupport.api.manager;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/22.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TicketPush extends API {

	public TicketPush() {
		url = "/api/admin/ticketpush";
		access = Access.LEADER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String uid = req.getParameter("uid");
		String desc = req.getParameter("desc");
		if (Checker.hasNull(uid, desc)) {
			return Error.PARAMETER_REQUIRED;
		}
		if (desc.length() > Settings.MAX_DESC_LENGTH) {
			return Error.LENGTH_LIMIT_EXCEEDED;
		}
		Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
		try (Session s = SQLCore.sf.openSession()) {
			s.beginTransaction();
			User u = s.get(User.class, Long.parseLong(uid));
			if (u == null) {
				return Error.USER_NOT_FOUND;
			}
			Ticket t = new Ticket(null, u, desc, null, "Pushed By Admin", null, op, Status.UNCHECKED);
			s.save(t);
			s.getTransaction().commit();
			return t;
		}
	}

}
