package love.sola.netsupport.api;

import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Block;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.TableUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/29.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "Register", urlPatterns = "/api/register", loadOnStartup = 22)
public class Register extends HttpServlet {

	public static Map<String, Long> authorized = new ConcurrentHashMap<>();

	public static final String STUDENT_ID_REGEX = "^(2010|2012|2013|2014|2015)[0-9]{9}$";
	public static final String PHONE_NUMBER_REGEX = "^1[34578][0-9]{9}$";

	public static final String REDIRECT_PAGE = "http://topaz.sinaapp.com/nm/result.html?";

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/plain;charset=utf-8");

		ISP isp = checkISP(request.getParameter("isp"));
		Block block = checkBlock(request.getParameter("block"));
		String result = register(
				checkStudentId(request.getParameter("sid")),
				request.getParameter("name"),
				isp,
				checkNetAccount(request.getParameter("username"), isp),
				block,
				checkRoom(request.getParameter("room"), block),
				checkPhoneNumber(request.getParameter("phone")),
				checkWechat(request.getParameter("wechatid"))
		);
		response.sendRedirect(
				response.encodeRedirectURL(REDIRECT_PAGE +
						"msg=" + result + "" +
						"&type=" + (result.equals("Register_Success") ? 1 : 0)
				)
		);
	}

	@SuppressWarnings("Duplicates")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.addHeader("Content-type", "text/plain;charset=utf-8");
		response.sendRedirect(
				response.encodeRedirectURL(REDIRECT_PAGE +
						"msg=" + lang("Illegal_Request") +
						"&type=-1"
				)
		);
	}

	private String register(long sid, String name, ISP isp, String netAccount, Block block, int room, long phone, String wechat) {
		if (wechat == null) return "Illegal_Request";
		if (sid == -1) return "Invalid_Student_Id";
		if (name == null) return "Invalid_Name";
		if (isp == null) return "Invalid_ISP";
		if (netAccount == null) return "Invalid_Account";
		if (block == null) return "Invalid_Block";
		if (room == -1) return "Invalid_Room";
		if (phone == -1) return "Invalid_Phone_Number";
		User user = TableUser.getUserById(sid);
		if (user == null) return "Invalid_Student_Id";
		if (!user.getName().equals(name)) return "Invalid_Name";
		if (user.getWechatId() != null) return "User_Already_Registered";
		user.setIsp(isp);
		user.setNetAccount(netAccount);
		user.setBlock(block);
		user.setRoom(room);
		user.setPhone(phone);
		user.setWechatId(wechat);
		TableUser.updateUser(user);
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

	private Block checkBlock(String block) {
		if (block == null) return null;
		try {
			return Block.fromId(Integer.parseInt(block));
		} catch (NumberFormatException ignored) {
		}
		return null;
	}

	private int checkRoom(String room, Block block) {
		if (block == null) return -1;
		if (room == null) return -1;
		try {
			Integer i = Integer.parseInt(room);
			if (i <= 100 || i >= 1300) return -1;
			return i;
		} catch (NumberFormatException ignored) {
		}
		return -1;
	}

	private String checkWechat(String wechat) {
		if (wechat == null) return null;
		Long l = authorized.remove(wechat);
		return l == null ? null : l < System.currentTimeMillis() - Settings.I.User_Register_Timeout * 1000 ? null : wechat;
	}

}
