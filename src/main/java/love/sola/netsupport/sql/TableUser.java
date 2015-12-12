package love.sola.netsupport.sql;

import love.sola.netsupport.pojo.User;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * ***********************************************
 * Created by Sola on 2015/11/10.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@SuppressWarnings("Duplicates")
public class TableUser extends SQLCore {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ISP = "isp";
	public static final String COLUMN_NET_ACCOUNT = "netaccount";
	public static final String COLUMN_WECHAT = "wechat";
	public static final String COLUMN_BLOCK = "block";
	public static final String COLUMN_ROOM = "room";
	public static final String COLUMN_PHONE = "phone";

	public static User getById(long id) {
		try (Session s = sf.openSession()) {
			return s.get(User.class, id);
		}
	}


	public static User getByWechat(String wechat) {
		try (Session s = sf.openSession()) {
			return (User) s.createCriteria(User.class).add(Restrictions.eq(User.PROPERTY_WECHAT, wechat)).uniqueResult();
		}
	}

	public static User getByName(String name) {
		try (Session s = sf.openSession()) {
			return (User) s.createCriteria(User.class).add(Restrictions.eq(User.PROPERTY_NAME, name)).uniqueResult();
		}
	}

	public static int update(User user) {
		try (Session s = sf.openSession()) {
			s.beginTransaction();
			s.update(user);
			s.getTransaction().commit();
			return 1;
		}
	}

}
