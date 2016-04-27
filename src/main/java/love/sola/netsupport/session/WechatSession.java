package love.sola.netsupport.session;


import java.util.Collection;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class WechatSession {

	private static MapSessionRepository repository;

	static {
		repository = new MapSessionRepository();
	}

	public static WxSession get(String id) {
		return repository.getSession(id);
	}

	public static WxSession create() {
		return repository.createSession();
	}

	public static Collection<? extends WxSession> list() {
		return repository.asMap().values();
	}

}
