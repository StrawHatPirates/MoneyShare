package com.tachys.moneyshare.dataaccess.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseContract;
import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseMemberContract;
import com.tachys.moneyshare.dataaccess.db.contracts.MemberContract;

public class MoneyShareDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "MoneyShareDbHelper";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoneyShare.db";

    private static final String SQL_CREATE_EXPENSES =
            "CREATE TABLE IF NOT EXISTS " + ExpenseContract.ExpenseEntry.TABLE_NAME + " ("
                    + ExpenseContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY,"
                    + ExpenseContract.ExpenseEntry.COLUMN_NAME_Name + TEXT_TYPE + COMMA_SEP + " )";

    private static final String SQL_DELETE_EXPENSES =
            "DROP TABLE IF EXISTS " + ExpenseContract.ExpenseEntry.TABLE_NAME;

    private static final String SQL_CREATE_EXPENSEMEMBERS =
            "CREATE TABLE IF NOT EXISTS " + ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME + " ("
                    + ExpenseMemberContract.ExpenseMemberEntry._ID + " INTEGER PRIMARY KEY,"
                    + ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId + INTEGER_TYPE + COMMA_SEP
                    + ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_MemberId + INTEGER_TYPE + COMMA_SEP
                    + ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Type + TEXT_TYPE + COMMA_SEP
                    + ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Amount + REAL_TYPE + COMMA_SEP + " )";

    private static final String SQL_DELETE_EXPENSEMEMBERS =
            "DROP TABLE IF EXISTS " + ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME;

    private static final String SQL_CREATE_MEMBERS =
            "CREATE TABLE IF NOT EXISTS " + MemberContract.MemberEntry.TABLE_NAME + " ("
                    + MemberContract.MemberEntry._ID + " INTEGER PRIMARY KEY,"
                    + MemberContract.MemberEntry.COLUMN_NAME_Name + TEXT_TYPE + COMMA_SEP
                    + MemberContract.MemberEntry.COLUMN_NAME_Email + TEXT_TYPE + COMMA_SEP
                    + MemberContract.MemberEntry.COLUMN_NAME_Phone + TEXT_TYPE + COMMA_SEP + " )";

    private static final String SQL_DELETE_MEMBERS =
            "DROP TABLE IF EXISTS " + MemberContract.MemberEntry.TABLE_NAME;

    public MoneyShareDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG, "Creating Table if not exists");
        db.execSQL(SQL_CREATE_EXPENSES);
        db.execSQL(SQL_CREATE_MEMBERS);
        db.execSQL(SQL_CREATE_EXPENSEMEMBERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The local DB is going to be just a cache. The APP will fetch all the data from the server again.
        Log.i(LOG_TAG, "Deleting Table if exists");
        db.execSQL(SQL_DELETE_EXPENSES);
        db.execSQL(SQL_DELETE_MEMBERS);
        db.execSQL(SQL_DELETE_EXPENSEMEMBERS);
        onCreate(db);
    }
}
