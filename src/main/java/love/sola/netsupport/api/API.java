package love.sola.netsupport.api;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2016/2/27.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public abstract class API {

	public String url = null; //url
	public int access = Access.GOD_MODE; //operator's permission
	public Command authorize = null; //session check

	protected abstract Object process(HttpServletRequest req, WxSession session) throws Exception;

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"url='" + url + '\'' +
				", access=" + Access.inverseMap.get(access) +
				", authorize=" + authorize +
				'}';
	}

}
