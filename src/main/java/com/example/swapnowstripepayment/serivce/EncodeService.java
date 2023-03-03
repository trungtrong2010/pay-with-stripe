package com.example.swapnowstripepayment.serivce;
import org.springframework.stereotype.Service;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncodeService {

    // Docs: https://stackjava.com/demo/code-java-vi-du-ma-hoa-giai-ma-voi-aes.html

    String SECRET_KEY = "16kytu_thoi_nghe"; // 16 ky tu
    SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    // AES = Advanced Encryption Standard.
    // ECB = Electronic Codebook mode.
    // PKCS5Padding = PKCS #5-style padding.

    public String encodeData(String key) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            if (cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] byteEncrypted = new byte[0];
        try {
            if (cipher != null) {
                byteEncrypted = cipher.doFinal(key.getBytes());
            }
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(byteEncrypted);
    }

    public String decodeData(String key) {
        byte[] bytes = Base64.getDecoder().decode(key);

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            if (cipher != null) {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            }
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        byte[] byteDecrypted = new byte[0];
        try {
            byteDecrypted = cipher.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(byteDecrypted);
    }
}
