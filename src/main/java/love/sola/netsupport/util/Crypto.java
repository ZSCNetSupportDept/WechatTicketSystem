package love.sola.netsupport.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class Crypto {

	public static String hash(String pw) {
		return BCrypt.hashpw(pw, BCrypt.gensalt());
	}

	public static boolean check(String plain, String hash) {
		return BCrypt.checkpw(plain, hash);
	}

}
