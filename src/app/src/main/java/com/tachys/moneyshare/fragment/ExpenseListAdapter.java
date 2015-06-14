package com.tachys.moneyshare.fragment;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.model.Expense;

import java.util.List;

/**
 * Created by tejom_000 on 13-06-2015.
 */
public class ExpenseListAdapter extends ArrayAdapter<Expense> {
    public ExpenseListAdapter(Context context, List<Expense> items) {
        super(context, R.layout.expense_list_item, items);
    }
}
