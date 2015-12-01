package love.sola.netsupport.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/30.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TableLang extends SQLCore {

	public static void getLang(Map<String, String> lang) {
		try (Connection conn = ds.getConnection()) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM lang;");
			while (rs.next()) {
				lang.put(rs.getString("lkey"), rs.getString("lvalue"));
			}
		} catch (SQLException e) {
		}
	}

}
