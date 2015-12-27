package love.sola.netsupport.api.root;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
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

@WebServlet(name = "Dashboard", urlPatterns = "/api/root/dashboard", loadOnStartup = 51)
public class DashBoard extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		process(request, out);
		out.close();
	}

	private void process(HttpServletRequest request, PrintWriter out) {
		WxSession session = Checker.isAuthorized(request, Command.LOGIN);
		if (session == null) {
			out.println("Unauthorized");
			return;
		}

		Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
		if (op.getAccess() != Access.ROOT) {
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
