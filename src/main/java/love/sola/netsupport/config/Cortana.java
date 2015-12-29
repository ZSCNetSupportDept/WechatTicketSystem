package love.sola.netsupport.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ***********************************************
 * Created by Sola on 2015/12/29.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Cortana {


	public static List<Compiled> entries;

	public static void load() {
		InputStream in = Lang.class.getClassLoader().getResourceAsStream("cortana.yml");
		Map<String, Object> root = (Map<String, Object>) new Yaml().load(in);

	}


	public static class Compiled {
		Pattern[] patterns;
		String[] replies;
	}

}
