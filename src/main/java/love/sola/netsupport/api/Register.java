package love.sola.netsupport.api;

import com.google.gson.Gson;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.util.ParseUtil;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * ***********************************************
 * Created by Sola on 2015/11/29.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "Register", urlPatterns = "/api/register", loadOnStartup = 21)
public class Register extends HttpServlet {

	public static final String STUDENT_ID_REGEX = "^(2010|2012|2013|2014|2015)[0-9]{9}$";
	public static final String PHONE_NUMBER_REGEX = "^1[34578][0-9]{9}$";

	private Gson gson = SQLCore.gson;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/json;charset=utf-8");
		PrintWriter out = response.getWriter();

		WxSession session = Checker.isAuthorized(request, Command.REGISTER);
		if (session == null) {
			printAuthorizeFailed(request, out);
			return;
		}
		String wechat = (String) session.getAttribute(Attribute.WECHAT);
		if (wechat == null) {
			printAuthorizeFailed(request, out);
			return;
		}

		ISP isp = checkISP(request.getParameter("isp"));
		int block = checkBlock(request.getParameter("block"));
		String result = register(
				checkStudentId(request.getParameter("sid")),
				request.getParameter("name"),
				isp,
				checkNetAccount(request.getParameter("username"), isp),
				block,
				checkRoom(request.getParameter("room"), block),
				checkPhoneNumber(request.getParameter("phone")),
				wechat
		);
		boolean isSuccess = result.equals("Register_Success");
		if (isSuccess) {
			session.invalidate();
			out.println(ParseUtil.parseJsonP(request, gson.toJson(new Response(Response.ResponseCode.OK, result))));
		} else {
			out.println(ParseUtil.parseJsonP(request, gson.toJson(new Response(Response.ResponseCode.REQUEST_FAILED, result))));
		}
		out.close();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private String register(long sid, String name, ISP isp, String netAccount, int block, int room, long phone, String wechat) {
		if (wechat == null) return "Illegal_Request";
		if (sid == -1) return "Invalid_Student_Id";
		if (name == null) return "Invalid_Name";
		if (isp == null) return "Invalid_ISP";
		if (netAccount == null) return "Invalid_Account";
		if (block == -1) return "Invalid_Block";
		if (room == -1) return "Invalid_Room";
		if (phone == -1) return "Invalid_Phone_Number";
		User user = TableUser.getById(sid);
		if (user == null) return "Invalid_Student_Id";
		if (!user.getName().equals(name)) return "Invalid_Name";
		if (user.getWechatId() != null) return "User_Already_Registered";
		user.setIsp(isp);
		user.setNetAccount(netAccount);
		user.setBlock(block);
		user.setRoom(room);
		user.setPhone(phone);
		user.setWechatId(wechat);
		try {
			TableUser.update(user);
		} catch (ConstraintViolationException e) {
			String dupKey = e.getConstraintName();
			return "Duplicated_" + dupKey.toUpperCase(); // PHONE ACCOUNT WECHAT
		}
		return "Register_Success";
	}


	private long checkStudentId(String studentId) {
		if (studentId == null) return -1;
		if (studentId.matches(STUDENT_ID_REGEX)) {
			try {
				return Long.parseLong(studentId);
			} catch (NumberFormatException ignored) {
			}
		}
		return -1;
	}

	private long checkPhoneNumber(String phone) {
		if (phone == null) return -1;
		if (phone.matches(PHONE_NUMBER_REGEX)) {
			try {
				return Long.parseLong(phone);
			} catch (NumberFormatException ignored) {
			}
		}
		return -1;
	}

	private ISP checkISP(String isp) {
		if (isp == null) return null;
		try {
			return ISP.fromId(Integer.parseInt(isp));
		} catch (NumberFormatException ignored) {
		}
		return null;
	}

	private String checkNetAccount(String account, ISP isp) {
		if (isp == null) return null;
		if (account == null) return null;
		if (!account.matches(isp.accountRegex)) return null;
		return account;
	}

	private int checkBlock(String block) {
		if (block == null) return -1;
		try {
			return Integer.parseInt(block);
		} catch (NumberFormatException ignored) {
		}
		return -1;
	}

	private int checkRoom(String room, int block) {
		if (block == -1) return -1;
		if (room == null) return -1;
		try {
			Integer i = Integer.parseInt(room);
			if (i <= 100 || i >= 1300) return -1;
			return i;
		} catch (NumberFormatException ignored) {
		}
		return -1;
	}

	private void printAuthorizeFailed(HttpServletRequest request, PrintWriter out) {
		out.println(ParseUtil.parseJsonP(request, gson.toJson(new Response(Response.ResponseCode.AUTHORIZE_FAILED))));
		out.close();
		return;
	}

}
