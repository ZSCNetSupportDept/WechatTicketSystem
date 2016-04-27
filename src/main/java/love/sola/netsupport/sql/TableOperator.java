package love.sola.netsupport.sql;

import love.sola.netsupport.pojo.Operator;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TableOperator extends SQLCore {

	public static boolean has(String wechat) {
		try (Session s = SQLCore.sf.openSession()) {
			return (long) s.createCriteria(Operator.class)
					.add(Restrictions.eq(Operator.PROPERTY_WECHAT, wechat))
					.setProjection(Projections.rowCount())
					.uniqueResult() > 0;
		}
	}


	public static Operator get(String wechat) {
		try (Session s = SQLCore.sf.openSession()) {
			return (Operator) s.createCriteria(Operator.class)
					.add(Restrictions.eq(Operator.PROPERTY_WECHAT, wechat))
					.uniqueResult();
		}
	}

	public static Operator get(int id) {
		try (Session s = SQLCore.sf.openSession()) {
			return s.get(Operator.class, id);
		}
	}

	protected static void init() {
		try (Session s = SQLCore.sf.openSession()) {
			Operator.USER_SELF = s.get(Operator.class, -1);
			Operator.ADMIN = s.get(Operator.class, 0);
		}
	}

}
