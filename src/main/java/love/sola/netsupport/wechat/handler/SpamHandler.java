package love.sola.netsupport.wechat.handler;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/12/17.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class SpamHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT()
				.fromUser(wxMessage.getToUserName())
				.toUser(wxMessage.getFromUserName());

		return out.build();
	}

}
