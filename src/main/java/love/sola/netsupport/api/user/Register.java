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

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.enums.Attribute;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.pojo.User;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.sql.SQLCore;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.WxMpServlet;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static love.sola.netsupport.util.Checker.*;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Register extends API {

	public Register() {
		url = "/register";
		access = Access.GUEST;
		authorize = Command.REGISTER;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		String wechat = session.getAttribute(Attribute.WECHAT);
		if (wechat == null) {
			return Error.UNAUTHORIZED;
		}
		ISP isp = checkISP(req.getParameter("isp"));
		int block = checkBlock(req.getParameter("block"));
		return register(
				checkStudentId(req.getParameter("sid")),
				req.getParameter("name"),
				isp,
				checkNetAccount(req.getParameter("username"), isp),
				block,
				checkRoom(req.getParameter("room"), block),
				checkPhoneNumber(req.getParameter("phone")),
				wechat);
	}

	private Object register(long sid, String name, ISP isp, String netAccount, int block, int room, long phone, String wechat) {
		if (sid == -1) return Error.INVALID_PARAMETER.withMsg("Invalid_Student_Id");
		if (name == null) return Error.INVALID_PARAMETER.withMsg("Invalid_Name");
		if (isp == null) return Error.INVALID_PARAMETER.withMsg("Invalid_ISP");
		if (netAccount == null) return Error.INVALID_PARAMETER.withMsg("Invalid_Account");
		if (block == -1) return Error.INVALID_PARAMETER.withMsg("Invalid_Block");
		if (room == -1) return Error.INVALID_PARAMETER.withMsg("Invalid_Room");
		if (phone == -1) return Error.INVALID_PARAMETER.withMsg("Invalid_Phone_Number");
		User user = TableUser.getById(sid);
		if (user == null) return Error.INVALID_PARAMETER.withMsg("Invalid_Student_Id");
		if (!user.getName().equals(name)) return Error.INVALID_PARAMETER.withMsg("Invalid_Name");
		if (user.getWechatId() != null) return Error.INVALID_PARAMETER.withMsg("User_Already_Registered");
		user.setIsp(isp);
		user.setNetAccount(netAccount);
		user.setBlock(block);
		user.setRoom(room);
		user.setPhone(phone);
		user.setWechatId(wechat);
		try {
			TableUser.update(user);
		} catch (ConstraintViolationException e) {
			String dupKey = e.getConstraintName();
			return Error.INVALID_PARAMETER.withMsg("Duplicated_" + dupKey.toUpperCase()); // PHONE ACCOUNT WECHAT
		}
		// FIXME: 2015/12/30 Temporary converter
		converterWithRetry(user);
		return Error.OK;
	}

	public static void converterWithRetry(User u) {
		Throwable last = null;
		for (int i = 0; i < 3; i++) {
			try {
				converter(u);
				return;
			} catch (WxErrorException | SQLException e) {
				last = e;
			}
		}
		last.printStackTrace();
		try {
			WxMpServlet.instance.wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(u.getWechatId()).content("数据转换失败").build());
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

	public static void converter(User u) throws WxErrorException, SQLException {
		try (Connection conn = SQLCore.ds.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT wechat FROM `convert` WHERE id=?");
			ps.setLong(1, u.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				WxMpServlet.instance.wxMpService
						.userUpdateGroup(u.getWechatId(), 100L);
				String old = rs.getString(1);
				ps = conn.prepareStatement("UPDATE `operators` SET wechat=? WHERE wechat=?");
				ps.setString(1, u.getWechatId());
				ps.setString(2, old);
				if (ps.executeUpdate() == 1) {
					WxMpServlet.instance.wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(u.getWechatId()).content("数据转换成功").build());
				} else {
					WxMpServlet.instance.wxMpService.customMessageSend(WxMpCustomMessage.TEXT().toUser(u.getWechatId()).content("已进行过数据转换").build());
				}
			}
		}
	}

}
