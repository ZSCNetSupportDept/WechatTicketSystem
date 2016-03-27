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
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
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
