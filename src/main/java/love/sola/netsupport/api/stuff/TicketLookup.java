package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketLookup extends API {

	public TicketLookup() {
		url = "/admin/ticketlookup";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		Operator op = session.getAttribute(Attribute.OPERATOR);
		int block;
		if (req.getParameter("block") != null) {
			block = Integer.parseInt(req.getParameter("block"));
		} else {
			block = op.getBlock();
		}
		return TableTicket.unsolvedByBlock(block);
	}

}
