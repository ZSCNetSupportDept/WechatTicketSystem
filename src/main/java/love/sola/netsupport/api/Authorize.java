package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.JsonP;
import love.sola.netsupport.wechat.Command;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ***********************************************
 * Created by Sola on 2015/12/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "Authorize", urlPatterns = "/api/authorize", loadOnStartup = 21)
public class Authorize extends HttpServlet {

	private Gson gson = SQLCore.gson;

	public static Map<String, Long> fetchedTime = new ConcurrentHashMap<>();
	public static Map<String, Command> fetchedCommand = new ConcurrentHashMap<>();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(authorize(request));
		out.println(JsonP.parse(request, json));
		out.close();
	}

	private Response authorize(HttpServletRequest request) {
		String wechat = request.getParameter("wechat");
		if (wechat == null) {
			return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		}
		Long l = fetchedTime.remove(wechat);
		Command c = fetchedCommand.remove(wechat);
		if (l == null || c == null) {
			return new Response(Response.ResponseCode.AUTHORIZE_FAILED);
		}
		if (l < System.currentTimeMillis() - Settings.I.User_Command_Timeout * 1000) {
			return new Response(Response.ResponseCode.REQUEST_EXPIRED);
		}
		switch (c) {
			case REGISTER:
				Register.authorized.put(wechat, System.currentTimeMillis());
				break;
			case QUERY:
				request.getSession(true).setAttribute("wechat", wechat);
				request.getSession(true).setAttribute("wechat", wechat);
			default:
				return new Response(Response.ResponseCode.AUTHORIZE_FAILED);
		}
		return new Response(Response.ResponseCode.OK);
	}

}
