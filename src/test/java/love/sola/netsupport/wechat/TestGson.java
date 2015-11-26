package love.sola.netsupport.wechat;

import com.google.gson.Gson;
import love.sola.netsupport.config.Settings;
import org.junit.Test;

/**
 * ***********************************************
 * Created by Sola on 2015/11/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestGson {

	@Test
	public void testGson() {
		Gson gson = new Gson();
		Settings settings = new Settings();
		settings.Wechat_AppId = "*";
		settings.Wechat_Secret = "*";
		settings.Wechat_Token = "*";
		settings.Wechat_AesKey = "*";
		System.out.println(gson.toJson(settings));
	}

}
