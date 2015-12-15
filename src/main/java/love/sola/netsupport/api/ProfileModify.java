package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
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
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "ProfileModify", urlPatterns = "/api/profilemodify", loadOnStartup = 22)
public class ProfileModify extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(process(request));
		out.println(ParseUtil.parseJsonP(request, json));
		out.close();
	}

	private Response process(HttpServletRequest request) {
		WxSession session = Checker.isAuthorized(request, Command.QUERY);
		if (session == null) {
			return new Response(Response.ResponseCode.UNAUTHORIZED);
		}
		User u = (User) session.getAttribute(Attribute.USER);
		if (u == null) return new Response(Response.ResponseCode.UNAUTHORIZED);
		String isp = request.getParameter("isp");
		String netaccount = request.getParameter("username");
		String block = request.getParameter("block");
		String room = request.getParameter("room");
		String phone = request.getParameter("phone");
		return null;
	}

}
