package love.sola.netsupport.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class Crypto {

	public static String hash(String pw) {
		return BCrypt.hashpw(pw, BCrypt.gensalt());
	}

	public static boolean check(String plain, String hash) {
		return BCrypt.checkpw(RSAUtil.decrypt(plain), hash);
	}

}
