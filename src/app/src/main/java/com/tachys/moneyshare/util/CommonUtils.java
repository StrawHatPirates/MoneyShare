package com.tachys.moneyshare.util;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtils {

    public static String MONEYSHARE_PREF = "money_share_pref";

    public static String getPref(Context ctx, String key) {
        return ctx.getSharedPreferences(MONEYSHARE_PREF, Context.MODE_PRIVATE).getString(key, null);
    }

    public static void setPref(Context ctx, String key, String val) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(MONEYSHARE_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(key, val);
        editor.commit();
    }


}
