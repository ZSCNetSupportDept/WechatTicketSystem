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
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
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
