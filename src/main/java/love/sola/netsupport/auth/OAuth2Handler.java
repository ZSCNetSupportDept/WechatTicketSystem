package love.sola.netsupport.auth;

import love.sola.netsupport.session.WxSession;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

/**
 * ***********************************************
 * Created by Sola on 2016/3/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public interface OAuth2Handler {

	void onOAuth2(AsyncContext actx, HttpServletResponse resp, String user, WxSession session);

}
