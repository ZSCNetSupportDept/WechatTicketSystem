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
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class ProfileHandler implements WxMpMessageHandler {

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

}
