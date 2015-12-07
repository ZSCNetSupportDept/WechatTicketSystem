package love.sola.netsupport.api.admin;

import com.google.gson.Gson;
import love.sola.netsupport.api.Response;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.JsonP;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "GetUser",urlPatterns = "/api/getuser",loadOnStartup = 1)
public class GetUser extends HttpServlet {

	private Gson gson = SQLCore.gson;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String json = gson.toJson(query(request));
		out.println(JsonP.parse(request, json));
		out.close();
	}

	private Response query(HttpServletRequest request) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		if ((id == null || id.isEmpty()) && (name == null || name.isEmpty())) {
			return new Response(Response.ResponseCode.PARAMETER_REQUIRED);
		}
		if (id != null) {
			try {
				User u = TableUser.getUserById(Long.parseLong(id));
				if (u == null)
					return new Response(Response.ResponseCode.USER_NOT_FOUND);
				else
					return new Response(Response.ResponseCode.OK, u);
			} catch (NumberFormatException e) {
				return new Response(Response.ResponseCode.ILLEGAL_PARAMETER);
			}
		} else {
			User u = TableUser.getUserByName(name);
			if (u == null)
				return new Response(Response.ResponseCode.USER_NOT_FOUND);
			else
				return new Response(Response.ResponseCode.OK, u);
		}
	}

}
