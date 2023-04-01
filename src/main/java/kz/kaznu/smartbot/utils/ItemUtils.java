package kz.kaznu.smartbot.utils;

import kz.kaznu.smartbot.models.dto.ItemParamsDto;
import kz.kaznu.smartbot.models.entities.Item;
import kz.kaznu.smartbot.models.entities.ItemParam;
import kz.kaznu.smartbot.models.entities.Order;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ItemUtils {
    public static String createMainItemParamsInfo(Item item, List<ItemParamsDto> mainParams) {
        StringBuilder builder = new StringBuilder(item.getFullName() + "\nБағасы: " + item.getNewPrice() + " тг\n\n");
        mainParams.forEach(i -> builder.append(i.getParamName()).append(": ").append(i.getParamValue()).append("\n"));
        return builder.toString();
    }

    public static String createInfoTextWithAllParams(Item item ,Map<String, List<ItemParamsDto>> params) {
        StringBuilder builder = new StringBuilder(item.getFullName() + "\nБағасы: " + item.getNewPrice() + " тг\n\n");
        for (Map.Entry<String,List<ItemParamsDto>> param: params.entrySet()) {
            builder.append(param.getKey()).append(":\n");
            param.getValue().forEach(i -> builder.append(i.getParamName()).append(": ").append(i.getParamValue()).append("\n"));
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String createOrdersText(Order order) {
        StringBuilder builder = new StringBuilder();
        builder.append("<i>Тапсырыс күйі: ").append(order.getStatus().getName()).append("</i>\n\n")
                .append("Тапсырыс нөмірі: ").append(order.getId()).append("\n")
                .append("Жалпы сома: " ).append(order.getTotal()).append(" тг\n")
                .append("Тапсырыс уақыты: ").append(order.getOrderDate().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"))).append("\n")
                .append("Телефон нөмірі: ").append(order.getConsumerPhone()).append("\n")
                .append("Мекен-жайы: ").append(order.getDeliveryAddress()).append("\n");

        if (!order.getConsumerIndex().equals(""))
            builder.append("Индекс: ").append(order.getConsumerIndex());
        return builder.toString();
    }
}
