package love.sola.netsupport.api.user;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TicketSubmit extends API {

	public TicketSubmit() {
		url = "/ticketsubmit";
		access = Access.USER;
		authorize = Command.SUBMIT;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String desc = req.getParameter("desc");
		if (desc == null || desc.isEmpty()) {
			return Error.PARAMETER_REQUIRED;
		}
		if (desc.length() > Settings.MAX_DESC_LENGTH) {
			return Error.LENGTH_LIMIT_EXCEEDED;
		}

		try (Session s = SQLCore.sf.openSession()) {
			User u = (User) session.getAttribute(Attribute.USER);
			if (TableTicket.hasOpen(u)) {
				session.invalidate();
				return Error.ALREADY_SUBMITTED;
			}
			Ticket t = new Ticket();
			t.setUser(u);
			t.setDescription(desc);
			t.setStatus(0);
			s.beginTransaction();
			s.save(t);
			s.getTransaction().commit();
			session.invalidate();
			return Error.OK;
		}
	}
}
