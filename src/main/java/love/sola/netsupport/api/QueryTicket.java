package love.sola.netsupport.api;

import love.sola.netsupport.sql.SQLCore;
import org.hibernate.HibernateException;
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
 * Created by Sola on 2015/12/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "QueryTicket", urlPatterns = "/api/queryticket", loadOnStartup = 23)
public class QueryTicket extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();
	}

	private Response query(HttpServletRequest request) {
		try (Session s = SQLCore.sf.openSession()) {
			// TODO: 2015/12/5 TICKET QUERY 
		} catch (HibernateException e) {

		}
		return null;
	}

}
