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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import love.sola.netsupport.sql.TableTicket;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Ticket {

	public static final String PROPERTY_USER = "user";
	public static final String PROPERTY_STATUS = "status";
	public static final String PROPERTY_SUBMIT_TIME = "submitTime";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(optional = false)
	@JoinColumn(name = TableTicket.COLUMN_SID)
	private User user;
	private String description;
	@Column(name = TableTicket.COLUMN_SUBMIT_TIME, insertable = false, updatable = false)
	private Date submitTime;
	private String remark;
	private Date updateTime;
	@ManyToOne(optional = true)
	@JoinColumn(name = TableTicket.COLUMN_OPSID)
	private Operator operator;
	private Integer status;

}
