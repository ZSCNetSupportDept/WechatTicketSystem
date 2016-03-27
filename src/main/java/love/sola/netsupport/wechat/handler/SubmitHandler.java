package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.NewsBuilder;

import java.util.Map;

import static love.sola.netsupport.config.Lang.format;
import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/12/9.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class SubmitHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		User u = TableUser.getByWechat(wxMessage.getFromUserName());
		if (TableTicket.hasOpen(u)) {
			return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
					.content(lang("Already_Opening_Ticket")).build();
		}
		WxSession session = WechatSession.create();
		session.setAttribute(Attribute.AUTHORIZED, Command.SUBMIT);
		session.setAttribute(Attribute.WECHAT, wxMessage.getFromUserName());
		session.setAttribute(Attribute.USER, u);

		NewsBuilder out = WxMpXmlOutMessage.NEWS().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
		item.setTitle(lang("Submit_Title"));
		item.setDescription(lang("Submit_Desc"));
		item.setUrl(format("User_Submit_Link", session.getId(), u.getName(), u.getIsp().id, u.getRoom(), u.getBlock(), u.getPhone()));
		out.addArticle(item);
		return out.build();
	}

}
