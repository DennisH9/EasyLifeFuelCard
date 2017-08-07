package com.newland.base.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 进行金额单位的换算
 * Created by linmq on 2016/5/12.
 */
public class AmountUtils {
	 /**
     * 元换成分
     * @param yuan 以元为单位的金额
     * @return long 
     */
    public static long yuan2fen(double yuan){
        BigDecimal bigDecimal = new BigDecimal(Double.toString(yuan));
        BigDecimal decimal100 = new BigDecimal(Integer.toString(100));
        return bigDecimal.multiply(decimal100).longValue();
    }

    public static long yuanStr2fen(String yuanStr){
        if(TextUtils.isEmpty(yuanStr)){
            return 0;
        }
        double yuan = 0;
        try {
            yuan = Double.valueOf(yuanStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return yuan2fen(yuan);
    }

    /**
     * 分换成元
     * @param fen 以分为单位的金额
     * @param scale 进度，表示需要精确到小数点后几位
     * @return double
     */
    public static double fen2yuan(long fen, int scale){
        BigDecimal bigDecimal = new BigDecimal(Long.toString(fen));
        BigDecimal decimal100 = new BigDecimal(Integer.toString(100));
        return bigDecimal.divide(decimal100, scale, RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 分换成元
     * @param fen 以分为单位的金额
     * @return String
     */
    public static String fen2yuan(long fen){
        double yuan = fen2yuan(fen,2);
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(yuan);
    }
//Test
    public static void main(String[] args){
        System.out.println(fen2yuan(10000));
    }
}
