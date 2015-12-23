package love.sola.netsupport.api.root;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2015/12/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "SetPassword",urlPatterns = "/api/admin/setpass",loadOnStartup = 53)
public class SetPassword extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

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

		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		if (pass == null || pass.length() < 8) {
			out.println("Invalid pass");
			return;
		}
		try (Session s = SQLCore.sf.openSession()) {
			s.beginTransaction();
			op = s.get(Operator.class, Integer.parseInt(id));
			if (op == null) {
				out.println("Invalid user");
				return;
			}
			op.setPassword(Crypto.hash(pass));
			s.update(op);
			s.getTransaction().commit();
			out.println("Operation success");
		} catch (NumberFormatException e) {
			out.println("Invalid id");
			return;
		}
	}

}
