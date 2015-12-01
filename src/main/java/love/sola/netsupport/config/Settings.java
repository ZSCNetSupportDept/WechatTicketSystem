package love.sola.netsupport.config;

import lombok.ToString;
import love.sola.netsupport.sql.TableConfig;

/**
 * ***********************************************
 * Created by Sola on 2015/11/23.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@ToString
public class Settings {

	public static Settings I;

	static {
		I = TableConfig.getSettings();
	}

	// -------------------------------------------- //
	// CONFIGURATIONS
	// -------------------------------------------- //
	public String Wechat_AppId;
	public String Wechat_Secret;
	public String Wechat_Token;
	public String Wechat_AesKey;

	public int Check_Spam_Cache_Expire_Time;
	public int Check_Spam_Interval;

	public int User_Caching_Time;

	public int User_Register_Timeout;
	public int User_Command_Timeout;

	//No arg constructor for Yaml.loadAs
	public Settings() { I = this; }

}
