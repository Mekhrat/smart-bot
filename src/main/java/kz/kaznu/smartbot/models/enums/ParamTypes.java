package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParamTypes {

    GENERAL("Жалпы"),
    DISPLAY("Дисплей"),
    PROCESSOR_AND_MEMORY("Процессор және жад"),
    CAMERA_AND_MULTIMEDIA("Камера және мультимедиа"),
    ORGANIZER("Ұйымдастырушы және басқа мүмкіндіктер"),
    BATTERY("Батарея және сөйлесу уақыты"),
    WEIGHT_AND_DIMENSIONS("Салмағы және өлшемдері"),
    PECULIARITIES("Мүмкіндіктері"),

    CONNECTION("Байланыс"),
    MULTIMEDIA_FEATURES("Мультимедиа мүмкіндіктері"),
    CONSTRUCTION_AND_APPEARANCE("Құрылысы және сыртқы түрі"),

    MICROPHONE_SPECIFICATIONS("Микрофонның техникалық сипаттамалары"),
    WIRELESS_CONNECTION("Сымсыз байланыс");

    private final String name;
}
