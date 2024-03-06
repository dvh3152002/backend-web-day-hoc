package com.example.backendkhoaluan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String getTimeLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy hh_mm_ss z");
        return sdf.format(new Date());
    }
}
