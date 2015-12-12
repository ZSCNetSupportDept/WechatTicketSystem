package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2015/12/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "QueryTicket", urlPatterns = "/api/ticketquery", loadOnStartup = 24)
public class TicketQuery extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(query(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response query(HttpServletRequest request) {
		try (Session s = SQLCore.sf.openSession()) {

			HttpSession httpSession = request.getSession(false);
			if (!Checker.authorized(httpSession, Command.QUERY)) {
				return new Response(Response.ResponseCode.UNAUTHORIZED);
			}
			User u = (User) httpSession.getAttribute("user");
			if (u == null) return new Response(Response.ResponseCode.UNAUTHORIZED);

			Criteria c = s.createCriteria(Ticket.class);
			int first = request.getParameter("offset") == null ? 0 : Integer.parseInt(request.getParameter("offset"));
			int limit = request.getParameter("limit") == null ? 5 : Integer.parseInt(request.getParameter("limit"));
			c.setFirstResult(first);
			c.setMaxResults(limit);
			c.addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME));
			c.add(Restrictions.eq(Ticket.PROPERTY_USER, u));
			if (request.getParameter("status") != null) {
				c.add(Restrictions.eq(Ticket.PROPERTY_STATUS, Integer.parseInt(request.getParameter("status"))));
			} else if (request.getParameter("statusl") != null && request.getParameter("statush") != null) {
				c.add(Restrictions.between(Ticket.PROPERTY_STATUS,
						Integer.parseInt(request.getParameter("statusl")),
						Integer.parseInt(request.getParameter("statush"))
				));
			}
			return new Response(Response.ResponseCode.OK, c.list());
		} catch (NumberFormatException e) {
			return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
		} catch (HibernateException e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.DATABASE_ERROR, e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.INTERNAL_ERROR, e);
		}
	}

}
