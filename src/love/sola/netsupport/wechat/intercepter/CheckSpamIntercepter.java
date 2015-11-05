package love.sola.netsupport.wechat.intercepter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageInterceptor;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ***********************************************
 * Created by Sola on 2015/11/4.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class CheckSpamIntercepter implements WxMpMessageInterceptor {

	private static Cache<String, Long> cache = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.weakKeys()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();

	@Override
	public boolean intercept(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

		return false;
	}

}
