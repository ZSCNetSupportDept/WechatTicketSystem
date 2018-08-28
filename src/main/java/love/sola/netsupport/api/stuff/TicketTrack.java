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

package love.sola.netsupport.api.stuff;

import javax.servlet.http.HttpServletRequest;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketTrack extends API {

    public TicketTrack() {
        url = "/admin/tickettrack";
        access = Access.MEMBER;
        authorize = Command.LOGIN;
    }

    @Override
    protected Object process(HttpServletRequest req, WxSession session) throws Exception {
        String tid = req.getParameter("id");
        if (tid == null) {
            return Error.PARAMETER_REQUIRED;
        }
        return TableTicket.track(Integer.parseInt(tid));
    }

}
