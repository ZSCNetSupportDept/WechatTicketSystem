package love.sola.netsupport.util;

import com.google.common.net.UrlEscapers;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Sola {@literal <dev@sola.love>}
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
                equalTo("http://s.wts.sola.love/nm/v2/result.html?type=1&title=Test%20Title&msg=Test%20Message&")
        );
    }

}
