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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import love.sola.netsupport.config.Settings;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import java.util.concurrent.TimeUnit;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class CheckSpamMatcher implements WxMpMessageMatcher {

	private class ValueLoader extends CacheLoader<String, Long> {
		@Override
		public Long load(String key) throws Exception {
			return System.currentTimeMillis() + Settings.I.Check_Spam_Interval;
		}
	}

	private LoadingCache<String, Long> cache = CacheBuilder.newBuilder()
			.concurrencyLevel(4)
			.maximumSize(4096)
			.expireAfterWrite(Settings.I.Check_Spam_Cache_Expire_Time, TimeUnit.SECONDS)
			.build(new ValueLoader());

	@Override
	public boolean match(WxMpXmlMessage wxMessage) {
		Long l = cache.getIfPresent(wxMessage.getFromUserName());
		if (l != null && l > System.currentTimeMillis()) {
			return true;
		}
		cache.refresh(wxMessage.getFromUserName());
		return false;
	}

}
