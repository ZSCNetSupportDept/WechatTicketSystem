package love.sola.netsupport.wechat;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class TestEncrypt {

	@Test
	public void test() {
		String hash = BCrypt.hashpw("mypasswordhere", BCrypt.gensalt());
		assert BCrypt.checkpw("mypasswordhere", hash);
	}

}
