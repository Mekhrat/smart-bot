package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Color {
    RED("қызыл"),
    WHITE("ақ"),
    BLUE("көк"),
    GREEN("жасыл"),
    PINK("қызғылт"),
    VIOLET("күлгін"),
    GOLD("алтын"),
    SILVER("күміс"),
    BLACK("қара");

    private final String name;


}
