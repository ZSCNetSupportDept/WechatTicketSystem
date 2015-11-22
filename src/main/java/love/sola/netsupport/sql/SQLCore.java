package love.sola.netsupport.sql;

import javax.naming.InitialContext;
import javax.sql.DataSource;

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

}
