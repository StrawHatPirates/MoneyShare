package com.tachys.moneyshare.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtils {

    public static String MONEYSHARE_PREF = "money_share_pref";

    public static String EXPENSE_ID = "expense_id";
    public static String EXPENSE_TITLE = "expense_title";
    public static String EXPENSE_AMOUNT = "expense_amount";
    public static String EXPENSE_PAYERS = "expense_payers";
    public static String EXPENSE_PAYEES = "expense_payees";

    public static long my_member_id = 0;
    public static String my_member_name = null;
    public static String my_member_mail = null;
    public static String my_member_phone = null;

    public static int REQUEST_CREATE_EXPENSE = 1;

    public static int RESULT_CREATED_EXPENSE = 2;


    public static String getPref(Context ctx, String key) {
        return ctx.getSharedPreferences(MONEYSHARE_PREF, Context.MODE_PRIVATE).getString(key, null);
    }

    public static void setPref(Context ctx, String key, String val) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(MONEYSHARE_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(key, val);
        editor.commit();
    }


}
