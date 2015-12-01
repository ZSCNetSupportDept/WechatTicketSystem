package love.sola.netsupport.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import love.sola.netsupport.enums.Block;
import love.sola.netsupport.enums.ISP;

/**
 * ***********************************************
 * Created by Sola on 2014/8/20.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@Data
@AllArgsConstructor
public class User {

	private final long id;
	private final String name;
	private ISP isp;
	private String netAccount;
	private String wechatId;
	private Block block;
	private int room;
	private long phone;

}
