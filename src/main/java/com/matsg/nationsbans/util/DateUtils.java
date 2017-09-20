package com.matsg.nationsbans.util;

import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtils {

    public static Timestamp getBanTimestamp(String banLengthString) {
        Calendar c = Calendar.getInstance();

        int value;
        String[] part = banLengthString.split("(?<=\\d)(?=\\D)");

        try {
            value = Integer.parseInt(part[0]);
        } catch (NumberFormatException e) {
            return null;
        }

        String dateType = part[1];

        if (dateType.equalsIgnoreCase("h")) {
            c.add(Calendar.HOUR_OF_DAY, value);
        } else if (dateType.equalsIgnoreCase("d")) {
            c.add(Calendar.DAY_OF_MONTH, value);
        } else if (dateType.equalsIgnoreCase("m")) {
            c.add(Calendar.MONTH, value);
        } else {
            return null;
        }
        return new Timestamp(c.getTime().getTime());
    }
}