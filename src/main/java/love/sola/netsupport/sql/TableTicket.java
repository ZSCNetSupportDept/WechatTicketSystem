package love.sola.netsupport.sql;

import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Ticket;
import love.sola.netsupport.pojo.User;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
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


	public static Ticket queryLastOpen(User u) {
		try (Session s = SQLCore.sf.openSession()) {
			return (Ticket) s.createCriteria(Ticket.class)
					.addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
					.add(Restrictions.eq(Ticket.PROPERTY_USER, u))
					.add(Restrictions.ne(Ticket.PROPERTY_STATUS, Status.SOLVED))
					.setMaxResults(1)
					.uniqueResult();
		}
	}

	public static Ticket queryLast(User u) {
		try (Session s = SQLCore.sf.openSession()) {
			return (Ticket) s.createCriteria(Ticket.class)
					.addOrder(Order.desc(Ticket.PROPERTY_SUBMIT_TIME))
					.add(Restrictions.eq(Ticket.PROPERTY_USER, u))
					.setMaxResults(1)
					.uniqueResult();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Ticket> queryUnsolvedByBlock(int b) {
		try (Session s = SQLCore.sf.openSession()) {
			return s.createCriteria(Ticket.class)
					.createCriteria(Ticket.PROPERTY_USER)
					.add(Restrictions.between(User.PROPERTY_BLOCK, b * 10, (b + 1) * 10 - 1))
					.list();
		}
	}

}
