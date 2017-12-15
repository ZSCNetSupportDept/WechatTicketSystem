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

package love.sola.netsupport.api.root;

import love.sola.netsupport.api.API;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WechatSession;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class DashBoard extends API {

    public DashBoard() {
        url = "/root/dashboard";
        access = Access.ROOT;
        authorize = Command.LOGIN;
    }

    @Override
    protected Object process(HttpServletRequest req, WxSession session) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (love.sola.netsupport.session.WxSession ws : WechatSession.list()) {
            sb.append("=====").append(ws.getId()).append("=====\n");
            Set<String> e = ws.getAttributeNames();
            for (String key : e) {
                sb.append(key).append(": ").append(ws.getAttribute(key).toString()).append("\n");
            }
        }
        return sb.toString();
    }

}
