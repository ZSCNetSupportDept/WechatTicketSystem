package love.sola.netsupport.api;

import love.sola.netsupport.enums.Access;
import love.sola.netsupport.session.WxSession;
import love.sola.netsupport.wechat.Command;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sola {@literal <dev@sola.love>}
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
