package love.sola.netsupport.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "toolschk", indexes = {
		@Index(columnList = "block,chktime,status"),
		@Index(columnList = "chktime,status")
})
@DynamicInsert
public class ToolsCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "opsid", nullable = false)
	private Operator operator;
	@Column(nullable = false)
	private Integer block;
	@Column(name = "chktime", nullable = false)
	private Date checkTime = new Date();
	@ColumnDefault("0")
	private Integer status = 0;
	private String remark;

}
