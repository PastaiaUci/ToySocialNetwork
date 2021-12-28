package com.example.toysocialnetworkgui.Utils.constants;

import java.time.format.DateTimeFormatter;

public class ValidatorConstants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final int USER_NUMBER_OF_ATTRIBUTES = 3;
    public static final int FRIENDSHIP_NUMBER_OF_ATTRIBUTRES = 3;
    public static final String VALID_FIRST_NAME = "/\b[A-Z][a-z]+\b/gm";
    public static final String VALID_LAST_NAME = "/\b[A-Z][a-z]+\b/gm";
    public static final Long TEMPORARY_USER_ID = 0L;
    public static final Long TEMPORARY_MESSAGE_ID = -1L;
}
