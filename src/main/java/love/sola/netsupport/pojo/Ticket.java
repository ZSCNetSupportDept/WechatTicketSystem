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
