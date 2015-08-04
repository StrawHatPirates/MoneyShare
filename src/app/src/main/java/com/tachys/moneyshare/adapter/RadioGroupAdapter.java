package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * Created by tejom_000 on 27-07-2015.
 */
public class RadioGroupAdapter extends RadioGroup {
    public RadioGroupAdapter(Context context) {
        super(context);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }
}
