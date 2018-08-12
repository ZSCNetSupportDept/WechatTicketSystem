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
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableTicket;
import love.sola.netsupport.wechat.Command;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class TicketLookup extends API {

    public TicketLookup() {
        url = "/admin/ticketlookup";
        access = Access.MEMBER;
        authorize = Command.LOGIN;
    }

    @Override
    protected Object process(HttpServletRequest req, WxSession session) throws Exception {
        Operator op = session.getAttribute(Attribute.OPERATOR);
        int block;
        if (req.getParameter("block") != null) {
            block = Integer.parseInt(req.getParameter("block"));
        } else {
            block = op.getBlock();
        }
        return TableTicket.unsolvedByBlock(block);
    }

}
