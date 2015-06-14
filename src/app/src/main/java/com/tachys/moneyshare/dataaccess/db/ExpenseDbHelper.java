package com.tachys.moneyshare.dataaccess.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseContract;

public class ExpenseDbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = "ExpenseDbHelper";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_EXPENSES =
            "CREATE TABLE IF NOT EXISTS " + ExpenseContract.ExpenseEntry.TABLE_NAME + " ("
                    + ExpenseContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY,"
                    + ExpenseContract.ExpenseEntry.COLUMN_NAME_Name + TEXT_TYPE + COMMA_SEP
                    + ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate + TEXT_TYPE + " )";

    private static final String SQL_DELETE_EXPENSES =
            "DROP TABLE IF EXISTS " + ExpenseContract.ExpenseEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MoneyShare.db";

    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(LOG_TAG, "Creating Table if not exists");
        db.execSQL(SQL_CREATE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The local DB is going to be just a cache. The APP will fetch all the data from the server again.
        Log.i(LOG_TAG, "Deleting Table if exists");
        db.execSQL(SQL_DELETE_EXPENSES);
        onCreate(db);
    }
}
