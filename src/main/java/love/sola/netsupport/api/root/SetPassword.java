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
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.pojo.Operator;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.util.Crypto;
import love.sola.netsupport.wechat.Command;
import org.hibernate.Session;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class SetPassword extends API {

	public SetPassword() {
		url = "/root/setpass";
		access = Access.ROOT;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String id = req.getParameter("id");
		String pass = req.getParameter("pass");
		if (pass == null || pass.length() < 8) {
			return Error.INVALID_PARAMETER;
		}
		try (Session s = SQLCore.sf.openSession()) {
			s.beginTransaction();
			Operator op = s.get(Operator.class, Integer.parseInt(id));
			if (op == null) {
				return Error.OPERATOR_NOT_FOUND;
			}
			op.setPassword(Crypto.hash(pass));
			s.update(op);
			s.getTransaction().commit();
			return Error.OK;
		}
	}

}
