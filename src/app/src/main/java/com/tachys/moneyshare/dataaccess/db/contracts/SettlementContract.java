package com.tachys.moneyshare.dataaccess.db.contracts;

import android.provider.BaseColumns;

public class SettlementContract {
    public SettlementContract() {
    }

    public static class SettlementEntry implements BaseColumns {
        public static final String TABLE_NAME = "settlement";
        public static final String COLUMN_NAME_PAYERID = "payer";
        public static final String COLUMN_NAME_PAYEEID = "payee";
        public static final String COLUMN_NAME_AMOUNT = "amount";
    }
}
