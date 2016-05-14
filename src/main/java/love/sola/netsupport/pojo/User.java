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

package love.sola.netsupport.pojo;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.sola.netsupport.enums.ISP;
import love.sola.netsupport.enums.ISPConverter;

import javax.persistence.*;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_WECHAT = "wechatId";
	public static final String PROPERTY_BLOCK = "block";

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Column(name = "name", updatable = false, nullable = false)
	private String name;
	@Convert(converter = ISPConverter.class)
	private ISP isp;
	@Column(name = "netaccount")
	private String netAccount;
	@Expose(serialize = false)
	@Column(name = "wechat")
	private String wechatId;
	private Integer block;
	private Integer room;
	private Long phone;


	//System Accounts
	public static User OFFICIAL_CHINA_UNICOM_XH;
	public static User OFFICIAL_CHINA_MOBILE_XH;
	public static User OFFICIAL_CHINA_MOBILE_FX;

}
