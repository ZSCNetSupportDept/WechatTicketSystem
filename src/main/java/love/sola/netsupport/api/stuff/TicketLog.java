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

package love.sola.netsupport.api.stuff;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.proxy.HibernateProxy;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketLog extends API {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    public TicketLog() {
        url = "/admin/ticketlog";
        access = Access.MEMBER;
        authorize = Command.LOGIN;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object process(HttpServletRequest req, WxSession session) throws Exception {
        int first;
        int limit;
        Date start;
        Date end;
        first = req.getParameter("first") == null ? 0 : Integer.parseInt(req.getParameter("first"));
        limit = req.getParameter("limit") == null ? 20 : Integer.parseInt(req.getParameter("limit"));
        start = req.getParameter("start") == null ? getToday() : dateFormat.parse(req.getParameter("start"));
        end = req.getParameter("end") == null ? getToday() : dateFormat.parse(req.getParameter("end"));
        end = DateUtils.addDays(end, 1);
        try (Session s = SQLCore.sf.openSession()) {
            AuditReader reader = TableTicket.getAuditReader(s);
            List<Object[]> resultList = reader.createQuery()
                .forRevisionsOfEntity(Ticket.class, false, true)
                .addOrder(AuditEntity.revisionNumber().desc())
                .add(AuditEntity.revisionProperty("timestamp").between(start.getTime(), end.getTime()))
                .setFirstResult(first)
                .setMaxResults(limit)
                .getResultList();
            return TableTicket.initializeTickets(resultList);
        }
    }

}
