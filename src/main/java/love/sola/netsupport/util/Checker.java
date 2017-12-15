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

package love.sola.netsupport.util;

import love.sola.netsupport.enums.Block;
import love.sola.netsupport.enums.ISP;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Checker {

    public static final String STUDENT_ID_REGEX = "^(2014|2015|2016|2017)[0-9]{9}$";
    public static final String PHONE_NUMBER_REGEX = "^1[34578][0-9]{9}$";

    public static boolean hasNull(Object... v) {
        for (Object o : v) if (o == null) return true;
        return false;
    }

    public static long checkStudentId(String studentId) {
        if (studentId == null) return -1;
        if (studentId.matches(STUDENT_ID_REGEX)) {
            try {
                return Long.parseLong(studentId);
            } catch (NumberFormatException ignored) {
            }
        }
        return -1;
    }

    public static long checkPhoneNumber(String phone) {
        if (phone == null) return -1;
        if (!phone.matches(PHONE_NUMBER_REGEX)) return -1;
        try {
            return Long.parseLong(phone);
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

    public static ISP checkISP(String isp) {
        if (isp == null) return null;
        try {
            return ISP.fromId(Integer.parseInt(isp));
        } catch (NumberFormatException ignored) {
        }
        return null;
    }

    public static String checkNetAccount(String account, ISP isp) {
        if (isp == null) return null;
        if (account == null) return null;
        if (!account.matches(isp.accountRegex)) return null;
        return account;
    }

    public static int checkBlock(String block) {
        if (block == null) return -1;
        try {
            int b = Integer.parseInt(block);
            if (Block.inverseMap.containsKey(b))
                return b;
            else
                return -1;
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

    public static int checkRoom(String room, int block) {
        if (block == -1) return -1;
        if (room == null) return -1;
        try {
            Integer i = Integer.parseInt(room);
            if (Block.checkRoom(block, i))
                return i;
            else
                return -1;
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }

}
