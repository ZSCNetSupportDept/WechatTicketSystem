package love.sola.netsupport.sql;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.pojo.User;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Sola {@literal <dev@sola.love>}
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

	public static User getByName(String name) {
		try (Session s = sf.openSession()) {
			return (User) s.createCriteria(User.class).add(Restrictions.eq(User.PROPERTY_NAME, name)).uniqueResult();
		}
	}

	public static User getByWechat(String wechat) {
		try {
			User u = cache.get(wechat);
			return u == NULL_USER ? null : u;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int update(User user) {
		try (Session s = sf.openSession()) {
			s.beginTransaction();
			s.update(user);
			s.getTransaction().commit();
			cache.put(user.getWechatId(), user);
			return 1;
		}
	}

	protected static void init() {
		try (Session s = SQLCore.sf.openSession()) {
			User.OFFICIAL_CHINA_UNICOM_XH = s.get(User.class, 100104L);
			User.OFFICIAL_CHINA_MOBILE_XH = s.get(User.class, 100864L);
			User.OFFICIAL_CHINA_MOBILE_FX = s.get(User.class, 100865L);
		}
	}

	private static LoadingCache<String, User> cache = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.maximumSize(4096)
			.expireAfterAccess(Settings.I.User_Wechat_Cache_Expire_Time, TimeUnit.SECONDS)
			.build(new ValueLoader());

	private static class ValueLoader extends CacheLoader<String, User> {
		@Override
		public User load(String key) throws Exception {
			User u = TableUser.getByWechat0(key);
			return u == null ? NULL_USER : u;
		}
	}

	private static final User NULL_USER = new User();

	public static void flushCache() {
		cache.invalidateAll();
	}

	private static User getByWechat0(String wechat) {
		try (Session s = sf.openSession()) {
			return (User) s.createCriteria(User.class).add(Restrictions.eq(User.PROPERTY_WECHAT, wechat)).uniqueResult();
		}
	}

}
