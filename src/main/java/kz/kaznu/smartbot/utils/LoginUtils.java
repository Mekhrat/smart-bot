package kz.kaznu.smartbot.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LoginUtils {
    private static Map<String, String> codes = new HashMap<>();

    public static String generateActivateCodeForEmail(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        codes.put(email, code);
        return code;
    }

    public static boolean checkActivateCode(String email, String code) {
        if (codes.get(email).equals(code)) {
            codes.remove(email);
            return true;
        }
        return false;
    }
}
