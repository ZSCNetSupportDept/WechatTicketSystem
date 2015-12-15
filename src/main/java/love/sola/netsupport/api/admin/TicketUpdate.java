package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2015/12/13.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TicketUpdate", urlPatterns = "/api/admin/ticketupdate", loadOnStartup = 32)
public class TicketUpdate extends HttpServlet {

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
		String json = gson.toJson(update(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response update(HttpServletRequest request) {
		String ticket = request.getParameter("ticket");
		String remark = request.getParameter("remark");
		String status = request.getParameter("status");
		if (Checker.hasNull(ticket, remark, status)) return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		try (Session s = SQLCore.sf.openSession()) {
			Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
			Ticket t = s.get(Ticket.class, Integer.parseInt(ticket));
			if (t == null) {
				return new Response(Response.ResponseCode.TICKET_NOT_FOUND);
			}
			t.setOperator(op);
			t.setRemark(remark);
			t.setStatus(Integer.parseInt(status));
			s.beginTransaction();
			s.update(t);
			s.getTransaction().commit();
			return new Response(Response.ResponseCode.OK, t);
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
