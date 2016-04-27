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
