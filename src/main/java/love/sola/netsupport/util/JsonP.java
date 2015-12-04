package love.sola.netsupport.util;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/3.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class JsonP {

	public static String parse(HttpServletRequest request, String json) {
		String jsonp = request.getParameter("jsonp");
		if (jsonp == null || jsonp.isEmpty())
			return json;
		else
			return jsonp.replace("{0}", json);
	}

}
