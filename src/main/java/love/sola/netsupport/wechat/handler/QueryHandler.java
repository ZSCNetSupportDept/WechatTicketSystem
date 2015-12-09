package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.api.Authorize;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
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

import java.text.SimpleDateFormat;
import java.util.Map;

import static love.sola.netsupport.config.Lang.format;
import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/12/8.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class QueryHandler implements WxMpMessageHandler {

	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		User u = TableUser.getUserByWechat(wxMessage.getFromUserName());
		Ticket t = TableTicket.queryLast(u);
		if (t == null) {
			return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName())
					.content(lang("No_Ticket_Available")).build();
		}

		NewsBuilder out = WxMpXmlOutMessage.NEWS().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
		StringBuilder sb = new StringBuilder()
				.append("Ticket ID: ").append(t.getId()).append("\n")
				.append("Desc: ").append(t.getDescription()).append("\n")
				.append("Submit Time: ").append(dateFormat.format(t.getSubmitTime())).append("\n");
		if (t.getUpdateTime() != null) {
			sb.append("Operator: ").append(t.getOperator().getId()).append("\n");
			sb.append("Remark: ").append(t.getRemark()).append("\n");
			sb.append("Latest Update: ").append(dateFormat.format(t.getUpdateTime())).append("\n");
		}
		sb.append("Ticket Status: ").append(t.getStatus()).append("\n");
		sb.append(lang("More_Details"));
		item.setUrl(format("User_Query_Link", wxMessage.getFromUserName()));
		item.setTitle(lang("Query_Title"));
		item.setDescription(sb.toString());
		out.addArticle(item);
		Authorize.fetchedTime.put(wxMessage.getFromUserName(), System.currentTimeMillis());
		Authorize.fetchedCommand.put(wxMessage.getFromUserName(), Command.QUERY);
		return out.build();
	}

}
