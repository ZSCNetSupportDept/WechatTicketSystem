package love.sola.netsupport.wechat.matcher;

import love.sola.netsupport.sql.TableUser;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class RegisterMatcher implements WxMpMessageMatcher {

	@Override
	public boolean match(WxMpXmlMessage message) {
		return TableUser.getByWechat(message.getFromUserName()) == null;
	}

}
