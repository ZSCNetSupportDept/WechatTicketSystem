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

import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableOperator;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import java.util.Map;

import static love.sola.netsupport.config.Lang.format;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class SubscribeHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		String fromUser = wxMessage.getFromUserName();
		User u = TableUser.getByWechat(fromUser);
		WxSession session = WechatSession.create();
		if (u != null) {
			session.setAttribute(Attribute.AUTHORIZED, Command.PROFILE);
			session.setAttribute(Attribute.WECHAT, fromUser);
			session.setAttribute(Attribute.USER, u);
			out.content(format("Event_Subscribe", format("Already_Registered", format("User_Profile_Link", session.getId(), u.getName(), u.getIsp().id, u.getNetAccount(), u.getBlock(), u.getRoom(), u.getPhone()))));

			Operator op = TableOperator.get(fromUser);
			if (op != null) {
				wxMpService.userUpdateGroup(fromUser, 100L);
			}
		} else {
			session.setAttribute(Attribute.AUTHORIZED, Command.REGISTER);
			session.setAttribute(Attribute.WECHAT, fromUser);
			out.content(format("Event_Subscribe", format("User_Register", format("User_Register_Link", session.getId()))));
		}
		return out.build();
	}

}
