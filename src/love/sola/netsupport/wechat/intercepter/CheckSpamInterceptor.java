package love.sola.netsupport.wechat.intercepter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
public class CheckSpamInterceptor implements WxMpMessageInterceptor {

	private static class ValueLoader extends CacheLoader<String, Long> {
		@Override
		public Long load(String key) throws Exception {
			return System.currentTimeMillis(); //TODO: CONFIGURATION
		}
	}

	private static LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.weakKeys()
			.maximumSize(4096)
			.expireAfterWrite(5, TimeUnit.SECONDS)
			.build(new ValueLoader());

	@Override
	public boolean intercept(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
		if (cache.getIfPresent(wxMessage.getFromUserName()) != null) {

		}
		return false;
	}

}
