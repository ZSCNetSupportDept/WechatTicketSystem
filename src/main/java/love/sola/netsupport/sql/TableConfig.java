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

import love.sola.netsupport.config.Settings;

import java.sql.*;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TableConfig extends SQLCore {

	public static final String KEY_SYS = "sys";

	public static Settings getSettings() {
		try (Connection conn = ds.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM settings WHERE type='" + KEY_SYS + "'");
			if (rs.next()) {
				return gson.fromJson(rs.getString("data"), Settings.class);
			}
		} catch (SQLException e) {
		}
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
