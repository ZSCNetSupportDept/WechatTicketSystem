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

package love.sola.netsupport.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Crypto {

	public static String hash(String pw) {
		return BCrypt.hashpw(pw, BCrypt.gensalt());
	}

	public static boolean check(String plain, String hash) {
		return BCrypt.checkpw(plain, hash);
	}

}
