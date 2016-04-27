package love.sola.netsupport.auth;

import love.sola.netsupport.session.WxSession;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public interface OAuth2Handler {

	void onOAuth2(AsyncContext actx, HttpServletResponse resp, String user, WxSession session);

}
