package love.sola.netsupport.session;

import java.util.Set;

/**
 * @author Sola
 */
public interface WxSession {

	String getId();

	<T> T getAttribute(String name);

	Set<String> getAttributeNames();

	void setAttribute(String name, Object value);

	void removeAttribute(String name);

	void invalidate();

}