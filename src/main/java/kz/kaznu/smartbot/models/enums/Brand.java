package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Brand {
    APPLE("Apple"),
    SAMSUNG("Samsung"),
    XIAOMI("Xiaomi"),
    OPPO("Oppo"),
    HUAWEI("Xuawei"),
    VIVO("Vivo"),
    POCO("Poco"),
    REALME("Realme");


    private final String name;
}
