package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.pojo.User;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.matcher.CommandMatcher;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class RegisterHandler implements WxMpMessageHandler {

	Map<String, User> pre_confirm = new HashMap<>();

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager)
			throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		String in = wxMessage.getContent();
		String userName = wxMessage.getFromUserName();
		if (in.matches(Command.REGISTER.getRegex())) {
			out.content("Welcome, please type your student identification number.");
		} else if (pre_confirm.containsKey(userName)) {
			//TODO

		} else {
			out.content("Illegal Operation.");
			CommandMatcher.inCmdUsers.remove(userName);
		}
		return out.build();
	}

}
