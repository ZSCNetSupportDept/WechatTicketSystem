package love.sola.netsupport.wechat.matcher;

import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ***********************************************
 * Created by Sola on 2015/11/26.
 * Don't modify this source without my agreement
 * ***********************************************
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
			return message.getContent().matches(command.getRegex());
		}
	}

}
