package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Ram {
    M2("2Гб"),
    M4("4Гб"),
    M6("6Гб"),
    M8("8Гб"),
    M12("12Гб"),
    M16("16Гб"),
    M24("24Гб"),
    M32("32Гб");

    private final String name;
}
