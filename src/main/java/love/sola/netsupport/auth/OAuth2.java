package love.sola.netsupport.auth;

import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.WechatSession;
import love.sola.netsupport.wechat.WxMpServlet;
import me.chanjar.weixin.common.session.WxSession;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "OAuth2", urlPatterns = "/oauth2/callback", loadOnStartup = 21)
public class OAuth2 extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AsyncContext acxt = req.startAsync();
		String code = req.getParameter("code");
		String state = req.getParameter("state");
		if (Checker.hasNull(code, state)) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		acxt.start(() -> {
			try {
				WxMpService wxMpService = WxMpServlet.instance.wxMpService;
				WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
				WxMpUser wxUser = wxMpService.oauth2getUserInfo(token, "zh_CN");
				String sid = WechatSession.genId();
				WxSession session = WechatSession.get(sid, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

	}

}
