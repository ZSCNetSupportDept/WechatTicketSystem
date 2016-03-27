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
 * ***********************************************
 * Created by Sola on 2015/12/12.
 * Don't modify this source without my agreement
 * ***********************************************
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
	public void onOAuth2(AsyncContext acxt, HttpServletResponse resp, String user, WxSession session) {
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
