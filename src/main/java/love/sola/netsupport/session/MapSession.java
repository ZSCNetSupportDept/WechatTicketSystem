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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public final class MapSession implements WxSession, Serializable {

    private final String id;
    private Map<String, Object> sessionAttrs = new HashMap<String, Object>();
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = creationTime;
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

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    @Override
    public String getId() {

        return id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public boolean isInvalidated() {
        return invalidated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapSession)) return false;
        MapSession that = (MapSession) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
