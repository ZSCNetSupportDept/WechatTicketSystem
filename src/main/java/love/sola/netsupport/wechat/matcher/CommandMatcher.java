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

package love.sola.netsupport.wechat.matcher;

import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class CommandMatcher implements WxMpMessageMatcher {

	public static Map<String, Command> inCmdUsers = new ConcurrentHashMap<>();

	Command command;

	public CommandMatcher(Command command) {
		this.command = command;
	}

	@Override
	public boolean match(WxMpXmlMessage message) {
		String fromUser = message.getFromUserName();
		if (inCmdUsers.containsKey(fromUser)) {
			return command == inCmdUsers.get(fromUser);
		} else {
			return message.getContent().matches(command.regex);
		}
	}

}
