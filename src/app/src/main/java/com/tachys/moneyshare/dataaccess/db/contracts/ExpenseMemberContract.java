package com.tachys.moneyshare.dataaccess.db.contracts;

import android.provider.BaseColumns;

public class ExpenseMemberContract {
    public ExpenseMemberContract() {
    }

    public static class ExpenseMemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "expense";
        public static final String COLUMN_NAME_ExpenseId = "expenseid";
        public static final String COLUMN_NAME_Type = "type";
        public static final String COLUMN_NAME_MemberId = "memberid";
        public static final String COLUMN_NAME_Amount = "amount";
        public static final String PAID = "Paid";
        public static final String OWE = "Owe";
    }
}
