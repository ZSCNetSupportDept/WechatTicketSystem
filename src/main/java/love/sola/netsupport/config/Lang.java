package love.sola.netsupport.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Lang {

	public static Map<String, String> messages;
	public static Map<String, MessageFormat> format_cache = new HashMap<>(32);

	static {
		InputStream in = Lang.class.getClassLoader().getResourceAsStream("lang.yml");
		//noinspection unchecked
		messages = new Yaml().loadAs(in, Map.class);
	}

	public static String lang(String key) {
		String value = messages.get(key);
		return value == null ? "!!" + key + "!!" : value;
	}

	public static String format(String key, Object... args) {
		MessageFormat cache = format_cache.get(key);
		if (cache != null) {
			return cache.format(args);
		} else {
			cache = new MessageFormat(lang(key));
			format_cache.put(key, cache);
			return cache.format(args);
		}
	}

}
