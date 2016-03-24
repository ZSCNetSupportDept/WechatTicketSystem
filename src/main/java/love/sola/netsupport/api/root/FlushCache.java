package love.sola.netsupport.api.root;

import love.sola.netsupport.api.API;
import love.sola.netsupport.api.Error;
import love.sola.netsupport.enums.Access;
import love.sola.netsupport.sql.TableUser;
import love.sola.netsupport.wechat.Command;
import me.chanjar.weixin.common.session.WxSession;

import javax.servlet.http.HttpServletRequest;

/**
 * ***********************************************
 * Created by Sola on 2015/12/15.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class FlushCache extends API {

	public FlushCache() {
		url = "/root/flushcache";
		access = Access.ROOT;
		authorize = Command.LOGIN;
	}

	@Override
	protected Object process(HttpServletRequest req, WxSession session) throws Exception {
		TableUser.flushCache();
		return Error.OK;
	}

}
