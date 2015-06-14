package com.tachys.moneyshare.dataaccess.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseContract;
import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseMemberContract;
import com.tachys.moneyshare.dataaccess.db.contracts.MemberContract;
import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DBAccess implements IDataAccess {
    private final String LOG_TAG = "DBAccess";
    private Context context;
    private MemberDbHelper memberHelper;
    private ExpenseDbHelper expenseHelper;
    private ExpenseMemberDbHelper expenseMemberHelper;

    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat isoSdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

    public DBAccess(Context context) {
        this.context = context;
        memberHelper = new MemberDbHelper(context);
        expenseHelper = new ExpenseDbHelper(context);
        expenseMemberHelper = new ExpenseMemberDbHelper(context);
    }

    @Override
    public Member addMember(Member member) {

        //Writable DB Helper

        try (SQLiteDatabase db = memberHelper.getWritableDatabase()) {

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(MemberContract.MemberEntry.COLUMN_NAME_Name, member.Name);
            values.put(MemberContract.MemberEntry.COLUMN_NAME_Email, member.Email);
            values.put(MemberContract.MemberEntry.COLUMN_NAME_Phone, member.Phone);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(MemberContract.MemberEntry.TABLE_NAME, null, values);

            Log.i(LOG_TAG, "Added Member:" + member.Email + " Id:" + newRowId);
            member.Id = newRowId;

            return member;
        }
    }

    @Override
    public ArrayList<Member> addMember(ArrayList<Member> members) {
        //Writable DB Helper
        SQLiteDatabase db = memberHelper.getWritableDatabase();

        try {

            db.beginTransaction();

            for (Member member : members) {// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(MemberContract.MemberEntry.COLUMN_NAME_Name, member.Name);
                values.put(MemberContract.MemberEntry.COLUMN_NAME_Email, member.Email);
                values.put(MemberContract.MemberEntry.COLUMN_NAME_Phone, member.Phone);

                // Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(MemberContract.MemberEntry.TABLE_NAME, null, values);

                Log.i(LOG_TAG, "Added Member:" + member.Email + " Id:" + newRowId);
                member.Id = newRowId;
            }

            db.setTransactionSuccessful();
            return members;
        } catch (Exception ex) {
            db.endTransaction();
            Log.e(LOG_TAG, ex.toString());
            throw ex;
        } finally {
            db.close();
        }
    }

    @Override
    public ArrayList<Member> getMember() {

        // Readable DB Helper

        try (SQLiteDatabase db = memberHelper.getReadableDatabase()) {
            String[] projection = {
                    MemberContract.MemberEntry._ID,
                    MemberContract.MemberEntry.COLUMN_NAME_Name,
                    MemberContract.MemberEntry.COLUMN_NAME_Email,
                    MemberContract.MemberEntry.COLUMN_NAME_Phone,
            };

            String sortOrder = MemberContract.MemberEntry.COLUMN_NAME_Name + " DESC";

            Cursor c = db.query(MemberContract.MemberEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);

            if (c.moveToFirst()) {
                ArrayList<Member> members = new ArrayList<>();

                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(MemberContract.MemberEntry._ID));
                    String Name = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Name));
                    String Email = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Email));
                    String Phone = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Phone));

                    Member newMember = new Member(Id, Name, Email, Phone);

                    members.add(newMember);
                } while (c.moveToNext());

                return members;
            }

            return null;

        }

    }

    @Override
    public Member getMember(long memberId) {

        try (SQLiteDatabase db = memberHelper.getReadableDatabase()) {
            String[] projection = {
                    MemberContract.MemberEntry._ID,
                    MemberContract.MemberEntry.COLUMN_NAME_Name,
                    MemberContract.MemberEntry.COLUMN_NAME_Email,
                    MemberContract.MemberEntry.COLUMN_NAME_Phone,
            };

            String sortOrder = MemberContract.MemberEntry.COLUMN_NAME_Name + " DESC";

            String selection = MemberContract.MemberEntry._ID + " =?";
            String[] selectionArgs = {String.valueOf(memberId)};

            Cursor c = db.query(MemberContract.MemberEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);

            if (c.moveToFirst()) {
                Member member;


                long Id = c.getLong(c.getColumnIndexOrThrow(MemberContract.MemberEntry._ID));
                String Name = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Name));
                String Email = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Email));
                String Phone = c.getString(c.getColumnIndexOrThrow(MemberContract.MemberEntry.COLUMN_NAME_Phone));

                member = new Member(Id, Name, Email, Phone);


                return member;
            }

            return null;
        }
    }

    @Override
    public Expense addExpense(Expense expense) {
        SQLiteDatabase expenseDb = expenseHelper.getWritableDatabase();
        SQLiteDatabase expenseMemberDb = expenseMemberHelper.getWritableDatabase();

        try {

            expenseDb.beginTransaction();
            expenseMemberDb.beginTransaction();

            Date date = new Date();
            String formattedDate = isoSdf.format(date);
            ContentValues expenseValues = new ContentValues();
            expenseValues.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_Name, expense.Name);
            expenseValues.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate, formattedDate);

            long expenseId = expenseDb.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, expenseValues);

            Log.i(LOG_TAG, "Inserted Expense:" + expense.Name + " Id:" + expenseId);

            expense.Id = expenseId;

            Log.i(LOG_TAG, " Adding PaidBy");
            for (Member member : expense.PaidBy.keySet()) {
                ContentValues expenseMemberValues = new ContentValues();
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId, expenseId);
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_MemberId, member.Id);
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Amount, expense.PaidBy.get(member));
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Type, ExpenseMemberContract.ExpenseMemberEntry.PAID);

                expenseMemberDb.insert(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME, null, expenseMemberValues);
            }

            Log.i(LOG_TAG, " Adding PaidTo");
            for (Member member : expense.PaidTo.keySet()) {
                ContentValues expenseMemberValues = new ContentValues();
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId, expenseId);
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_MemberId, member.Id);
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Amount, expense.PaidTo.get(member));
                expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Type, ExpenseMemberContract.ExpenseMemberEntry.OWE);

                expenseMemberDb.insert(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME, null, expenseMemberValues);
            }

            return expense;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
            expenseDb.endTransaction();
            expenseMemberDb.endTransaction();
        } finally {
            expenseDb.close();
            expenseMemberDb.close();
        }

        return null;
    }

    @Override
    public ArrayList<Expense> getExpenses() {
        // Readable DB Helper

        try (SQLiteDatabase expenseDb = expenseHelper.getReadableDatabase(); SQLiteDatabase expenseMemberDb = expenseMemberHelper.getReadableDatabase()) {
            String[] projection = {
                    ExpenseContract.ExpenseEntry._ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_Name,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate,
            };

            String sortOrder = ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate + " DESC";

            Cursor c = expenseDb.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);

            if (c.moveToFirst()) {
                ArrayList<Expense> expenses = new ArrayList<>();

                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry._ID));
                    String Name = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_Name));
                    String formattedDate = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate));

                    Expense expense = new Expense();
                    expense.Id = Id;
                    expense.Name = Name;
                    expense.LastUpdated = isoSdf.parse(formattedDate);
                    expense.PaidBy = new HashMap<>();
                    expense.PaidTo = new HashMap<>();

                    String[] exprojection = {
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_MemberId,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Amount,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Type,
                    };

                    String selection = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId + " =?";
                    String[] selectionArgs = {String.valueOf(Id)};


                    Cursor cex = expenseMemberDb.query(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME,
                            exprojection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            null);

                    if (cex.moveToFirst()) {

                        do {
                            long expenseId = cex.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId));
                            long memberId = cex.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_MemberId));
                            Double amount = cex.getDouble(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Amount));
                            String type = cex.getString(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_Type));

                            if (expenseId != Id) {
                                Log.e(LOG_TAG, "Got a differnet expense id than the one expected");
                                return null;
                            }

                            Member member = getMember(memberId);
                            if (type.equals(ExpenseMemberContract.ExpenseMemberEntry.PAID)) {
                                expense.PaidBy.put(member, amount);
                            } else if (type.equals(ExpenseMemberContract.ExpenseMemberEntry.OWE)) {
                                expense.PaidTo.put(member, amount);
                            }

                        } while (cex.moveToNext());
                    }


                    expenses.add(expense);
                } while (c.moveToNext());

                return expenses;
            }

            return null;

        } catch (ParseException pex) {
            Log.e(LOG_TAG, "Exception while parsing : " + pex.toString());
            //throw new Exception("Not Able to parse the date format");
        }

        return null;
    }
}
