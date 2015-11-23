package love.sola.netsupport.config;

import lombok.ToString;

/**
 * ***********************************************
 * Created by Sola on 2015/11/23.
 * Don't modify this source without my agreement
 * ***********************************************
 */
@ToString
public class Settings {

	public static Settings I;

	// -------------------------------------------- //
	// CONFIGURATIONS
	// -------------------------------------------- //
	public String Wechat_AppId;
	public String Wechat_Secret;
	public String Wechat_Token;
	public String Wechat_AesKey;


	//No arg constructor for Yaml.loadAs
	public Settings() {
		I = this;
	}

}
