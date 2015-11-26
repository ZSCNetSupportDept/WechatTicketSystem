package love.sola.netsupport.sql;

import love.sola.netsupport.enums.ISPType;
import love.sola.netsupport.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ***********************************************
 * Created by Sola on 2015/11/10.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TableUser extends SQLCore {

	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_NET_ACCOUNT = "netaccount";
	public static final String COLUMN_ISP = "isp";
	public static final String COLUMN_WECHAT = "wechat";

	public static User getUserByName(String name) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE name=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getLong(COLUMN_ID),
						rs.getString(COLUMN_NAME),
						rs.getString(COLUMN_NET_ACCOUNT),
						ISPType.fromId(rs.getInt(COLUMN_ISP)),
						rs.getString(COLUMN_WECHAT));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static User getUserById(long id) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE id=?");
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getLong(COLUMN_ID),
						rs.getString(COLUMN_NAME),
						rs.getString(COLUMN_NET_ACCOUNT),
						ISPType.fromId(rs.getInt(COLUMN_ISP)),
						rs.getString(COLUMN_WECHAT));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int updateUser(User user) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE user_info SET " +
					COLUMN_WECHAT + "=?," +
					COLUMN_NET_ACCOUNT + "=?," +
					COLUMN_ISP + "=? " +
					"WHERE id=?");
			ps.setString(1, user.getWechatId());
			ps.setString(2, user.getNetAccount());
			ps.setInt(3, user.getIsp().id);
			ps.setLong(4, user.getId());
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
