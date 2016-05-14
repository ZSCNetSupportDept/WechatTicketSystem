/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.sola.netsupport.api.user;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketSubmit extends API {

	public TicketSubmit() {
		url = "/ticketsubmit";
		access = Access.USER;
		authorize = Command.SUBMIT;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String desc = req.getParameter("desc");
		if (desc == null || desc.isEmpty()) {
			return Error.PARAMETER_REQUIRED;
		}
		if (desc.length() > Settings.MAX_DESC_LENGTH) {
			return Error.LENGTH_LIMIT_EXCEEDED;
		}

		try (Session s = SQLCore.sf.openSession()) {
			User u = session.getAttribute(Attribute.USER);
			if (TableTicket.hasOpen(u)) {
				session.invalidate();
				return Error.ALREADY_SUBMITTED;
			}
			Ticket t = new Ticket();
			t.setUser(u);
			t.setDescription(desc);
			t.setStatus(0);
			s.beginTransaction();
			s.save(t);
			s.getTransaction().commit();
			session.invalidate();
			return Error.OK;
		}
	}
}
