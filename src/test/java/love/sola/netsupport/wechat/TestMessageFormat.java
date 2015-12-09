package love.sola.netsupport.wechat;

import love.sola.netsupport.config.Lang;
import org.junit.Test;

/**
 * ***********************************************
 * Created by Sola on 2015/12/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestMessageFormat {

	@Test
	public void testLang() {
		assert Lang.messages != null;
		System.out.println(Lang.messages);
	}

}
