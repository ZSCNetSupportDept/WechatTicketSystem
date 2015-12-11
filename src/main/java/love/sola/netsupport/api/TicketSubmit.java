package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
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
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TicketSubmit", urlPatterns = "/api/ticketsubmit", loadOnStartup = 23)
public class TicketSubmit extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(submit(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response submit(HttpServletRequest request) {
		String desc = request.getParameter("desc");
		if (desc == null) {
			return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		}

		try (Session s = SQLCore.sf.openSession()) {

			HttpSession httpSession = request.getSession(false);
			if (httpSession == null || httpSession.getAttribute("authorized") != Command.SUBMIT) {
				return new Response(Response.ResponseCode.UNAUTHORIZED);
			}
			User u = (User) httpSession.getAttribute("user");
			if (u == null) return new Response(Response.ResponseCode.UNAUTHORIZED);

			long n = (long) s.createCriteria(Ticket.class)
					.add(Restrictions.eq(Ticket.PROPERTY_USER, u))
					.add(Restrictions.eq(Ticket.PROPERTY_STATUS, 0))
					.setProjection(Projections.rowCount())
					.uniqueResult();
			if (n > 0) {
				return new Response(Response.ResponseCode.ALREADY_SUBMITTED);
			}
			Ticket t = new Ticket();
			t.setUser(u);
			t.setDescription(desc);
			t.setStatus(0);
			s.beginTransaction();
			s.save(t);
			s.getTransaction().commit();
			request.getSession().invalidate();
			return new Response(Response.ResponseCode.OK, t);
		} catch (NumberFormatException e) {
			return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
		} catch (HibernateException e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.DATABASE_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.INTERNAL_ERROR, e);
		}
	}


}
