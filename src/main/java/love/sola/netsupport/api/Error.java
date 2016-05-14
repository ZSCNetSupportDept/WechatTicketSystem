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

import static love.sola.netsupport.config.Lang.lang;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Error {

	public static final Error ALREADY_SUBMITTED = new Error(1);
	public static final Object OK = new Object();
	public static final Error PARAMETER_REQUIRED = new Error(-1);
	public static final Error ILLEGAL_PARAMETER = new Error(-2);
//	public static final Error REQUEST_FAILED = new Error(-3); REMOVED
	public static final Error LENGTH_LIMIT_EXCEEDED = new Error(-4);
	public static final Error INVALID_PARAMETER = new Error(-5);
	public static final Error USER_NOT_FOUND = new Error(-11);
	public static final Error TICKET_NOT_FOUND = new Error(-12);
	public static final Error OPERATOR_NOT_FOUND = new Error(-13);
	public static final Error UNAUTHORIZED = new Error(-20);
	public static final Error WRONG_PASSWORD = new Error(-22);
	public static final Error PERMISSION_DENIED = new Error(-24);
	public static final Error INTERNAL_ERROR = new Error(-90);
	public static final Error DATABASE_ERROR = new Error(-91);

	public int errCode;
	public String errMsg;

	private Error(int code) {
		this(code, lang("ERR_" + code));
	}

	public Error(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	public Error withMsg(String msg) {
		return new Error(errCode, msg);
	}

}