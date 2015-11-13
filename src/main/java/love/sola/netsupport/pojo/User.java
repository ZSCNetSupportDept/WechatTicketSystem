package love.sola.netsupport.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import love.sola.netsupport.enums.ISPType;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@Data
@AllArgsConstructor
public class User {

	private final int id;
	private final String name;
	private final long studentId;
	private String netAccount;
	private ISPType isp;
	private String wechatId;

}
