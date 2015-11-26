package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import love.sola.netsupport.wechat.matcher.CommandMatcher;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.outxmlbuilder.TextBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * ***********************************************
 * Created by Sola on 2015/11/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class RegisterHandler implements WxMpMessageHandler {

	public static final String STUDENT_ID_REGEX = "^(2012|2013|2014|2015)[0-9]{9}";

	Map<String, User> pre_confirm = new HashMap<>();
	Map<String, User> confirmed = new HashMap<>();

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager)
			throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		handle(wxMessage.getContent(), wxMessage.getFromUserName(), out);
		return out.build();
	}

	private void handle(String in, String userName, TextBuilder out) {
		if (in.matches(Command.REGISTER.getRegex())) {
			out.content("Welcome, please type your student identification number.");
			return;
		}
		if (pre_confirm.containsKey(userName)) {
			User user = pre_confirm.get(userName);
			if (in.trim().equals(user.getName())) {
				pre_confirm.remove(userName);
				confirmed.put(userName, user);
				out.content("Confirmed Success.\n" +
						"Please enter your ISP.");
			} else {
				out.content("Validate failed.\n" +
						"if you have any issue, please contact administrator.\n" +
						"Type 'q' to cancel this operation.");
			}
			return;
		}
		if (confirmed.containsKey(userName)) {
			User user = pre_confirm.get(userName);
			if (user.getIsp() == null) {
				//TODO
			}
			return;
		}
		if (in.equalsIgnoreCase("q")) {
			CommandMatcher.inCmdUsers.remove(userName);
			pre_confirm.remove(userName);
			confirmed.remove(userName);
			out.content("Operation Canceled.");
			return;
		}
		Long sid = checkStudentId(in);
		User user;
		if (sid == null || (user = TableUser.getUserById(sid)) == null) {
			out.content("Invalid student identification number. Type 'q' to cancel this operation.");
			return;
		}
		pre_confirm.put(userName, user);
		out.content("Please type your real name to validate.");
	}

	private Long checkStudentId(String studentId) {
		if (studentId.matches(STUDENT_ID_REGEX)) {
			try {
				return Long.parseLong(studentId);
			} catch (NumberFormatException ignored) { }
		}
		return null;
	}

}
