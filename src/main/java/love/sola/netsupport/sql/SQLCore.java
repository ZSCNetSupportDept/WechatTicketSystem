package love.sola.netsupport.sql;

import com.google.gson.Gson;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

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
	public static Gson gson = new Gson();
	public static SessionFactory sf;
	public static ServiceRegistry sr;

	static {
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:comp/env/jdbc/netsupport");
			ds.setLoginTimeout(3);

			sr = new StandardServiceRegistryBuilder().configure().build();
			sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
