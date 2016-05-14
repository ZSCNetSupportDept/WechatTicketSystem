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
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Sola {@literal <dev@sola.love>}
 */

@Data
@ToString(exclude = "password")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "operators")
public class Operator {

	public static final String PROPERTY_WECHAT = "wechat";

	@Id
	@Column(name = "id", nullable = false, insertable = false, updatable = false)
	private Integer id;
	@Column(name = "name", nullable = false, insertable = false, updatable = false)
	private String name;
	@Column(name = "access", nullable = false, insertable = false, updatable = false)
	private Integer access;
	@Column(name = "wechat", insertable = false, updatable = false)
	@Expose(serialize = false)
	private String wechat;
	private Integer block;
	private Integer week;
	@Expose(serialize = false)
	private String password;


	//System Accounts
	public static Operator USER_SELF;
	public static Operator ADMIN;

}
