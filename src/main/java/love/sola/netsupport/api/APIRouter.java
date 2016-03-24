package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.wechat.WechatSession;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.HibernateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2016/2/27.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "APIRouter", urlPatterns = "/api/*", loadOnStartup = 11)
public class APIRouter extends HttpServlet {

	protected static Gson gson = SQLCore.gson;
	private Map<String, API> nodes = new HashMap<>();

	public APIRouter() {
		try {
//			ClassPath path = ClassPath.from(getServletContext().getClassLoader());
//			Set<ClassPath.ClassInfo> classes = path.getTopLevelClasses();
//			for (ClassPath.ClassInfo info : classes) {
			for (Class<?> clz : API.LIST) {
//				Class<?> clz = info.load();
				if (!API.class.equals(clz) && API.class.isAssignableFrom(clz)) {
					try {
						System.out.print("Loading API: " + clz.getName());
						API obj = (API) clz.newInstance();
						System.out.println("Registered API: " + obj);
						nodes.put(obj.url, obj);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Total " + nodes.size() + " API(s) loaded.");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.addHeader("Content-type", "application/json;charset=utf-8");
		resp.addHeader("Access-Control-Allow-Origin", "*");
		Object obj = null;
		try {
			API api = nodes.get(req.getPathInfo());
			if (api == null) {
				obj = nodes;
//				resp.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			WxSession session = getSession(req);
			if (session == null) {
				obj = Error.UNAUTHORIZED;
				return;
			}
			if (api.authorize != null) {
				if (session.getAttribute(Attribute.AUTHORIZED) != api.authorize) {
					obj = Error.UNAUTHORIZED;
					return;
				}
				if (api.access == Access.USER) {
					User u = (User) session.getAttribute(Attribute.USER);
					if (u == null) {
						obj = Error.UNAUTHORIZED;
						return;
					}
				}
				if (api.access < Access.USER) {
					Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
					if (op == null) {
						obj = Error.UNAUTHORIZED;
						return;
					}
					if (op.getAccess() > api.access) {
						obj = Error.PERMISSION_DENIED;
						return;
					}
				}
			}
			obj = api.process(req, session);
		} catch (ParseException | NumberFormatException e) {
			obj = Error.ILLEGAL_PARAMETER;
		} catch (HibernateException e) {
			e.printStackTrace();
			obj = Error.DATABASE_ERROR;
		} catch (Exception e) {
			e.printStackTrace();
			obj = Error.INTERNAL_ERROR;
		} finally {
			if (!resp.isCommitted()) {
				try (PrintWriter out = resp.getWriter()) {
					out.println(gson.toJson(obj));
				}
			}
		}
	}

	private static WxSession getSession(HttpServletRequest req) {
		String t = req.getParameter("token");
		if (t == null || t.isEmpty()) return null;
		return WechatSession.get(t, false);
	}

}
