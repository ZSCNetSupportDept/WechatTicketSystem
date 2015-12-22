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

	public static final int MAX_DESC_LENGTH = 255;

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

	public int User_Session_Max_Inactive;

	//No arg constructor for Yaml.loadAs
	public Settings() { I = this; }

}
