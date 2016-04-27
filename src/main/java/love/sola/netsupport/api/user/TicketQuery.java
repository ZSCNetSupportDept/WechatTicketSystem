package love.sola.netsupport.api.user;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketQuery extends API {

	public TicketQuery() {
		url = "/ticketquery";
		access = Access.USER;
		authorize = Command.QUERY;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		try (Session s = SQLCore.sf.openSession()) {
			User u = session.getAttribute(Attribute.USER);
			Criteria c = s.createCriteria(Ticket.class);
			int first = req.getParameter("offset") == null ? 0 : Integer.parseInt(req.getParameter("offset"));
			int limit = req.getParameter("limit") == null ? 5 : Integer.parseInt(req.getParameter("limit"));
			c.setFirstResult(first);
			c.setMaxResults(limit);
			c.addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME));
			c.add(Restrictions.eq(Ticket.PROPERTY_USER, u));
			if (req.getParameter("status") != null) {
				c.add(Restrictions.eq(Ticket.PROPERTY_STATUS, Integer.parseInt(req.getParameter("status"))));
			} else if (req.getParameter("statusl") != null && req.getParameter("statush") != null) {
				c.add(Restrictions.between(Ticket.PROPERTY_STATUS,
						Integer.parseInt(req.getParameter("statusl")),
						Integer.parseInt(req.getParameter("statush"))
				));
			}
			return c.list();
		}
	}

}
