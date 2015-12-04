package love.sola.netsupport.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * ***********************************************
 * Created by Sola on 2015/12/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "sid")
	private User user;
	private String description;
	private Date submitTime;
	private String remark;
	private Date updateTime;
	@ManyToOne(optional = true)
	@JoinColumn(name = "opsid")
	private User operator;
	private int status;

}
