package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.api.Authorize;
import love.sola.netsupport.pojo.User;
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
import static love.sola.netsupport.config.Lang.lang;

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
		User u = TableUser.getUserByWechat(fromUser);
		if (u != null) {
			out.content(lang("Already_Registered"));
		} else {
			out.content(format("User_Register_Link", wxMessage.getFromUserName()));
			Authorize.fetchedTime.put(wxMessage.getFromUserName(), System.currentTimeMillis());
			Authorize.fetchedCommand.put(wxMessage.getFromUserName(), Command.REGISTER);
		}
		return out.build();
	}

}
