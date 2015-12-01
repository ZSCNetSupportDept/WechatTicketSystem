package love.sola.netsupport.sql;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import love.sola.netsupport.config.Settings;
import love.sola.netsupport.enums.Block;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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


	public static User getUserById(long id) {
		if (id < 0) return null;
		try {
			return cache_id.get(id);
		} catch (ExecutionException e) {
			return null;
		}
	}

	public static User getUserByWechat(String wechat) {
		if (wechat == null) return null;
		try {
			return cache_wechat.get(wechat);
		} catch (ExecutionException e) {
			return null;
		}
	}

	public static User getUserByName(String name) {
		if (name == null) return null;
		User u = getUserByName0(name);
		if (u != null) {
			cache_id.put(u.getId(), u);
			if (u.getWechatId()!=null) cache_wechat.put(u.getWechatId(), u);
		}
		return u;
	}

	public static int updateUser(User user) {
		int r = updateUser0(user);
		if (r > 0) {
			cache_id.put(user.getId(), user);
			if (user.getWechatId() != null) cache_wechat.put(user.getWechatId(), user);
		}
		return r;
	}

	private static LoadingCache<Long, User> cache_id = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.maximumSize(2048)
			.expireAfterAccess(Settings.I.User_Caching_Time, TimeUnit.SECONDS)
			.build(new IdLoader());

	private static LoadingCache<String, User> cache_wechat = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.maximumSize(2048)
			.expireAfterAccess(Settings.I.User_Caching_Time, TimeUnit.SECONDS)
			.build(new WechatLoader());


	private static class IdLoader extends CacheLoader<Long, User> {
		@Override
		public User load(Long key) throws Exception {
			User u = getUserById0(key);
			System.out.println("Queried user: " + u);
			if (u == null) throw new UserNotFoundException();
			if (u.getWechatId() != null) cache_wechat.put(u.getWechatId(), u);
			return u;
		}
	}

	private static class WechatLoader extends CacheLoader<String, User> {
		@Override
		public User load(String key) throws Exception {
			User u = getUserByWechat0(key);
			System.out.println("Queried user: " + u);
			if (u == null) throw new UserNotFoundException();
			cache_id.put(u.getId(), u);
			return u;
		}
	}

	public static class UserNotFoundException extends Exception { }


	private static User getUserById0(long id) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE id=?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return constructUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	private static User getUserByWechat0(String wechat) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE wechat=?");
			ps.setString(1, wechat);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return constructUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static User getUserByName0(String name) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE name=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return constructUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int updateUser0(User user) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE user_info SET " +
					COLUMN_WECHAT + "=?," +
					COLUMN_ISP + "=?," +
					COLUMN_NET_ACCOUNT + "=?," +
					COLUMN_BLOCK + "=?," +
					COLUMN_ROOM + "=?," +
					COLUMN_PHONE + "=? " +
					"WHERE id=?");
			ps.setString(1, user.getWechatId());
			ps.setInt(2, user.getIsp().id);
			ps.setString(3, user.getNetAccount());
			ps.setInt(4, user.getBlock().id);
			ps.setInt(5, user.getRoom());
			ps.setLong(6, user.getPhone());
			ps.setLong(7, user.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private static User constructUser(ResultSet rs) throws SQLException {
		return new User(
				rs.getLong(COLUMN_ID),
				rs.getString(COLUMN_NAME),
				ISP.fromId(rs.getInt(COLUMN_ISP)),
				rs.getString(COLUMN_NET_ACCOUNT),
				rs.getString(COLUMN_WECHAT),
				Block.fromId(rs.getInt(COLUMN_BLOCK)),
				rs.getInt(COLUMN_ROOM),
				rs.getInt(COLUMN_PHONE)
		);
	}

}
