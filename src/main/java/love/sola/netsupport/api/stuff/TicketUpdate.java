package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2015/12/13.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TicketUpdate extends API {

	public TicketUpdate() {
		url = "/admin/ticketupdate";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String ticket = req.getParameter("ticket");
		String remark = req.getParameter("remark");
		String status = req.getParameter("status");
		if (Checker.hasNull(ticket, remark, status)) return Error.PARAMETER_REQUIRED;
		try (Session s = SQLCore.sf.openSession()) {
			Operator op = session.getAttribute(Attribute.OPERATOR);
			Ticket t = s.get(Ticket.class, Integer.parseInt(ticket));
			if (t == null) {
				return Error.TICKET_NOT_FOUND;
			}
			t.setOperator(op);
			t.setRemark(remark);
			t.setStatus(Integer.parseInt(status));
			t.setUpdateTime(new Date());
			s.beginTransaction();
			s.update(t);
			s.getTransaction().commit();
			return t;
		}
	}
}
