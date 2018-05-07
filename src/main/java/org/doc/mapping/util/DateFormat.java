package org.doc.mapping.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Chen Hui
 */
public enum DateFormat {

    /** HH:mm:ss */
    HH_mm_ss("HH:mm:ss"),
    /** yyyyMMdd */
    yyyyMMdd("yyyyMMdd"),

    /** yyyy-MM-dd */
    yyyy_MM_dd("yyyy-MM-dd"),

    /** yyyy-MM-dd-HH */
    yyyy_MM_dd_HH("yyyy-MM-dd-HH"),

    /** yyyy.MM.dd */
    yyyydotMMdotdd("yyyy.MM.dd"),

    /** MM.dd */
    MMdotdd("MM.dd"),

    /** yyyy年M月d日 */
    yyyy_NIAN_MM_YUE_dd_RI("yyyy年M月d日"),

    /** yyyy/M/d */
    yyyy_slash_MM_slash_dd("yyyy/M/d"),

    /** dd/MM/yyyy */
    dd_slash_MM_slash_yyyy("dd/MM/yyyy"),

    /** yyyy-MM-dd HH:mm:ss */
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),

    /** yyyy-MM-dd HH:mm */
    yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm"),

    /** yyyyMMddHHmmss */
    yyyyMMddHHmmss("yyyyMMddHHmmss"),

    /** yyyyMMddHHmm */
    yyyyMMddHHmm("yyyyMMddHHmm"),

    /** yyMMdd HHmmss */
    yyMMdd_HHmmss("yyMMdd HHmmss"),

    /** EEE, d MMM yyyy HH:mm:ss zzz */
    EEE_d_MM_yyyy_HH_mm_ss_zzz("EEE, d MMM yyyy HH:mm:ss zzz"),

    /** MM/dd/yyyy H:m:s */
    MM_slash_dd_slash_yy_H_m_s("MM/dd/yyyy H:m:s"),

    /** yyyy/MM/dd H:m:s */
    yyyy_slash_MM_slash_dd_H_m_s("yyyy/MM/dd H:m:s"),

    /** yyyy年M月d日 HH:mm （星期X）*/
    yyyy_NIAN_MM_YUE_dd_RI_HH_SHI_mm_FEN_E("yyyy年M月d日 HH:mm （E）");

    private String format;
    
    private ThreadLocal<SimpleDateFormat> formatter;
    
    private static Map<String, DateFormat> formatMap;
    
    static  {
        formatMap = new HashMap<String, DateFormat>();
        for (DateFormat f: DateFormat.values()) {
            formatMap.put(f.format, f);
        }
    }
    
    DateFormat(final String format) {
        this.format = format;
        this.formatter = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(format, Locale.CHINESE);
            }
        };
    }
    
    public String getFormat() {
        return format;
    }
    
    public SimpleDateFormat getFormatter() {
        return formatter.get();
    }
    
    public String format(Date time) {
        return formatter.get().format(time);
    }
    
    public String format(long time) {
        return format(new Date(time));
    }
    
    public Date parse(String str) throws ParseException {
        return formatter.get().parse(str);
    }
    
    public boolean isValidDate(String date) {
        if (date == null || date.trim().length() == 0) {
            return false;
        }
        try {
            Date tmpDate = getFormatter().parse(date);
            String date1 = getFormatter().format(tmpDate);
            if (date1 != null && date1.equals(date)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 根据格式获取对应的formatter
     * @param date
     * @return
     */
    public static SimpleDateFormat getFormatter(String date) {
        DateFormat f = formatMap.get(date);
        if (f != null) {
            return f.getFormatter();
        }else{
            if(date != null && date.trim().length() != 0) {
                SimpleDateFormat dateFormat = new SimpleDateFormat();
                dateFormat.applyPattern(date.trim());
                return dateFormat;
            }else{
                return null;
            }
        }
    }

}