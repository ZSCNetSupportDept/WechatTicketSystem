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

import java.text.SimpleDateFormat;

import love.sola.netsupport.enums.Status;
import love.sola.netsupport.pojo.Ticket;

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class ParseUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm EEE");

    public static String parseTicket(Ticket t) {
        StringBuilder sb = new StringBuilder()
                .append(lang("Ticket_Info_Id")).append(t.getId()).append("\n")
                .append(lang("Ticket_Info_Desc")).append(t.getDescription()).append("\n")
                .append(lang("Ticket_Info_Submit_Time")).append(dateFormat.format(t.getSubmitTime())).append("\n");
        if (t.getOperator() != null)
            sb.append(lang("Ticket_Info_Operator")).append(t.getOperator().getId()).append("\n");
        if (t.getRemark() != null)
            sb.append(lang("Ticket_Info_Remark")).append(t.getRemark()).append("\n");
        if (t.getUpdateTime() != null)
            sb.append(lang("Ticket_Info_Update_Time")).append(dateFormat.format(t.getUpdateTime())).append("\n");
        sb.append(lang("Ticket_Info_Status")).append(Status.getLocalized(t.getStatus()));
        return sb.toString();
    }

}
