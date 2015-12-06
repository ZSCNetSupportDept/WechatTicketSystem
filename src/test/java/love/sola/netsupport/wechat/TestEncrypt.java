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
		System.out.println("hash = " + hash);
		System.out.println("BCrypt.checkpw(\"mypasswordhere\",hash) = " + BCrypt.checkpw("mypasswordhere", hash));
	}

	@Test
	public void testChk() {
		String hash = "$2a$10$RCXxfEygwHQeF4BKjx5Uwu/C72HqoCN.jKpNSNdwizcy7m301C9Am";
		System.out.println("BCrypt.checkpw(\"mypasswordhere\",hash) = " + BCrypt.checkpw("mypasswordhere", hash));
	}

}
