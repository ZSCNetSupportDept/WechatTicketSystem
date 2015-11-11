package love.sola.netsupport.sql;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class SQLCore {

	public static DataSource ds;

	static {
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:comp/env/jdbc/netsupport");
			ds.setLoginTimeout(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void free(Connection conn) {
		if (conn != null) try { conn.close(); } catch (SQLException e) { }
	}

}
