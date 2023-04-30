package kz.kaznu.smartbot.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DeliveryUtils {

    private static Map<String, Long> codes = new HashMap<>();

    public static String generateCode(Long orderId) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        codes.put(code, orderId);
        return code;
    }

    public static Long get(String code) {
        return codes.remove(code);
    }
}
