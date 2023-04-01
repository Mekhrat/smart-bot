package kz.kaznu.smartbot.models.enums;

import lombok.Getter;

@Getter
public enum Status {

    START, EMPTY, SEND_EMAIL, SEND_ACTIVATE_CODE, SEND_FULL_NAME, SEND_PHONE, SEND_ADDRESS, SEND_INDEX;
}
