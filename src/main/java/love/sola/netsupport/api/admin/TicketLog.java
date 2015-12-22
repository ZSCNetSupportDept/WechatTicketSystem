package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2015/12/18.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TicketLog", urlPatterns = "/api/admin/ticketlog", loadOnStartup = 35)
public class TicketLog extends HttpServlet {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(query(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response query(HttpServletRequest request) {
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		int first;
		int limit;
		Date start;
		Date end;
		try {
			first = request.getParameter("first") == null ? 0 : Integer.parseInt(request.getParameter("first"));
			limit = request.getParameter("limit") == null ? 20 : Integer.parseInt(request.getParameter("limit"));
			start = request.getParameter("start") == null ? getToday() : dateFormat.parse(request.getParameter("start"));
			end = request.getParameter("end") == null ? getToday() : dateFormat.parse(request.getParameter("end"));
			end = DateUtils.addDays(end, 1);
		} catch (ParseException | NumberFormatException e) {
			return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
		}
		try (Session s = SQLCore.sf.openSession()) {
			AuditReader reader = TableTicket.getAuditReader(s);
			Object obj = reader.createQuery()
					.forRevisionsOfEntity(Ticket.class, false, true)
					.addOrder(AuditEntity.revisionNumber().asc())
					.add(AuditEntity.revisionProperty("timestamp").between(start.getTime(), end.getTime()))
					.setFirstResult(first)
					.setMaxResults(limit)
					.getResultList();
			return new Response(Response.ResponseCode.OK, obj);
		} catch (HibernateException e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.DATABASE_ERROR, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.INTERNAL_ERROR, e.getMessage());
		}
	}

	private static Date getToday() {
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}

}
