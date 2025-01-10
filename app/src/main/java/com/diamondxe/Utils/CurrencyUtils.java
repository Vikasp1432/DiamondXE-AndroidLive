package com.diamondxe.Utils;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyUtils {

    public static String calculateFinalAmount(String subTotalFormat, String exchangeRate, String currencySymbol) {
        try {
            double subTotal = Double.parseDouble(subTotalFormat);
            double rate = Double.parseDouble(exchangeRate);

            double resultDouble = subTotal * rate;
            BigDecimal bd = new BigDecimal(resultDouble).setScale(2, RoundingMode.HALF_UP);
            double resultWithTwoDecimals = bd.doubleValue();

            DecimalFormat formatter = new DecimalFormat("#,##,###", DecimalFormatSymbols.getInstance());
            String formattedAmount = formatter.format(resultWithTwoDecimals);

            Log.e("calculateFinalAmount", "Final Amount: " + formattedAmount);

            return currencySymbol+formattedAmount;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid input";
        }
    }
    public static String calculateFinalAmountwithoutcurrency(String subTotalFormat, String exchangeRate) {
        try {
            double subTotal = Double.parseDouble(subTotalFormat);
            double rate = Double.parseDouble(exchangeRate);

            double resultDouble = subTotal * rate;
            BigDecimal bd = new BigDecimal(resultDouble).setScale(2, RoundingMode.HALF_UP);
            double resultWithTwoDecimals = bd.doubleValue();

            DecimalFormat formatter = new DecimalFormat("#,##,###", DecimalFormatSymbols.getInstance());
            String formattedAmount = formatter.format(resultWithTwoDecimals);

            Log.e("calculateFinalAmount", "Final Amount: " + formattedAmount);

            return formattedAmount;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "Invalid input";
        }
    }
}