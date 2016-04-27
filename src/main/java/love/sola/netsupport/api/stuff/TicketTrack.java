package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketTrack extends API {

	public TicketTrack() {
		url = "/admin/tickettrack";
		access = Access.MEMBER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String tid = req.getParameter("id");
		if (tid == null) {
			return Error.PARAMETER_REQUIRED;
		}
		return TableTicket.track(Integer.parseInt(tid));
	}

}
