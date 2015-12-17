package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableOperator;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.util.RSAUtil;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2015/12/12.
 * Don't modify this source without my agreement
 * ***********************************************
 */

@WebServlet(name = "Login", urlPatterns = "/api/admin/login", loadOnStartup = 31)
public class Login extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(login(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response login(HttpServletRequest request) {
		try {
			int oid = Integer.parseInt(request.getParameter("id"));
			String password = request.getParameter("pass");
			boolean bypass = request.getParameter("bypass") != null;
			Operator op = TableOperator.get(oid);
			if (op == null)
				return new Response(Response.ResponseCode.OPERATOR_NOT_FOUND);
			else if (op.getAccess() == Access.NOLOGIN)
				return new Response(Response.ResponseCode.PERMISSION_DENIED);

			if (!Crypto.check(bypass ? password : RSAUtil.decrypt(password), op.getPassword())) {
				return new Response(Response.ResponseCode.WRONG_PASSWORD);
			}

			String sid = WechatSession.genId();
			WxSession session = WechatSession.get(sid, true);
			if (bypass) {
				session.setAttribute(Attribute.AUTHORIZED, Command.fromId(Integer.parseInt(request.getParameter("bypass"))));
			} else {
				session.setAttribute(Attribute.AUTHORIZED, Command.LOGIN);
			}

			session.setAttribute(Attribute.WECHAT, op.getWechat());
			session.setAttribute(Attribute.OPERATOR, op);

			if (request.getParameter("bypassuser") != null) {
				User u = TableUser.getById(Long.parseLong(request.getParameter("bypassuser")));
				session.setAttribute(Attribute.USER, u);
				session.setAttribute(Attribute.WECHAT, u.getWechatId());
			}
			if (request.getParameter("bypasswechat") != null) {
				session.setAttribute(Attribute.WECHAT, request.getParameter("bypasswechat"));
			}

			return new Response(Response.ResponseCode.OK, sid);
		} catch (Exception e) {
			return new Response(Response.ResponseCode.REQUEST_FAILED, e);
		}
	}
}