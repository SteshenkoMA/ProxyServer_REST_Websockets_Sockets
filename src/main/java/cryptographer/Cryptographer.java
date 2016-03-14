package cryptographer;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.Base64;
import java.util.logging.Logger;

public class Cryptographer {
    private final Logger LOGGER = Logger.getLogger(Cryptographer.class.getName());
    private Base64.Decoder decoder = Base64.getDecoder();
    private Base64.Encoder encoder = Base64.getEncoder();
    private final String CODER_CONFIG = "AES/CBC/NoPadding";

    public String encryptWithTicket(String ticket, String value) {
        return encrypt(ticket.substring(0, 16), ticket.substring(16, 32), value);
    }

    public String decryptWithTicket(String ticket, String value) {
        return decrypt(ticket.substring(0, 16), ticket.substring(16, 32), value);
    }

    public String encrypt(String key1, String key2, String value) {
        try {
        	Cipher cipher;
            cipher = Cipher.getInstance(CODER_CONFIG);
            LOGGER.info("iv:");
            LOGGER.info(key2);
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            LOGGER.info("iv:");
            LOGGER.info(java.util.Arrays.toString(key2.getBytes("UTF-8")));
            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            LOGGER.info("key:");
            LOGGER.info(key1);
            LOGGER.info("key:");
            LOGGER.info(java.util.Arrays.toString(key1.getBytes("UTF-8")));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            LOGGER.info("messageNotEncrypted:");
            LOGGER.info(java.util.Arrays.toString(value.getBytes()));
            byte[] encrypted = cipher.doFinal(value.getBytes());
            LOGGER.info("encrypted:");
            LOGGER.info(java.util.Arrays.toString(encrypted));
            return encoder.encodeToString(encrypted);
        } catch (Exception ex ) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decrypt(String key1, String key2, String encrypted) {
        try {
        	Cipher cipher;
            cipher = Cipher.getInstance(CODER_CONFIG);
            IvParameterSpec iv = new IvParameterSpec(key2.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key1.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(decoder.decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}