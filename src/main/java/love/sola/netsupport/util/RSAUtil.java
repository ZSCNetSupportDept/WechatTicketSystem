package love.sola.netsupport.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

/**
 * @author Sola {@literal <dev@sola.love>}
 */
public class RSAUtil {


	public static Key publicKey;
	public static Key privateKey;
	public static String publicKey_s;
	public static String privateKey_s;

	static {
		genKeyPair();
	}

	public static void genKeyPair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			publicKey_s = Base64.encodeBase64String(publicKey.getEncoded());
			privateKey_s = Base64.encodeBase64String(privateKey.getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encrypt(String value) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64String(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
			return new String(original, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

}