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

	public static boolean nonNull(Object... v) {
		for (Object o : v) if (o == null) return false;
		return true;
	}

	public static boolean authorized(HttpSession s, Command c) {
		return s != null && s.getAttribute("authorized") == c;
	}

}
