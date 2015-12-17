package love.sola.netsupport.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ***********************************************
 * Created by Sola on 2015/11/30.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Lang {

	public static Map<String, String> messages;
	public static Map<String, AutoReply> replies;
	public static Map<String, MessageFormat> format_cache = new HashMap<>(32);

	static {
		try (InputStream in = Lang.class.getClassLoader().getResourceAsStream("lang.yml")) {
			//noinspection unchecked
			messages = new Yaml().loadAs(in, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static void loadReplies() {
		try (InputStream in = Lang.class.getClassLoader().getResourceAsStream("replies.yml")) {
			Map<String, Object> yaml = (Map<String, Object>) new Yaml().load(in);


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Data
	@AllArgsConstructor
	public static class AutoReply {
		Pattern[] regex;
		String[] replies;
	}

}
