package com.tachys.moneyshare.dataaccess.db.contracts;

import android.provider.BaseColumns;

public class ExpenseContract {

    public ExpenseContract() {
    }

    public static class ExpenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "expense";
        public static final String COLUMN_NAME_Name = "name";
        public static final String COLUMN_NAME_LastUpdate = "lastupdate";
    }
}
