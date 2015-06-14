package com.tachys.moneyshare.dataaccess.db.contracts;

import android.provider.BaseColumns;

public class ExpenseMemberContract {
    public ExpenseMemberContract() {
    }

    public static class ExpenseMemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "expensemember";

        public static final String COLUMN_NAME_ExpenseId = "expenseid";

        public static final String COLUMN_NAME_PAYEE = "payee";
        public static final String COLUMN_NAME_PAYER = "payer";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }
}
