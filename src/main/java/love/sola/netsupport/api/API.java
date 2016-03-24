package love.sola.netsupport.api;

import love.sola.netsupport.api.manager.GetUser;
import love.sola.netsupport.api.manager.TicketPush;
import love.sola.netsupport.api.root.DashBoard;
import love.sola.netsupport.api.root.FlushCache;
import love.sola.netsupport.api.root.SetPassword;
import love.sola.netsupport.api.stuff.TicketLog;
import love.sola.netsupport.api.stuff.TicketLookup;
import love.sola.netsupport.api.stuff.TicketTrack;
import love.sola.netsupport.api.stuff.TicketUpdate;
import love.sola.netsupport.api.user.ProfileModify;
import love.sola.netsupport.api.user.Register;
import love.sola.netsupport.api.user.TicketQuery;
import love.sola.netsupport.api.user.TicketSubmit;
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

	public static final Class[] LIST = new Class[]{
			GetUser.class,
			TicketPush.class,
			DashBoard.class,
			FlushCache.class,
			SetPassword.class,
			TicketLog.class,
			TicketLookup.class,
			TicketTrack.class,
			TicketUpdate.class,
			ProfileModify.class,
			Register.class,
			TicketQuery.class,
			TicketSubmit.class,
			CheckSession.class
	};

}
