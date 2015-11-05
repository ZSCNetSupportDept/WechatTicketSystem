package love.sola.netsupport.wechat;

import love.sola.netsupport.wechat.handler.RegisterHandler;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ***********************************************
 * Created by Sola on 2015/11/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@WebServlet(name = "WxMpServlet", urlPatterns = "/wechattest", loadOnStartup = 2)
public class WxMpServlet extends HttpServlet {

	public static WxMpServlet instance;
	protected WxMpInMemoryConfigStorage config;
	protected WxMpService wxMpService;
	protected WxMpMessageRouter wxMpMessageRouter;

	public WxMpServlet() {
		instance = this;
	}

	@Override
	public void init() throws ServletException {
		super.init();

		config = new WxMpInMemoryConfigStorage();
		config.setAppId("*****");
		config.setSecret("*****");
		config.setToken("*****");
//		config.setAesKey("SolaAES");

		wxMpService = new WxMpServiceImpl();
		wxMpService.setWxMpConfigStorage(config);
		wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
		wxMpMessageRouter.rule().async(false).rContent("^").handler(new RegisterHandler()).end();
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
			response.getWriter().println("Access Denied");
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
			response.getWriter().write(outMessage.toXml());
			return;
		}

		if ("aes".equals(encryptType)) {
			String msgSignature = request.getParameter("msg_signature");
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), config, timestamp, nonce, msgSignature);
			WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
			response.getWriter().write(outMessage.toEncryptedXml(config));
			return;
		}

		response.getWriter().println("Unknown encrypt-type");
		return;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
