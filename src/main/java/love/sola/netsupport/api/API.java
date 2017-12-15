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

package love.sola.netsupport.api;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.wechat.Command;
import org.apache.commons.lang3.time.DateUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public abstract class API {

    public String url = null; //url
    public int access = Access.GOD_MODE; //operator's permission
    public Command authorize = null; //session check

    protected abstract Object process(HttpServletRequest req, WxSession session) throws Exception;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "url='" + url + '\'' +
                ", access=" + Access.inverseMap.get(access) +
                ", authorize=" + authorize +
                '}';
    }

    public static String getParameterWithDefault(String obj, String def) {
        return obj == null ? def : obj;
    }

    public static Date getParameterAsDate(String obj, Date def) {
        return obj == null ? def : new Date(Long.valueOf(obj));
    }

    public static Date getToday() {
        return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
    }

    public static Date getDay(Date date) {
        return DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
    }

}
