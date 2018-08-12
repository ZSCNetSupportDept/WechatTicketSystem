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

package love.sola.netsupport.api.user;

import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;

import static love.sola.netsupport.util.Checker.checkBlock;
import static love.sola.netsupport.util.Checker.checkISP;
import static love.sola.netsupport.util.Checker.checkNetAccount;
import static love.sola.netsupport.util.Checker.checkPhoneNumber;
import static love.sola.netsupport.util.Checker.checkRoom;

/**
 * @author Sola {@literal <dev@sola.love>}
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
