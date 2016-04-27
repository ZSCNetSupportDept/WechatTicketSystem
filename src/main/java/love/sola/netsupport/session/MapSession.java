/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package love.sola.netsupport.session;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@EqualsAndHashCode(of = "id")
public final class MapSession implements WxSession, Serializable {

	@Getter
	private final String id;
	private Map<String, Object> sessionAttrs = new HashMap<String, Object>();
	@Getter
	private long creationTime = System.currentTimeMillis();
	@Getter
	@Setter
	private long lastAccessedTime = creationTime;
	@Getter
	private boolean invalidated = false;

	/**
	 * Creates a new instance with a secure randomly generated identifier.
	 */
	public MapSession() {
		this(UUID.randomUUID().toString());
	}

	/**
	 * Creates a new instance with the specified id. This is preferred to the
	 * default constructor when the id is known to prevent unnecessary consumption on
	 * entropy which can be slow.
	 *
	 * @param id the identifier to use
	 */
	public MapSession(String id) {
		this.id = id;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String attributeName) {
		return (T) sessionAttrs.get(attributeName);
	}

	public Set<String> getAttributeNames() {
		return sessionAttrs.keySet();
	}

	public void setAttribute(String attributeName, Object attributeValue) {
		if (attributeValue == null) {
			removeAttribute(attributeName);
		} else {
			sessionAttrs.put(attributeName, attributeValue);
		}
	}

	public void removeAttribute(String attributeName) {
		sessionAttrs.remove(attributeName);
	}

	public void invalidate() {
		invalidated = true;
	}

}