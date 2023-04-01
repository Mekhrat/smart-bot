package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Memory {
    M16("16Гб"),
    M32("32Гб"),
    M64("64Гб"),
    M128("128Гб"),
    M256("256Гб"),
    M512("512Гб"),
    M1024("1024Гб");

    private final String name;
}
