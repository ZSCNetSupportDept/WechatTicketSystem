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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Status {

    public static final int UNCHECKED = 0;
    public static final int ARRANGED = 1;
    public static final int PUTOFF = 2;
    public static final int REPORTED = 4;
    public static final int ISP_HANDLED = 7;
    public static final int SOLVED = 9;

    public static final Map<Integer, String> inverseMap = new HashMap<>();

    static {
        System.out.println("Loading Status...");
        for (Field field : Status.class.getDeclaredFields()) {
            if (field.getType().isAssignableFrom(Integer.TYPE)) {
                try {
                    inverseMap.put((Integer) field.get(null), field.getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getLocalized(int status) {
        if (inverseMap.containsKey(status)) {
            return lang("STATUS_" + inverseMap.get(status));
        }
        return null;
    }

}
