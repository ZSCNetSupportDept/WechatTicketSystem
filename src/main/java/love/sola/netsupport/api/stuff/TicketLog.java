package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2015/12/18.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TicketLog extends API {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public TicketLog() {
		url = "/admin/ticketlog";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		int first;
		int limit;
		Date start;
		Date end;
		first = req.getParameter("first") == null ? 0 : Integer.parseInt(req.getParameter("first"));
		limit = req.getParameter("limit") == null ? 20 : Integer.parseInt(req.getParameter("limit"));
		start = req.getParameter("start") == null ? getToday() : dateFormat.parse(req.getParameter("start"));
		end = req.getParameter("end") == null ? getToday() : dateFormat.parse(req.getParameter("end"));
		end = DateUtils.addDays(end, 1);
		try (Session s = SQLCore.sf.openSession()) {
			AuditReader reader = TableTicket.getAuditReader(s);
			return reader.createQuery()
					.forRevisionsOfEntity(Ticket.class, false, true)
					.addOrder(AuditEntity.revisionNumber().desc())
					.add(AuditEntity.revisionProperty("timestamp").between(start.getTime(), end.getTime()))
					.setFirstResult(first)
					.setMaxResults(limit)
					.getResultList();
		}
	}

	private static Date getToday() {
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}

}
