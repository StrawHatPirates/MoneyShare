package com.tachys.moneyshare.util;

import android.content.Context;

import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;

/**
 * Created by tejom_000 on 30-07-2015.
 */
public class ProcessorUtil {

    Context mContext;

    static ProcessorUtil instance = null;


    public static ProcessorUtil getIntstance() {
        if (instance == null) {
            instance = new ProcessorUtil();
        }
        return instance;
    }

    public Expense getCalculatedExpense(Expense e, boolean isEqualSplit) {
        int total_expense = 0;

        if (isEqualSplit) {
            for (Member m : e.PaidBy.keySet()) {
                total_expense += e.PaidBy.get(m);
            }
            int total_members = e.PaidBy.size() + e.PaidTo.size();
            double perHeadShare = total_expense / total_members;

            for (Member m : e.PaidTo.keySet()) {
                e.PaidTo.put(m, perHeadShare);
            }
            double d;
            for (Member m : e.PaidBy.keySet()) {
                d = e.PaidBy.get(m);
                if (perHeadShare > d) {
                    e.PaidTo.put(m, perHeadShare - d);
                }
            }
        }
        return e;
    }


}
