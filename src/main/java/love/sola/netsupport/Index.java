package love.sola.netsupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2014/8/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "Index",urlPatterns = "/index",loadOnStartup = 1)
public class Index extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/plain;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println("Wechat Ticket System (WTS) 0.1 Copyright 2015 Sola & LiuYue all rights reserved. | Commercial license for ZSC Network Support Department (ZSCNSD).");
		out.println("For any problem, Please contact loli@sola.love.");
		out.close();
	}

}
