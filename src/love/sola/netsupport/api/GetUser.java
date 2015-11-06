package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.enums.ResponseCode;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLQuery;

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

	private Gson gson = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		gson = new Gson();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		if ((id == null || id.isEmpty()) && (name == null || name.isEmpty())) {
			out.println(gson.toJson(new Response(ResponseCode.PARAMETER_REQUIRED)));
		} else if (id != null) {
			try {
				User u = SQLQuery.getUserById(Integer.parseInt(id));
				if (u == null)
					out.println(gson.toJson(new Response(ResponseCode.USER_NOT_FOUND)));
				else
					out.println(gson.toJson(new Response(ResponseCode.OK, u)));
			} catch (NumberFormatException e) {
				out.println(gson.toJson(new Response(ResponseCode.ILLEGAL_PARAMETER)));
			}
		} else {
			User u = SQLQuery.getUserByName(name);
			if (u == null)
				out.println(gson.toJson(new Response(ResponseCode.USER_NOT_FOUND)));
			else
				out.println(gson.toJson(new Response(ResponseCode.OK, u)));
		}
		out.close();
	}

}
