package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.util.ParseUtil;
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
@WebServlet(name = "Login", urlPatterns = "/api/login", loadOnStartup = 31)
public class Login extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(login(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response login(HttpServletRequest request) {
		String password = request.getParameter("pass");
		if (Checker.hasNull(password)) return new Response(Response.ResponseCode.PARAMETER_REQUIRED);

		WxSession session = Checker.isOperator(request);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		Operator operator = (Operator) session.getAttribute(Attribute.OPERATOR);

		if (!Crypto.check(password,operator.getPassword()))
			return new Response(Response.ResponseCode.WRONG_PASSWORD);
		else
			return new Response(Response.ResponseCode.OK, operator);
	}
}
