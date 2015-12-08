package love.sola.netsupport.wechat.matcher;

import love.sola.netsupport.pojo.User;
import love.sola.netsupport.sql.TableUser;
import me.chanjar.weixin.mp.api.WxMpMessageMatcher;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;

import java.util.HashSet;
import java.util.Set;

/**
 * ***********************************************
 * Created by Sola on 2015/11/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class RegisterMatcher implements WxMpMessageMatcher {

	public static Set<String> registered = new HashSet<>();

	@Override
	public boolean match(WxMpXmlMessage message) {
		String fromUser = message.getFromUserName();
		if (registered.contains(fromUser)) {
			return false;
		} else {
			User u = TableUser.getUserByWechat(fromUser);
			if (u != null) registered.add(u.getWechatId());
			return u == null;
		}
	}

}
