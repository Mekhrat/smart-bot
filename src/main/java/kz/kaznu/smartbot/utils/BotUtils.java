package kz.kaznu.smartbot.utils;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotUtils {

    public static boolean validateEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePhone(String phone) {
       return phone.charAt(0) == '8' && phone.matches("\\d+") && phone.length() >= 11 && phone.length() < 13;
    }

    public static boolean validateIndex(String index) {
        if (index.equals(""))
            return true;
        return index.matches("\\d+") && index.length() == 6;
    }

    public static boolean checkLocalDateByMinutes(LocalDateTime toDate, int minutes) {
        return toDate.plusMinutes(minutes).isAfter(LocalDateTime.now());
    }

    public static boolean checkLocalDateByHours(LocalDateTime toDate, int hours) {
        return toDate.plusHours(hours).isAfter(LocalDateTime.now());
    }


}
