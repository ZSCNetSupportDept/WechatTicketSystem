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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import love.sola.netsupport.config.Settings;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class MapSessionRepository {

	private final LoadingCache<String, MapSession> sessions;

	public MapSessionRepository() {
		this(CacheBuilder.newBuilder()
				.concurrencyLevel(4)
				.maximumSize(65535)
				.expireAfterAccess(Settings.I.User_Session_Max_Inactive, TimeUnit.SECONDS)
				.build(new CacheLoader<String, MapSession>() {
					       @Override
					       public MapSession load(@Nonnull String key) throws Exception {
						       return new MapSession(key);
					       }
				       }
				)
		);
	}

	public MapSessionRepository(LoadingCache<String, MapSession> sessions) {
		Validate.notNull(sessions);
		this.sessions = sessions;
	}

	public void save(MapSession session) {
		sessions.put(session.getId(), session);
	}

	public MapSession getSession(String id) {
		MapSession saved = sessions.getIfPresent(id);
		if (saved == null) {
			return null;
		}
		if (saved.isInvalidated()) {
			delete(saved.getId());
			return null;
		}
		return saved;
	}

	public void delete(String id) {
		sessions.invalidate(id);
	}

	public MapSession createSession() {
		MapSession session = new MapSession();
		save(session);
		return session;
	}

	public Map<String, MapSession> asMap() {
		return sessions.asMap();
	}

}
