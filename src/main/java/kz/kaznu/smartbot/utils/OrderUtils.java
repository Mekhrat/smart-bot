package kz.kaznu.smartbot.utils;

import kz.kaznu.smartbot.models.dto.ConsumerInfo;
import kz.kaznu.smartbot.models.entities.TelegramUser;

import java.util.HashMap;
import java.util.Map;

public class OrderUtils {

    private static Map<String, ConsumerInfo> orders = new HashMap<>();

    public static void addName(TelegramUser user, String name) {
        orders.put(user.getEmail(), ConsumerInfo.builder().name(name).build());
    }

    public static void addPhone(TelegramUser user, String phone) {
        orders.get(user.getEmail()).setPhone(phone);
    }

    public static void addAddress(TelegramUser user, String address) {
        orders.get(user.getEmail()).setAddress(address);
    }

    public static void addIndex(TelegramUser user, String index) {
        orders.get(user.getEmail()).setIndex(index);
    }

    public static ConsumerInfo getOrderInfo(String email) {
        return orders.get(email);
    }
}
