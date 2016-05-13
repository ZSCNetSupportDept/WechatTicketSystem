package love.sola.netsupport.config;

import lombok.ToString;
import love.sola.netsupport.sql.TableConfig;

/**
 * @author Sola {@literal <dev@sola.love>}
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
	public int User_Wechat_Cache_Expire_Time;

	//No arg constructor for Yaml.loadAs
	public Settings() {
		I = this;
	}

}
