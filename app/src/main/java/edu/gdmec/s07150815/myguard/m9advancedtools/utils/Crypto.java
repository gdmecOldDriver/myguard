package edu.gdmec.s07150815.myguard.m9advancedtools.utils;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ys on 2016/12/22.
 */
public class Crypto {
    //加密一个文本 返回base64编码后的内容
    //seed 种子 密码
    //return 密文
    //throws Exception
    public static String encrypt(String seed,String plain)throws Exception{
        byte[] rawKey=getRawKey(seed.getBytes());
        byte[] encrypted=encrypt(rawKey,plain.getBytes());
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }
    //解密base64编码后的密文
    //seed 种子 密文
    //return 原文
    //throws Exception
    public static String decrypt(String seed,String encrypted) throws Exception{
        byte[] rawKey=getRawKey(seed.getBytes());
        byte[] enc=Base64.decode(encrypted.getBytes(),Base64.DEFAULT);
        byte[] result=decrypt(rawKey,enc);
        return new String(result);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(raw,"AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte[] decrypted =cipher.doFinal(encrypted);
        return decrypted;

    }

    private static byte[] encrypt(byte[] raw, byte[] plain)throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(raw,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte[] encrypted =cipher.doFinal(plain);
        return encrypted;
    }

    private static byte[] getRawKey(byte[] seed)throws Exception {
        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        SecureRandom random=SecureRandom.getInstance("SHAIPRNG");
        random.setSeed(seed);
        SecretKey key=keygen.generateKey();
        byte[] raw=key.getEncoded();
        return raw;
    }
}
