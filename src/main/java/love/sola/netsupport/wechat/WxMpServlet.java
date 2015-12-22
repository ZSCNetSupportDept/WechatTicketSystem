package love.sola.netsupport.wechat;

import love.sola.netsupport.config.Settings;
import love.sola.netsupport.wechat.handler.RegisterHandler;
import love.sola.netsupport.wechat.matcher.CheckSpamMatcher;
import love.sola.netsupport.wechat.matcher.RegisterMatcher;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static love.sola.netsupport.config.Lang.lang;

/**
 * ***********************************************
 * Created by Sola on 2015/11/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "WxMpServlet", urlPatterns = "/wechat", loadOnStartup = 99)
public class WxMpServlet extends HttpServlet {

	public static WxMpServlet instance;
	protected WxMpInMemoryConfigStorage config;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;
	protected CheckSpamMatcher checkSpamMatcher;

	public WxMpServlet() {
		instance = this;
	}

	@Override
	public void init() throws ServletException {
		super.init();

		config = new WxMpInMemoryConfigStorage();
		config.setAppId(Settings.I.Wechat_AppId);
		config.setSecret(Settings.I.Wechat_Secret);
		config.setToken(Settings.I.Wechat_Token);
		config.setAesKey(Settings.I.Wechat_AesKey);

		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);

		checkSpamMatcher = new CheckSpamMatcher();
		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule()
				.async(false)
				.msgType("event")
				.event("subscribe")
				.handler((wxMessage, context, wxMpService1, sessionManager)
						-> WxMpXmlOutMessage.TEXT()
						.fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName())
						.content(lang("Event_Subscribe")).build())
				.end();
		wxMpMessageRouter.rule()
				.async(false)
				.msgType("text")
				.matcher(new CheckSpamMatcher())
				.handler((wxMessage, context, wxMpService1, sessionManager)
						-> WxMpXmlOutMessage.TEXT()
						.fromUser(wxMessage.getToUserName())
						.toUser(wxMessage.getFromUserName())
						.content(lang("Message_Spam")).build())
				.end();
		wxMpMessageRouter.rule()
				.async(false)
				.msgType("text")
				.matcher(new RegisterMatcher())
				.handler(new RegisterHandler())
				.end();
		try {
			registerCommands(wxMpMessageRouter);
		} catch (IllegalAccessException | InstantiationException e) {
			throw new ServletException(e);
		}
	}

	public static void registerCommands(WxMpMessageRouter router) throws IllegalAccessException, InstantiationException {
		for (Command c : Command.values()) {
			WxMpMessageHandler handler = c.handler.newInstance();
			router.rule().async(false).msgType("text").rContent(c.regex).handler(handler).end();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		String signature = request.getParameter("signature");
		String nonce = request.getParameter("nonce");
		String timestamp = request.getParameter("timestamp");

		if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
			// Signature fail
			response.getWriter().println(lang("Access_Denied"));
			return;
		}

		String echostr = request.getParameter("echostr");
		if (StringUtils.isNotBlank(echostr)) {
			// validate request
			response.getWriter().println(echostr);
			return;
		}

		String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ? "raw" : request.getParameter("encrypt_type");

		if ("raw".equals(encryptType)) {
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			if (outMessage == null) {
				outMessage = WxMpXmlOutMessage.TEXT()
						.fromUser(inMessage.getToUserName())
						.toUser(inMessage.getFromUserName())
						.content(lang("Invalid_Operation"))
						.build();
			}
			response.getWriter().write(outMessage.toXml());
			return;
		}

		if ("aes".equals(encryptType)) {
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), config, timestamp, nonce, msgSignature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			if (outMessage == null) {
				outMessage = WxMpXmlOutMessage.TEXT()
						.fromUser(inMessage.getToUserName())
						.toUser(inMessage.getFromUserName())
						.content(lang("Invalid_Operation"))
						.build();
			}
			response.getWriter().write(outMessage.toEncryptedXml(config));
			return;
		}

		response.getWriter().println(lang("Unknown_Encrypt_Type"));
		return;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

}
