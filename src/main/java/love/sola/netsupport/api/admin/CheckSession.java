package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/12/21.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "CheckSession", urlPatterns = "/api/checksession", loadOnStartup = 43)
public class CheckSession extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(check(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response check(HttpServletRequest request) {
		String t = request.getParameter("token");
		if (t == null || t.isEmpty()) return new Response(Response.ResponseCode.UNAUTHORIZED);
		WxSession s = WechatSession.get(t, false);
		if (s == null) return new Response(Response.ResponseCode.UNAUTHORIZED);
		String more = request.getParameter("more");
		Map<String, Object> result = new HashMap<>();
		result.put(Attribute.AUTHORIZED, s.getAttribute(Attribute.AUTHORIZED));
		if (more != null){
			switch (more) {
				case "1":
					result.put(Attribute.USER, s.getAttribute(Attribute.USER));
					result.put(Attribute.OPERATOR, s.getAttribute(Attribute.OPERATOR));
					break;
			}
		}
		return new Response(Response.ResponseCode.OK, result);
	}



}
