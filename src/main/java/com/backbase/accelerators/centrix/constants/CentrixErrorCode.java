package com.backbase.accelerators.centrix.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CentrixErrorCode {

    SSO_NOT_SUPPORTED("0001", "System does not support single sign-on"),
    INVALID_USER_ID_CODE("1001", "Invalid User ID Code"),
    INVALID_SYSTEM_ID_CODE("1002", "Invalid System ID Code"),
    MISSING_USER_ID_CODE("1003", "Missing User ID Code"),
    MISSING_SYSTEM_ID_CODE("1004", "Missing System ID Code"),
    MISSING_PASSWORD("1005", "Missing Password"),
    ONE_TIME_PASSWORD_EXPIRED("1006", "One Time Password has expired"),
    LOCKED_USER("1007", "User is Locked"),
    INTERNAL_ERROR("9999", "Internal Error"); // Arbitrary 'catch-all' error code, just in case Centrix doesn't return on of the above codes

    private final String code;
    private final String message;

    public static CentrixErrorCode fromCode(String code) {
        return Arrays.stream(CentrixErrorCode.values())
                .filter(centrixErrorCode -> centrixErrorCode.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(CentrixErrorCode.INTERNAL_ERROR);
    }

}
