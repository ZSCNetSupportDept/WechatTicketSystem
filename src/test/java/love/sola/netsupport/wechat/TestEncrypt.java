package love.sola.netsupport.wechat;

import love.sola.netsupport.util.RSAUtil;
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
	public void testBCrypt() {
		String hash = BCrypt.hashpw("mypasswordhere", BCrypt.gensalt());
		assert BCrypt.checkpw("mypasswordhere", hash);
	}

	@Test
	public void testRSA() {
		assert "Hello World".equals(RSAUtil.decrypt(RSAUtil.encrypt("Hello World")));
		assert "Encrypt".equals(RSAUtil.decrypt(RSAUtil.encrypt("Encrypt")));
	}

}
