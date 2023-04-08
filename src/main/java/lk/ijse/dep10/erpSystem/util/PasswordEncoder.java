package lk.ijse.dep10.erpSystem.util;


import org.apache.commons.codec.digest.DigestUtils;

public class PasswordEncoder {
    public static String encode(String rawPassword) {
        return DigestUtils.sha256Hex(rawPassword);
    }

    public static boolean match(String rawPassword,String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
