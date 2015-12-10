package love.sola.netsupport.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Redirect {

	public static final String REDIRECT_PAGE = lang("RESULT_PAGE");

	public static void message(HttpServletResponse response, int type, String message) throws IOException {
		response.sendRedirect(
				response.encodeRedirectURL(REDIRECT_PAGE +
						"?msg=" + message +
						"&type=" + type
				)
		);
	}

}
