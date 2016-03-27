package love.sola.netsupport.api.user;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

import static love.sola.netsupport.util.Checker.*;

/**
 * ***********************************************
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class ProfileModify extends API {

	public ProfileModify() {
		url = "/profilemodify";
		access = Access.USER;
		authorize = Command.PROFILE;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		User u = session.getAttribute(Attribute.USER);
		ISP isp = checkISP(req.getParameter("isp"));
		String netAccount = checkNetAccount(req.getParameter("username"), isp);
		int block = checkBlock(req.getParameter("block"));
		int room = checkRoom(req.getParameter("room"), block);
		long phone = checkPhoneNumber(req.getParameter("phone"));
		if (room == -1)
			return Error.INVALID_PARAMETER.withMsg("Invalid_Room");
		if (phone == -1)
			return Error.INVALID_PARAMETER.withMsg("Invalid_Phone_Number");
		if (netAccount == null)
			return Error.INVALID_PARAMETER.withMsg("Invalid_Account");

		u.setIsp(isp);
		u.setNetAccount(netAccount);
		u.setBlock(block);
		u.setRoom(room);
		u.setPhone(phone);
		try {
			TableUser.update(u);
		} catch (ConstraintViolationException e) {
			String dupKey = e.getConstraintName();
			return Error.INVALID_PARAMETER.withMsg("Duplicated_" + dupKey.toUpperCase());
		}
		session.invalidate();
		return Error.OK;
	}
}
