package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
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
 * ***********************************************
 * Created by Sola on 2015/11/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class RegisterHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager)
			throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		String fromUser = wxMessage.getFromUserName();
		User u = TableUser.getByWechat(fromUser);
		WxSession session = WechatSession.create();
		if (u != null) {
			session.setAttribute(Attribute.AUTHORIZED, Command.PROFILE);
			session.setAttribute(Attribute.WECHAT, fromUser);
			session.setAttribute(Attribute.USER, u);
			out.content(format("Already_Registered", format("User_Profile_Link", session.getId(), u.getName(), u.getIsp().id, u.getNetAccount(), u.getBlock(), u.getRoom(), u.getPhone())));
		} else {
			session.setAttribute(Attribute.AUTHORIZED, Command.REGISTER);
			session.setAttribute(Attribute.WECHAT, fromUser);
			out.content(format("User_Register", format("User_Register_Link", session.getId())));
		}
		return out.build();
	}

}
