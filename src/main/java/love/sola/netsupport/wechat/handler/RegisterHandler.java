package love.sola.netsupport.wechat.handler;

import love.sola.netsupport.enums.ISPType;
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

	public static final String STUDENT_ID_REGEX = "^(2010|2012|2013|2014|2015)[0-9]{9}";

	Map<String, User> confirmed = new HashMap<>();
	Map<String, RegisterStep> steps = new HashMap<>();

	@Override

	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager)
			throws WxErrorException {
		TextBuilder out = WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUserName()).toUser(wxMessage.getFromUserName());
		handle(wxMessage.getContent(), wxMessage.getFromUserName(), out);
		return out.build();
	}

	private void handle(String in, String userName, TextBuilder out) {
		if (in.equalsIgnoreCase("q")) {
			CommandMatcher.inCmdUsers.remove(userName);
			confirmed.remove(userName);
			steps.remove(userName);
			out.content("Operation canceled.");
			return;
		}
		if (!steps.containsKey(userName)) {
			out.content("Welcome, please type your student identification number.");
			CommandMatcher.inCmdUsers.put(userName, Command.REGISTER);
			steps.put(userName, RegisterStep.STUDENT_ID);
			return;
		}
		RegisterStep step = steps.get(userName);
		if (step == RegisterStep.STUDENT_ID) {
			Long sid = checkStudentId(in);
			User user;
			if (sid == null || (user = TableUser.getUserById(sid)) == null) {
				out.content("Invalid student identification number. Type 'q' to cancel this operation.");
				return;
			}
			out.content("Please type your real name to validate.");
			confirmed.put(userName, user);
			steps.put(userName, RegisterStep.NAME);
			return;
		}
		User user = confirmed.get(userName);
		if (step == RegisterStep.NAME) {
			if (in.trim().equals(user.getName())) {
				out.content("Confirmed success.\n" +
						"Please enter your ISP.\n" +
						"1-Telecom 2-Unicom 3-ChinaMobile");
				steps.put(userName, RegisterStep.ISP);
			} else {
				out.content("Validate failed.\n" +
						"If you have any issue, please contact administrator.\n" +
						"Type 'q' to cancel this operation.");
			}
			return;
		}
		if (step == RegisterStep.ISP) {
			ISPType type = checkISP(in);
			if (type == null) {
				out.content("Invalid ISP. Please retype your ISP.");
			} else {
				user.setIsp(type);
				out.content("Success.\n" +
						"Please enter your net account.");
				steps.put(userName, RegisterStep.NET_ACCOUNT);
			}
			return;
		}
		if (step == RegisterStep.NET_ACCOUNT) {
			String account = checkNetAccount(in);
			if (account == null) {
				out.content("Invalid account. Please retype your net account.");
			} else {
				user.setNetAccount(account);
				steps.put(userName, RegisterStep.COMPLETE);
			}
		}
		if (step == RegisterStep.COMPLETE) {
			user.setWechatId(userName);
			TableUser.updateUser(user);
			CommandMatcher.inCmdUsers.remove(userName);
			confirmed.remove(userName);
			steps.remove(userName);
			out.content("Congratulations!\n" +
					"You has successful registered!\n" +
					"Please enjoy our service.");
		}
	}

	private Long checkStudentId(String studentId) {
		if (studentId.matches(STUDENT_ID_REGEX)) {
			try {
				return Long.parseLong(studentId);
			} catch (NumberFormatException ignored) {
			}
		}
		return null;
	}

	private ISPType checkISP(String isp) {
		try {
			return ISPType.fromId(Integer.parseInt(isp));
		} catch (NumberFormatException ignored) {
		}
		return null;
	}

	private String checkNetAccount(String account) {
		return account;
	}

	enum RegisterStep {
		STUDENT_ID,
		NAME,
		ISP,
		NET_ACCOUNT,
		COMPLETE,
		;
	}

}
