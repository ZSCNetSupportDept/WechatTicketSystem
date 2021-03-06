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

import org.hibernate.Session;

import java.util.Date;
import java.util.Map;

import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.util.ParseUtil;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class CancelHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        User u = TableUser.getByWechat(wxMessage.getFromUserName());
        Ticket t = TableTicket.latestOpen(u);
        if (t == null) {
            return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
                    .content(lang("No_Open_Ticket_Available")).build();
        }
        try (Session s = SQLCore.sf.openSession()) {
            t.setOperator(Operator.USER_SELF);
            t.setUpdateTime(new Date());
            t.setRemark(lang("User_Cancel_Remark"));
            t.setStatus(Status.SOLVED);
            s.beginTransaction();
            s.update(t);
            s.getTransaction().commit();
            NewsBuilder out = WxMpXmlOutMessage.NEWS().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setTitle(lang("Cancel_Title"));
            item.setDescription(ParseUtil.parseTicket(t));
            out.addArticle(item);
            return out.build();
        } catch (Exception e) {
            e.printStackTrace();
            return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
                    .content(lang("Cancel_Failed")).build();
        }
    }

}
