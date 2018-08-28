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

package love.sola.netsupport.sql;

import love.sola.netsupport.pojo.Operator;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;

import java.util.List;

import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TableTicket extends SQLCore {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SID = "sid";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_SUBMIT_TIME = "submittime";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_UPDATE_TIME = "updatetime";
    public static final String COLUMN_OPSID = "opsid";
    public static final String COLUMN_STATUS = "status";


    public static Ticket latestOpen(User u) {
        try (Session s = SQLCore.sf.openSession()) {
            return (Ticket) s.createCriteria(Ticket.class)
                    .addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
                    .add(Restrictions.eq(Ticket.PROPERTY_USER, u))
                    .add(Restrictions.ne(Ticket.PROPERTY_STATUS, Status.SOLVED))
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    public static Ticket latest(User u) {
        try (Session s = SQLCore.sf.openSession()) {
            return (Ticket) s.createCriteria(Ticket.class)
                    .addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
                    .add(Restrictions.eq(Ticket.PROPERTY_USER, u))
                    .setMaxResults(1)
                    .uniqueResult();
        }
    }

    public static boolean hasOpen(User u) {
        try (Session s = SQLCore.sf.openSession()) {
            return (long) s.createCriteria(Ticket.class)
                    .add(Restrictions.eq(Ticket.PROPERTY_USER, u))
                    .add(Restrictions.ne(Ticket.PROPERTY_STATUS, Status.SOLVED))
                    .setProjection(Projections.rowCount())
                    .uniqueResult() > 0;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Ticket> unsolvedByBlock(int b) {
        if (b == 0) return unsolved();
        try (Session s = SQLCore.sf.openSession()) {
            return s.createCriteria(Ticket.class)
                    .addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
                    .add(Restrictions.ne(Ticket.PROPERTY_STATUS, Status.SOLVED))
                    .createCriteria(Ticket.PROPERTY_USER)
                    .add(Restrictions.between(User.PROPERTY_BLOCK, b * 10, (b + 1) * 10 - 1))
                    .list();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Ticket> unsolved() {
        try (Session s = SQLCore.sf.openSession()) {
            return s.createCriteria(Ticket.class)
                    .createAlias(Ticket.PROPERTY_USER, "u")
                    .addOrder(Order.asc("u." + User.PROPERTY_BLOCK))
                    .addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
                    .add(Restrictions.ne(Ticket.PROPERTY_STATUS, Status.SOLVED))
                    .list();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Object[]> track(int tid) {
        try (Session s = SQLCore.sf.openSession()) {
            AuditReader reader = getAuditReader(s);
            return reader.createQuery()
                    .forRevisionsOfEntity(Ticket.class, false, true)
                    .addOrder(AuditEntity.revisionNumber().desc())
                    .add(AuditEntity.id().eq(tid))
                    .getResultList()
                    ;
        }
    }

    /**
     * this is a hacky method to initialize all related entities of ticket
     */
    public static List<Object[]> initializeTickets(List<Object[]> resultList) {
        for (Object[] result : resultList) {
            Ticket value = ((Ticket) result[0]);
            HibernateProxy proxiedUser = (HibernateProxy) value.getUser();
            if (proxiedUser != null) {
                User unproxiedUser = ((User) proxiedUser.getHibernateLazyInitializer()
                    .getImplementation());
                value.setUser(unproxiedUser);
            }
            HibernateProxy proxiedOperator = (HibernateProxy) value.getOperator();
            if (proxiedOperator != null) {
                Operator unproxiedOperator = ((Operator) proxiedOperator.getHibernateLazyInitializer()
                    .getImplementation());
                value.setOperator(unproxiedOperator);
            }
        }
        return resultList;
    }

}
