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

package love.sola.netsupport.enums;

import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public enum ISP {

    TELECOM(1, "^1[3|4|5|6|7|8|9][0-9]{9}$"),
    UNICOM(2, "^\\w+([-+.]\\w+)*@16900\\.gd"),
    CHINAMOBILE(3, "^1[3|4|5|6|7|8|9][0-9]{9}@139\\.gd$"),
    OTHER(4, ".*"),
    ;

    private static final Map<String, ISP> NAME_MAP = new HashMap<>();
    private static final Map<Integer, ISP> ID_MAP = new HashMap<>();

    static {
        for (ISP type : values()) {
            if (type.name != null) {
                NAME_MAP.put(type.name.toLowerCase(), type);
            }
            if (type.id > 0) {
                ID_MAP.put(type.id, type);
            }
        }
    }

    public final int id;
    public final String name;
    public final String accountRegex;

    ISP(int id, String accountRegex) {
        this.id = id;
        this.name = lang("ISP_" + name());
        this.accountRegex = accountRegex;
    }

    public static ISP fromName(String name) {
        if (name == null) {
            return null;
        }
        return NAME_MAP.get(name.toLowerCase());
    }

    public static ISP fromId(int id) {
        return ID_MAP.get(id);
    }

    @Override
    public String toString() {
        return name;
    }

}
