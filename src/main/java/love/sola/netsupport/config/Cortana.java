/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

package love.sola.netsupport.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Cortana {

	public static List<Compiled> entries;

	public static void load() {
		InputStream in = Lang.class.getClassLoader().getResourceAsStream("cortana.yml");
		RawConfig root = new Yaml().loadAs(in, RawConfig.class);
	}

	static class Compiled {
		Pattern[] patterns;
		String[] replies;
	}

	public static class Rule {
		String[] regexp;
		String[] replies;

		public Rule() {
		}
	}

	public static class RawConfig {
		Map<String, Rule> rules;

		public RawConfig() {
		}
	}

}
