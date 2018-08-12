/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.sola.netsupport.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.WxMpServlet;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@WebServlet(name = "OAuth2", urlPatterns = "/oauth2/callback", loadOnStartup = 21, asyncSupported = true)
public class OAuth2 extends HttpServlet {

    private static Map<String, OAuth2Handler> oAuth2HandlerMap = new HashMap<>();

    /**
     * for {@link love.sola.netsupport.wechat.WxMpServlet#registerCommands}
     *
     * @param state   the state key from open platform callback.
     * @param handler handler
     */
    public static void registerOAuth2Handler(String state, OAuth2Handler handler) {
        oAuth2HandlerMap.put(state, handler);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext actx = req.startAsync();
        String code = req.getParameter("code");
        String state = req.getParameter("state");
        if (Checker.hasNull(code, state)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        OAuth2Handler handler = oAuth2HandlerMap.get(state);
        if (handler == null) {
            resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
            return;
        }
        actx.start(() -> {
            try {
                WxMpService wxMpService = WxMpServlet.instance.wxMpService;
                WxMpOAuth2AccessToken token = wxMpService.oauth2getAccessToken(code);
                String wechat = token.getOpenId();
                WxSession session = WechatSession.create();
                handler.onOAuth2(actx, (HttpServletResponse) actx.getResponse(), wechat, session);
                actx.complete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
