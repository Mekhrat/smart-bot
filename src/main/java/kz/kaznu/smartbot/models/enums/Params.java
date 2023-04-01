package kz.kaznu.smartbot.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Params {
    COLOR("Түсі"),
    MEMORY("Жады"),
    RAM("Жедел жады"),
    BATTERY("Батарея сыйымдылығы"),
    PROCESSOR("Процессоры"),
    OPERATION_SYSTEM("Операциялық жүйесі"),
    SIM_CARD("SIM карта"),
    MATERIAL("Материалы"),
    NFC("NFC технологиясы"),
    INTERNET5G("5G қолдауы"),
    DIAGONAL("Диагональ"),
    DISPLAY_RESOLUTION("Дисплей ажыратымдылығы"),
    CPU_FREQUENCY("Процессор жиілігі"),
    CAMERA("Камера"),
    FRONT_CAMERA("Алдыңғы камера"),
    VIDEO("Видео"),
    AUDIO_FORMAT("Аудио форматтары"),
    SENSORS("Сенсорлар"),
    AIRPLANE_MODE("Ұшақ режимі"),
    ORGANIZER("Ұйымдастырушы"),
    CONTROL("Бақылау"),
    SUPPORT_WIRELESS_CHARGING("Сымсыз зарядтауды қолдау"),
    BATTERY_TYPE("Батарея түрі"),
    DIMENSIONS("Өлшемдері"),
    WEIGHT("Салмағы"),

    PLATFORM_SUPPORT("Платформаны қолдау"),
    VIEW_AND_REPLY_NOTIFICATION("Хабарландыруларды көру немесе жауап беру"),
    VIBRATION("Діріл"),
    STRAP_MATERIAL("Бау материалы"),
    TIME_DISPLAY_METHOD("Уақытты көрсету әдісі"),
    STRAP_COLOR("Бау түсі"),
    GLASS_TYPE("Шыны түрі"),
    PROTECTION("Қорғаныс"),
    CASE_SIZE("Корпус өлшемі"),
    SCREEN_PRESENCE("Экранның болуы"),
    SCREEN_TECHNOLOGY("Экран технологиясы"),
    MONITORING("Бақылау"),
    PHONE_CALLS("Телефон қоңыраулары"),
    MOBILE_INTERNET("Мобильді интернет"),
    GPS("GPS"),
    INTERFACES("Интерфейстер"),
    AUDIO_INPUT_OUTPUT("Аудио кіріс/шығыс"),
    AUDIO_VIDEO("Аудио бейне"),
    HEADPHONE_JACK("Құлаққап ұясы"),
    PRESENCE_OF_A_CAMERA("Камераның болуы"),
    CHARGING_FEATURES("Зарядтау мүмкіндіктері"),
    WORKING_HOURS("Жұмыс уақыты"),
    BUILT_IN_MEMORY("Кірістірілген жады"),

    VIEW("Түрі"),
    CONNECTION("Байланыс"),
    TYPE_OF_ACOUSTIC_DESIGN("Акустикалық дизайн түрі"),
    MOUNT_TYPE("Бекіту түрі"),
    DESIGN_FEATURES("Дизайн ерекшеліктері"),
    ACTIVE_NOISE_CANCELLATION("Белсенді шуды жою"),
    CHARGING_CONNECTOR("Зарядтау қосқышы"),

    BLUETOOTH_VERSION("Bluetooth нұсқасы"),
    HEADSET_FEATURES("Құлаққаптың мүмкіндіктері"),
    MICROPHONE("Микрофон"),
    MICROPHONE_MOUNT("Микрофон қондырмасы"),
    PECULIARITIES("Ерекшеліктер");



    private final String name;
}
