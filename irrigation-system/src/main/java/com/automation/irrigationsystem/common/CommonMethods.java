package com.automation.irrigationsystem.common;

import com.automation.irrigationsystem.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.util.EnumUtils;

import java.math.BigDecimal;
import java.util.Calendar;

@Slf4j
public class CommonMethods {

    private CommonMethods() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String str) {
        return (null == str || str.isBlank());
    }

    public static void throwExceptionIfEmpty(String str, String responseMessage) {
        if(isBlank(str)) {
            throw new InvalidRequestException(ResponseCodes.EMPTY_INPUT, responseMessage);
        }
    }

    public static void throwExceptionIfNotNumber(String str, String responseMessage) {
        if(isBlank(str)) {
            return;
        }
        try{
            Integer.parseInt(str);
        }catch (NumberFormatException e) {
            throw new InvalidRequestException(ResponseCodes.INVALID_INPUT, responseMessage);
        }
    }

    public static void throwExceptionIfLengthInvalid(String str, int length, String responseMessage) {
        if(!isBlank(str) && str.length() > length) {
            throw new InvalidRequestException(ResponseCodes.INVALID_INPUT, responseMessage);
        }
    }

    public static <T extends Enum<T>> void validateInputWithEnum(Class<T> enumClass, String valueToMatch, String responseMessage) {
        if(isBlank(valueToMatch)) {
            return;
        }
        try {
            EnumUtils.findEnumInsensitiveCase(enumClass, valueToMatch);
        }catch(IllegalArgumentException e) {
            throw new InvalidRequestException(ResponseCodes.INVALID_INPUT, responseMessage);
        }
    }

    public static String getWeatherNameForDate(Calendar cal) {
        if(null == cal) {
            return "";
        }

        int month = cal.get(Calendar.MONTH) + 1;
        if(month == 12 || month == 1){
            return WeatherTypes.WINTER.name();
        }else if(month == 2 || month == 3) {
            return WeatherTypes.SPRING.name();
        }else if(month >= 4 && month <= 6) {
            return WeatherTypes.SUMMER.name();
        } else if (month ==7 || month == 8) {
            return WeatherTypes.MONSOON.name();
        }else if(month == 9) {
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if(day < 16) {
                return WeatherTypes.MONSOON.name();
            }else {
                return WeatherTypes.AUTUMN.name();
            }
        }else {
            return WeatherTypes.AUTUMN.name();
        }
    }

    public static boolean validateIrrigationStartTime(String startTime) {
        if(CommonMethods.isBlank(startTime)) {
            return true;
        }
        String [] splitStr = startTime.split(":");
        if(splitStr.length != 2) {
            return false;
        }
        String hourStr = splitStr[0];
        String minuteStr = splitStr[1];
        int hour;
        int minute;
        try {
            hour = Integer.parseInt(hourStr.trim());
        }catch(NumberFormatException e) {
            return false;
        }
        try {
            minute = Integer.parseInt(minuteStr.trim());
        }catch(NumberFormatException e) {
            return false;
        }
        return !(hour < 0 || hour > 23 || minute < 0 || minute > 59);
    }

    public static String getTruncatedStartTime(String startTime, String defaultStartTime) {
        //If there is any error in start time than return 08:00 as default time.
        if(!validateIrrigationStartTime(startTime)) {
            return defaultStartTime;
        }
        if(CommonMethods.isBlank(startTime)) {
            return defaultStartTime;
        }

        String [] splitStr = startTime.split(":");
        return splitStr[0].trim()+":"+splitStr[1].trim();
    }

    public static String getDefaultValueIfNull(String defaultVal, String newVal) {
        return CommonMethods.isBlank(newVal) ? defaultVal : newVal;
    }

    public static BigDecimal getDefaultValueBDIfNull(BigDecimal defaultVal, String newVal) {
        return CommonMethods.isBlank(newVal) ? defaultVal : new BigDecimal(newVal);
    }
}