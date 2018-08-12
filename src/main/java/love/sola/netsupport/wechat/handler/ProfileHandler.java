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

package love.sola.netsupport.wechat.handler;

import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;

import love.sola.netsupport.auth.OAuth2Handler;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.Redirect;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import static love.sola.netsupport.config.Lang.format;
import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class ProfileHandler implements WxMpMessageHandler, OAuth2Handler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        User u = TableUser.getByWechat(wxMessage.getFromUserName());
        WxSession session = WechatSession.create();
        session.setAttribute(Attribute.AUTHORIZED, Command.PROFILE);
        session.setAttribute(Attribute.WECHAT, wxMessage.getFromUserName());
        session.setAttribute(Attribute.USER, u);
        TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
        out.content(format("Profile_Modify", format("User_Profile_Link", session.getId(), u.getName(), u.getIsp().id, u.getNetAccount(), u.getBlock(), u.getRoom(), u.getPhone())));
        return out.build();
    }

    @Override
    public void onOAuth2(AsyncContext actx, HttpServletResponse resp, String user, WxSession session) {
        try {
            User u = TableUser.getByWechat(user);
            if (u == null) {
                session.setAttribute(Attribute.AUTHORIZED, Command.REGISTER);
                session.setAttribute(Attribute.WECHAT, user);
                Redirect.error().icon(Redirect.WeUIIcon.INFO).noButton()
                        .title(lang("Need_Register_Title")).msg(lang("Need_Register"))
                        .to(format("User_Register_Link", session.getId())).go(resp);
                return;
            }
            session.setAttribute(Attribute.AUTHORIZED, Command.PROFILE);
            session.setAttribute(Attribute.WECHAT, user);
            session.setAttribute(Attribute.USER, u);
            resp.sendRedirect(format("User_Profile_Link", session.getId(), u.getName(), u.getIsp().id, u.getNetAccount(), u.getBlock(), u.getRoom(), u.getPhone()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
