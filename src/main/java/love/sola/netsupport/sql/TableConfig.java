package love.sola.netsupport.sql;

import love.sola.netsupport.config.Settings;

import java.sql.*;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TableConfig extends SQLCore{

	public static final String KEY_SYS = "sys";

	public static Settings getSettings() {
		try (Connection conn = ds.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM settings WHERE type='" + KEY_SYS + "'");
			if (rs.next()) {
				return gson.fromJson(rs.getString("data"), Settings.class);
			}
		} catch (SQLException e) { }
		return null;
	}

	public static int saveSettings(Settings obj) {
		try (Connection conn = ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE settings SET data=? WHERE type=?");
			ps.setString(1, gson.toJson(obj));
			ps.setString(2, KEY_SYS);
			return ps.executeUpdate();
		} catch (SQLException e) {
		}
		return -1;
	}

}
