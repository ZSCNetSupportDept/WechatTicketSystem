package love.sola.netsupport.api.stuff;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.HibernateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * ***********************************************
 * Created by Sola on 2015/12/13.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TicketLookup", urlPatterns = "/api/admin/ticketlookup", loadOnStartup = 33)
public class TicketLookup extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(lookup(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response lookup(HttpServletRequest request) {
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		try {
			Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
			int block;
			if (request.getParameter("block") != null) {
				block = Integer.parseInt(request.getParameter("block"));
			} else {
				block = op.getBlock();
			}
			if (block == 0 && op.getAccess() > Access.LEADER) {
				return new Response(Response.ResponseCode.PERMISSION_DENIED);
			}
			List<Ticket> list = TableTicket.unsolvedByBlock(block);
			return new Response(Response.ResponseCode.OK, list);
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
