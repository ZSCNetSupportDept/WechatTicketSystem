/*
 * This file is part of WechatTicketSystem.
 *
 * WechatTicketSystem is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WechatTicketSystem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with WechatTicketSystem.  If not, see <http://www.gnu.org/licenses/>.
 */

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
