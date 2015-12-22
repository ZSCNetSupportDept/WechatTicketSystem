package love.sola.netsupport.sql;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.service.ServiceRegistry;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
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
			.registerTypeAdapter(Command.class, (JsonDeserializer<Command>) (json, typeOfT, context) -> Command.fromId(json.getAsJsonPrimitive().getAsInt()))
			.registerTypeAdapter(Command.class, (JsonSerializer<Command>) (src, typeOfSrc, context) -> new JsonPrimitive(src.id))
			.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
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

			TableUser.init();
			TableOperator.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AuditReader getAuditReader(Session session) {
		return AuditReaderFactory.get(session);
	}

	public static class HibernateProxyTypeAdapter extends TypeAdapter<HibernateProxy> {

		public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
			@Override
			@SuppressWarnings("unchecked")
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
				return (HibernateProxy.class.isAssignableFrom(type.getRawType()) ? (TypeAdapter<T>) new HibernateProxyTypeAdapter(gson) : null);
			}
		};
		private final Gson context;

		private HibernateProxyTypeAdapter(Gson context) {
			this.context = context;
		}

		@Override
		public HibernateProxy read(JsonReader in) throws IOException {
			throw new UnsupportedOperationException("Not supported");
		}

		@SuppressWarnings({"rawtypes", "unchecked"})
		@Override
		public void write(JsonWriter out, HibernateProxy value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			// Retrieve the original (not proxy) class
			Class<?> baseType = Hibernate.getClass(value);
			// Get the TypeAdapter of the original class, to delegate the serialization
			TypeAdapter delegate = context.getAdapter(TypeToken.get(baseType));
			// Get a filled instance of the original class
			Object unproxiedValue = ((HibernateProxy) value).getHibernateLazyInitializer()
					.getImplementation();
			// Serialize the value
			delegate.write(out, unproxiedValue);
		}
	}

}
