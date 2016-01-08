package love.sola.netsupport.wechat.handler.admin;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.sql.TableOperator;
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
 * Created by Sola on 2015/12/28.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class OperatorInfoHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		try {
			Operator op = TableOperator.get(wxMessage.getFromUserName());
			if (op == null)
				out.content(lang("Not_Operator"));
			else if (op.getAccess() == Access.NOLOGIN) {
				out.content(lang("No_Login"));
			} else {
				out.content(format("Operator_Info", op.getId(), op.getName(), op.getAccess(), op.getBlock(), op.getWeek()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.content(lang("Login_Error"));
		}
		return out.build();
	}

}
