package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    IN_PROCESSING("Өңдеуде"),
    CANCELED("Қайтарылды"),
    DELIVERED("Жеткізілді"),
    ISSUED_TO_THE_COURIER("Курьерге берілді");

    private final String name;
}
