package com.android.mobile_project.utils.constant;

public class TimeConstant {
    public static String[] getTimeDisplayValue() {
        String[] value = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                value[i] = "0" + i;
            } else {
                value[i] = String.valueOf(i);
            }
        }
        return value;
    }
}
