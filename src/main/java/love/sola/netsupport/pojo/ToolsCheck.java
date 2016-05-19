package love.sola.netsupport.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "toolschk")
@DynamicInsert
public class ToolsCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "opsid")
	private User operator;
	private Integer block;
	@Column(name = "chktime")
	private Date checkTime = new Date();
	@OrderColumn
	private Integer status = 0;

}
