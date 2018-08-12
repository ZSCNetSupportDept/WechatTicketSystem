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

package love.sola.netsupport.api.manager;

import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Checker;
import love.sola.netsupport.wechat.Command;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketPush extends API {

    public TicketPush() {
        url = "/admin/ticketpush";
        access = Access.LEADER;
        authorize = Command.LOGIN;
    }

    @Override
    protected Object process(HttpServletRequest req, WxSession session) throws Exception {
        String uid = req.getParameter("uid");
        String desc = req.getParameter("desc");
        if (Checker.hasNull(uid, desc)) {
            return Error.PARAMETER_REQUIRED;
        }
        if (desc.length() > Settings.MAX_DESC_LENGTH) {
            return Error.LENGTH_LIMIT_EXCEEDED;
        }
        Operator op = session.getAttribute(Attribute.OPERATOR);
        try (Session s = SQLCore.sf.openSession()) {
            s.beginTransaction();
            User u = s.get(User.class, Long.parseLong(uid));
            if (u == null) {
                return Error.USER_NOT_FOUND;
            }
            Ticket t = new Ticket(u, desc, null, "Pushed By Admin", null, op, Status.UNCHECKED);
            s.save(t);
            s.getTransaction().commit();
            return t;
        }
    }

}
