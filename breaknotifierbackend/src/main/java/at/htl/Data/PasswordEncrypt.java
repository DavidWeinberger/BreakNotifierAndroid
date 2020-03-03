package at.htl.Data;

import java.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PasswordEncrypt {

    private static final String ALGORITHM = "AES";

    public static String decrypt(String value, String id) throws Exception {
        Key key = generateKey(id);
        Cipher cipher = Cipher.getInstance(PasswordEncrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        System.out.println("Key ist working");
        byte[] decryptedValue64 = Base64.getDecoder().decode(value.replace("\n",""));
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue, "utf-8");
        return decryptedValue;

    }

    private static Key generateKey(String id) throws Exception {
        Key key = new SecretKeySpec(id.substring(0,16).getBytes(), PasswordEncrypt.ALGORITHM);
        return key;
    }
}

