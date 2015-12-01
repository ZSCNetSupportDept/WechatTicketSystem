package love.sola.netsupport.config;

import love.sola.netsupport.sql.TableLang;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/30.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Lang {

	public static Map<String, String> messages;
	public static Map<String, MessageFormat> format_cache = new HashMap<>(32);

	static {
		messages = new HashMap<>();
		TableLang.getLang(messages);
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
