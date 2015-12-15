package love.sola.netsupport.sql;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import love.sola.netsupport.enums.ISP;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class SQLCore {

	public static DataSource ds;
	public static Gson gson = new GsonBuilder()
			.addSerializationExclusionStrategy(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes fieldAttributes) {
					final Expose expose = fieldAttributes.getAnnotation(Expose.class);
					return expose != null && !expose.serialize();
				}

				@Override
				public boolean shouldSkipClass(Class<?> aClass) {
					return false;
				}
			})
			.addDeserializationExclusionStrategy(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes fieldAttributes) {
					final Expose expose = fieldAttributes.getAnnotation(Expose.class);
					return expose != null && !expose.deserialize();
				}

				@Override
				public boolean shouldSkipClass(Class<?> aClass) {
					return false;
				}
			})
			.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
			.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
			.registerTypeAdapter(ISP.class, (JsonDeserializer<ISP>) (json, typeOfT, context) -> ISP.fromId(json.getAsJsonPrimitive().getAsInt()))
			.registerTypeAdapter(ISP.class, (JsonSerializer<ISP>) (src, typeOfSrc, context) -> new JsonPrimitive(src.id))
			.create();
	public static SessionFactory sf;
	public static ServiceRegistry sr;

	static {
		try {
			InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup("java:comp/env/jdbc/netsupport");
			ds.setLoginTimeout(3);

			sr = new StandardServiceRegistryBuilder().configure().build();
			sf = new MetadataSources(sr).buildMetadata().buildSessionFactory();

			TableOperator.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
