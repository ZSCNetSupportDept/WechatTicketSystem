package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "GetUser",urlPatterns = "/api/admin/getuser",loadOnStartup = 42)
public class GetUser extends HttpServlet {

	private Gson gson = SQLCore.gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

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
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
		if (op.getAccess() != Access.ROOT) {
			return new Response(Response.ResponseCode.PERMISSION_DENIED);
		}

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		if ((id == null || id.isEmpty()) && (name == null || name.isEmpty())) {
			return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		}
		if (id != null) {
			try {
				User u = TableUser.getById(Long.parseLong(id));
				if (u == null)
					return new Response(Response.ResponseCode.USER_NOT_FOUND);
				else
					return new Response(Response.ResponseCode.OK, u);
			} catch (NumberFormatException e) {
				return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
			}
		} else {
			User u = TableUser.getByName(name);
			if (u == null)
				return new Response(Response.ResponseCode.USER_NOT_FOUND);
			else
				return new Response(Response.ResponseCode.OK, u);
		}
	}

}
