package love.sola.netsupport.api.manager;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
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
 * Created by Sola on 2015/12/22.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TicketPush",urlPatterns = "/api/admin/ticketpush",loadOnStartup = 42)
public class TicketPush extends HttpServlet{

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(push(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response push(HttpServletRequest request) {
		String uid = request.getParameter("uid");
		String desc = request.getParameter("desc");
		if (Checker.hasNull(uid, desc)) {
			return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		}
		if (desc.length() > Settings.MAX_DESC_LENGTH) {
			return new Response(Response.ResponseCode.LENGTH_LIMIT_EXCEEDED);
		}

		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
		if (op.getAccess() > Access.LEADER) {
			return new Response(Response.ResponseCode.PERMISSION_DENIED);
		}

		try (Session s = SQLCore.sf.openSession()) {
			s.beginTransaction();
			User u = s.get(User.class, Long.parseLong(uid));
			if (u == null) {
				return new Response(Response.ResponseCode.USER_NOT_FOUND);
			}
			Ticket t = new Ticket(null, u, desc, null, "Pushed By Admin", null, op, Status.UNCHECKED);
			s.save(t);
			s.getTransaction().commit();
			return new Response(Response.ResponseCode.OK, t);
		} catch (NumberFormatException e) {
			return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
		} catch (HibernateException e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.DATABASE_ERROR, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.ResponseCode.INTERNAL_ERROR, e.getMessage());
		}
	}

}
