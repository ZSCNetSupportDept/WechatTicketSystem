package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.InternalSession;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * ***********************************************
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */

@WebServlet(name = "dashboard", urlPatterns = "/api/admin/dashboard", loadOnStartup = 41)
public class DashBoard extends HttpServlet {

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		print(request, out);
		out.close();
	}

	private void print(HttpServletRequest request, PrintWriter out) {
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			out.println("Unauthorized");
			return;
		}
		for (InternalSession s : WechatSession.list()) {
			out.println("=====" + s.getIdInternal() + "=====");
			WxSession ws = s.getSession();
			Enumeration<String> e = ws.getAttributeNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				out.println(key + ": " + ws.getAttribute(key));
			}
		}
	}

}
