package love.sola.netsupport.util;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * ***********************************************
 * Created by Sola on 2015/12/6.
 * Don't modify this source without my agreement
 * ***********************************************
 */
public class EncryptTest {

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

//	@Test
	public void testRSASpecKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		System.out.println("RSAUtil.privateKey_s = " + RSAUtil.privateKey_s);
		System.out.println("RSAUtil.publicKey_s = " + RSAUtil.publicKey_s);
//		String pkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA0qyARvHSCIUQ6YM6K+e/QgiZ+dc/MpVz5DIFwQab5iiifruQiaoA74ilHOOiq5i0ToR1VxNhCUZcAy2saHNifoYKTauMOUSV6IoP4X5jp691PlI9yxNx328mSlPNM9+7BgOzrUP1pR71d+T4LDn0o4J6Ad82vVIe7yWszzF4qQIDAQAB";
		String pkey = RSAUtil.publicKey_s;
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(pkey));
		RSAUtil.publicKey = keyFactory.generatePublic(keySpec);
		System.out.println("RSAUtil.encrypt(\"233\") = " + RSAUtil.encrypt("233"));
	}

}
