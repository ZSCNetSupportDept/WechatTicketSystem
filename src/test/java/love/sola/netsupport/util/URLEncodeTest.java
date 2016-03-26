package love.sola.netsupport.util;

import com.google.common.net.UrlEscapers;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * ***********************************************
 * Created by Sola on 2016/3/26.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class URLEncodeTest {

	@Test
	public void testEncode() throws UnsupportedEncodingException {
		assertThat(
				UrlEscapers.urlFragmentEscaper().escape("Test Title"),
				equalTo("Test%20Title")
		);
		assertThat(
				Redirect.success()
						.title("Test Title")
						.msg("Test Message")
						.toString(),
				equalTo("http://topaz.sinaapp.com/nm/v2/result.html?type=1&title=!!Test%20Title!!&msg=!!Test%20Message!!&")
		);
	}

}
