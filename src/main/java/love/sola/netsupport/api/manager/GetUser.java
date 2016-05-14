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

package love.sola.netsupport.api.manager;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class GetUser extends API {

	public GetUser() {
		url = "/admin/getuser";
		access = Access.LEADER;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String id = req.getParameter("id");
		String name = req.getParameter("name");
		if ((id == null || id.isEmpty()) && (name == null || name.isEmpty())) {
			return Error.PARAMETER_REQUIRED;
		}
		if (id != null) {
			try {
				User u = TableUser.getById(Long.parseLong(id));
				if (u == null)
					return Error.USER_NOT_FOUND;
				else
					return u;
			} catch (NumberFormatException e) {
				return Error.ILLEGAL_PARAMETER;
			}
		} else {
			User u = TableUser.getByName(name);
			if (u == null)
				return Error.USER_NOT_FOUND;
			else
				return u;
		}
	}

}
