package love.sola.netsupport.api;

import com.google.gson.Gson;
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

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("name");
		if (name == null) {
			response.addHeader("Content-type", "text/plain;charset=utf-8");
			out.println("Username required.");
		} else {
			response.addHeader("Content-type", "text/json;charset=utf-8");
			Gson gson = new Gson();
			out.println(gson.toJson(SQLQuery.getUserFromName(name)));
		}
		out.close();
	}

}
