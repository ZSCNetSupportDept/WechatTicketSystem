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

package love.sola.netsupport.wechat.handler.admin;

import love.sola.netsupport.auth.OAuth2Handler;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableOperator;
import love.sola.netsupport.util.Redirect;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static love.sola.netsupport.config.Lang.format;
import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class LoginHandler implements WxMpMessageHandler, OAuth2Handler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
        try {
            Operator operator = TableOperator.get(wxMessage.getFromUserName());
            if (operator == null)
                out.content(lang("Not_Operator"));
            else if (operator.getAccess() >= Access.NO_LOGIN) {
                out.content(lang("No_Login"));
            } else {
                WxSession session = WechatSession.create();
                session.setAttribute(Attribute.AUTHORIZED, Command.LOGIN);
                session.setAttribute(Attribute.WECHAT, wxMessage.getFromUserName());
                session.setAttribute(Attribute.OPERATOR, operator);
                out.content(format("Home_Page_Msg", format("Operator_Home_Page", session.getId())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.content(lang("Login_Error"));
        }
        return out.build();
    }


    @Override
    public void onOAuth2(AsyncContext actx, HttpServletResponse resp, String user, WxSession session) {
        try {
            Operator operator = TableOperator.get(user);
            if (operator == null) {
                Redirect.error().icon(Redirect.WeUIIcon.WARN_SAFE).noButton()
                        .title(lang("Not_Operator")).msg(lang("Not_Operator_OAuth2")).go(resp);
                return;
            }
            if (operator.getAccess() >= Access.NO_LOGIN) {
                Redirect.error().icon(Redirect.WeUIIcon.WAITING).noButton()
                        .title(lang("Left_Operator_Title")).msg(lang("Left_Operator")).go(resp);
                return;
            }
            session.setAttribute(Attribute.AUTHORIZED, Command.LOGIN);
            session.setAttribute(Attribute.WECHAT, user);
            session.setAttribute(Attribute.OPERATOR, operator);
            resp.sendRedirect(format("Operator_Home_Page", session.getId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
