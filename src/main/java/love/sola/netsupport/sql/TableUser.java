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
	public static final String COLUMN_STUDENT_ID = "studentid";
	public static final String COLUMN_NET_ACCOUNT = "netaccount";
	public static final String COLUMN_ISP = "isp";
	public static final String COLUMN_WECHAT = "wechat";

	public static User getUserByName(String name) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE name=?");
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt(COLUMN_ID),
						rs.getString(COLUMN_NAME),
						rs.getLong(COLUMN_STUDENT_ID),
						rs.getString(COLUMN_NET_ACCOUNT),
						ISPType.fromId(rs.getInt(COLUMN_ISP)),
						rs.getString(COLUMN_WECHAT));
			}
		} catch (SQLException e) {
		} finally { free(conn); }
		return null;
	}

	public static User getUserById(int id) {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM user_info WHERE id=?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt(COLUMN_ID),
						rs.getString(COLUMN_NAME),
						rs.getLong(COLUMN_STUDENT_ID),
						rs.getString(COLUMN_NET_ACCOUNT),
						ISPType.fromId(rs.getInt(COLUMN_ISP)),
						rs.getString(COLUMN_WECHAT));
			}
		} catch (SQLException e) {
		} finally { free(conn); }
		return null;
	}

}
