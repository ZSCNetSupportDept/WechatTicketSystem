package love.sola.netsupport.util;

import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpSession;

/**
 * ***********************************************
 * Created by Sola on 2015/12/12.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Checker {

	public static boolean hasNull(Object... v) {
		for (Object o : v) if (o == null) return true;
		return false;
	}

	public static boolean authorized(HttpSession s, Command c) {
		return s != null && s.getAttribute("authorized") == c;
	}

	public static boolean operator(HttpSession s) {
		return s != null && s.getAttribute("operator") != null;
	}

}
