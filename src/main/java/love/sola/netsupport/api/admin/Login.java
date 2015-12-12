package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.util.ParseUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

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
		String wechat = request.getParameter("wechat");
		String opId = request.getParameter("op");
		String password = request.getParameter("pass");
		if (Checker.nonNull(wechat, opId, password)) return new Response(Response.ResponseCode.PARAMETER_REQUIRED);

		try (Session s = SQLCore.sf.openSession()) {
			Operator operator = s.get(Operator.class, Integer.parseInt(opId));
			if (operator == null || operator.getAccess() == Access.NOLOGIN)
				return new Response(Response.ResponseCode.OPERATOR_NOT_FOUND);
			if (!wechat.equals(operator.getWechat()))
				return new Response(Response.ResponseCode.INCORRECT_WECHAT);
			if (!Crypto.check(password,operator.getPassword()))
				return new Response(Response.ResponseCode.WRONG_PASSWORD);

			HttpSession httpSession = request.getSession(true);
			httpSession.setAttribute("wechat", wechat);
			httpSession.setAttribute("operator", operator);
			return new Response(Response.ResponseCode.OK);
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
