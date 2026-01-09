package com.cware.netshopping.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.cware.netshopping.common.util.ConfigUtil;

import java.nio.charset.Charset;
import java.util.Base64;

public class AESCrypto {

    private final String SECRET_KEY = ConfigUtil.getString("PATMON_AES_SECRET_KEY"); //비밀키 노출에 주의! 개발/리얼환경 각각 별도로 전달됩니다.

    public String encryption(String str) throws Exception {
       // String plainUserId = "tmonapi";  //평문 tmonapi
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        String result = "";

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encrypted = cipher.doFinal(str.getBytes());
        //System.out.println(new String(Base64.getEncoder().encode(encrypted), Charset.forName("UTF-8")));

        result = new String(Base64.getEncoder().encode(encrypted), Charset.forName("UTF-8"));
        //result : nrteA09HloNkqxLBVozT3w==
        return result;
    }

    public String decryption(String str) throws Exception {
        //String encryptedUserId = "nrteA09HloNkqxLBVozT3w=="; //암호화된 데이터
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        String result = "";
        
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(str));
        //System.out.println(new String(decrypted));

        result = new String(decrypted);
        //result : tmonapi
        return result;
    }
}