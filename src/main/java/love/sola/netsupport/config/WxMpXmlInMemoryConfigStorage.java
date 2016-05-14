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

package love.sola.netsupport.config;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.ToString;
import me.chanjar.weixin.common.util.xml.XStreamInitializer;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;

import java.io.InputStream;

/**
 * @author chanjarster
 */
@XStreamAlias("wechat-config")
@ToString
public class WxMpXmlInMemoryConfigStorage extends WxMpInMemoryConfigStorage {

	@SuppressWarnings("unchecked")
	public static <T> T fromXml(Class<T> clazz, InputStream is) {
		XStream xstream = XStreamInitializer.getInstance();
		xstream.alias("wechat-config", clazz);
		xstream.processAnnotations(clazz);
		return (T) xstream.fromXML(is);
	}

}
