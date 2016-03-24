package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/13.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TicketLookup extends API {

	public TicketLookup() {
		url = "/admin/ticketlookup";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		Operator op = (Operator) session.getAttribute(Attribute.OPERATOR);
		int block;
		if (req.getParameter("block") != null) {
			block = Integer.parseInt(req.getParameter("block"));
		} else {
			block = op.getBlock();
		}
		return TableTicket.unsolvedByBlock(block);
	}

}
