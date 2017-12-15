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
