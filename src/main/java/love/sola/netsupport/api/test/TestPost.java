package love.sola.netsupport.api.test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "TestPost",urlPatterns = "/api/testpost",loadOnStartup = 10)
public class TestPost extends HttpServlet {

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
		response.addHeader("Content-type", "text/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println("Parameters:");
		for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
		}
		Integer i = (Integer) request.getSession().getAttribute("ReqCount");
		i = i == null ? 0 : i;
		request.getSession().setAttribute("ReqCount", i + 1);
		out.println("ReqCount = " + i);
		out.close();
	}

}
