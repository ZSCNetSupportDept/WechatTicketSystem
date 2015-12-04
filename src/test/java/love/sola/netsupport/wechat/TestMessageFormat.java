package love.sola.netsupport.wechat;

import org.junit.Test;

import java.text.MessageFormat;

/**
 * ***********************************************
 * Created by Sola on 2015/12/2.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestMessageFormat {

	@Test
	public void test() {
		MessageFormat format = new MessageFormat("You''ve not registered, please <a href=\"http://topaz.sinaapp.com/nm/reg.php?wechat={0}\">CLICK HERE</a> to register.");
		System.out.println(format.format(new Object[]{"wechatid"}));
	}

	@Test
	public void testJsonp() {
		String jsonp = "...{0}...";
		MessageFormat format = new MessageFormat(jsonp);
		System.out.println(format.format(new Object[]{"{SomeData}"}));
	}

}
