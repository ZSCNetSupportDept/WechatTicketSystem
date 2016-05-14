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

import javax.servlet.http.HttpServletRequest;

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

}
