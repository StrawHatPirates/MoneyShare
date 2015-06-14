package com.tachys.moneyshare.dataaccess.db.contracts;

import android.provider.BaseColumns;

public final class MemberContract {
    public MemberContract() {
    }

    public static class MemberEntry implements BaseColumns {
        public static final String TABLE_NAME = "member";
        public static final String COLUMN_NAME_Name = "name";
        public static final String COLUMN_NAME_Email = "email";
        public static final String COLUMN_NAME_Phone = "phone";
    }
}
