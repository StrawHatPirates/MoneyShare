package com.tachys.moneyshare.dataaccess.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseContract;
import com.tachys.moneyshare.dataaccess.db.contracts.ExpenseMemberContract;
import com.tachys.moneyshare.dataaccess.db.contracts.MemberContract;
import com.tachys.moneyshare.dataaccess.db.contracts.SettlementContract;
import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;
import com.tachys.moneyshare.model.MicroExpense;
import com.tachys.moneyshare.model.Settlement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DBAccess implements IDataAccess {
    private static final String TAG = "DBAccess";
    private static DBAccess instance = null;
    private final String LOG_TAG = "DBAccess";
    private MoneyShareDbHelper dbHelper;

    private Context mContext;

    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat isoSdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

    private static final String[] PROJECTION =
            {
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.LOOKUP_KEY,
                    Build.VERSION.SDK_INT
                            >= Build.VERSION_CODES.HONEYCOMB ?
                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                            ContactsContract.Contacts.DISPLAY_NAME

            };

    public static IDataAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public DBAccess(Context context) {
        mContext = context;
        dbHelper = new MoneyShareDbHelper(context);
    }

    @Override
    public Member addMember(Member member) {

        //Writable DB Helper
        Member result = findMember(member.Email, member.Phone, member.Email != null ? true : false);
        if (result != null) {
            return result;
        }

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {


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
        SQLiteDatabase db = null;
        Member result = null;
        try {

            db.beginTransaction();

            for (Member member : members) {
                // Create a new map of values, where column names are the keys
                result = findMember(member.Email, member.Phone, member.Email != null ? true : false);
                if (result != null) {
                    member.Id = result.Id;
                    continue;
                } else {
                    db = dbHelper.getWritableDatabase();
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
            }


            db.setTransactionSuccessful();
            return members;
        } catch (
                Exception ex
                )

        {
            db.endTransaction();
            Log.e(LOG_TAG, ex.toString());
            throw ex;
        } finally

        {
            db.close();
        }

    }

    @Override
    public Member findMember(String email, String phone, boolean isEmail) {
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    MemberContract.MemberEntry._ID,
                    MemberContract.MemberEntry.COLUMN_NAME_Name,
                    MemberContract.MemberEntry.COLUMN_NAME_Email,
                    MemberContract.MemberEntry.COLUMN_NAME_Phone,
            };

            String sortOrder = MemberContract.MemberEntry.COLUMN_NAME_Name + " DESC";
            String selection = null;
            String[] selectionArgs = null;
            if (isEmail) {
                selection = MemberContract.MemberEntry.COLUMN_NAME_Email + " =?";
                selectionArgs = new String[]{String.valueOf(email)};
            } else {
                selection = MemberContract.MemberEntry.COLUMN_NAME_Phone + " =?";
                selectionArgs = new String[]{String.valueOf(phone)};
            }

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
    public ArrayList<Member> getMembers() {

        // Readable DB Helper

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
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
    public ArrayList<Member> getAllContacts() {

        ArrayList<Member> list = new ArrayList<>();
        Cursor pCur = null;
        Cursor cur = null;
        Cursor emails = null;
        String name;
        String email = null;
        int emailType;
        String phoneNo = null;
        Member mem;
        int cnt = 0;
        try {
            ContentResolver cr = mContext.getContentResolver();
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }

                    }
                    emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id, null, null);
                    if (emails != null) {
                        while (emails.moveToNext()) {
                            email = emails.getString(emails
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                            emailType = emails.getInt(emails
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        }
                    }
                    emails.close();
                    if (Integer.parseInt(cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        pCur.close();
                    }
                    mem = new Member(name, email, phoneNo);
                    list.add(mem);
                    cnt++;
                    if (cnt > 50)
                        break;
                }
            }
            Log.i(LOG_TAG, " returning " + list.size());
            return list;

        } finally {
            if (pCur != null) {
                pCur.close();
            }
            if (cur != null) {
                cur.close();
            }
            if (emails != null) {
                emails.close();
            }
        }
    }

    @Override
    public Member getMember(long memberId) {

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
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

                Log.i(TAG, "member " + member.Id + " name " + member.Name);
                return member;
            }

            return null;
        }
    }

    @Override
    public Expense addExpense(Expense expense) {

        for (Member memberPaid : expense.PaidBy.keySet()) {
            Member tmp;
            if (memberPaid != null) {
                tmp = findMember(null, memberPaid.Phone, false);
                if (tmp == null) {
                    Log.e(LOG_TAG, "Insertion paid by mem " + memberPaid.Name);
                    memberPaid.Id = addMember(memberPaid).Id;
                }
            }
        }

        for (Member member : expense.PaidTo.keySet()) {
            Member tmp;
            if (member != null) {
                tmp = findMember(null, member.Phone, false);
                if (tmp == null) {
                    Log.e(LOG_TAG, "Insertion paid to mem " + member.Name);
                    member.Id = addMember(member).Id;
                }
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {

            Log.v(LOG_TAG, "Starting Writable DB Transaction");
            db.beginTransaction();

            Date date = new Date();
            String formattedDate = isoSdf.format(date);

            // Forming Content values for the expense table
            ContentValues expenseValues = new ContentValues();
            expenseValues.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_Name, expense.Name);
            expenseValues.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate, formattedDate);

            // inserting
            long expenseId = db.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, expenseValues);

            if (expenseId == -1) {
                Log.e(LOG_TAG, "Insertion failed!!");
                //TODO: Add error handling.
                return null;
            }

            Log.i(LOG_TAG, "Inserted Expense:" + expense.Name + " Id:" + expenseId);

            expense.Id = expenseId;

            //Expense Member Table Calculations
            double totalAmount = expense.getTotalAmount();

            HashMap<Long, Double> memberToPercentageMap = new HashMap<>();

            // Calculated percentage of the total amount each person owes.
            for (Member member : expense.PaidTo.keySet()) {
                Double amountOwed = expense.PaidTo.get(member);
                Double percentageOwed = amountOwed / totalAmount;
                memberToPercentageMap.put(member.Id, percentageOwed);
            }

            for (Member memberPaid : expense.PaidBy.keySet()) {
                double amountPaid = expense.PaidBy.get(memberPaid);

                for (Long owedMemberId : memberToPercentageMap.keySet()) {
                    double amountOwed = amountPaid * memberToPercentageMap.get(owedMemberId);

                    ContentValues expenseMemberValues = new ContentValues();
                    expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId, expenseId);
                    expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER, memberPaid.Id);
                    expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE, owedMemberId);
                    expenseMemberValues.put(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT, amountOwed);

                    Log.v(LOG_TAG, String.format("Addign Expense member for ExpenseId : %d Payee: %d, Payer: %d, Amount : %f", expenseId, owedMemberId, memberPaid.Id, amountOwed));
                    long exmemId = db.insert(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME, null, expenseMemberValues);

                    if (exmemId == -1) {
                        Log.e(LOG_TAG, "Insertion failed!!");
                        //TODO: Add error handling.
                        return null;
                    }
                }
            }

            Log.v(LOG_TAG, "Marking DB Transaction Successfull!");
            db.setTransactionSuccessful();
            return expense;

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
        } finally {
            Log.v(LOG_TAG, "Ending DB Transaction!");
            db.endTransaction();
            db.close();
        }

        return null;
    }

    @Override
    public ArrayList<Expense> getExpenses() {
        ArrayList<Expense> expenses = new ArrayList<>();

        // Readable DB Helper
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    ExpenseContract.ExpenseEntry._ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_Name,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate,
            };

            String sortOrder = ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate + " DESC";


            Cursor c = db.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    sortOrder);
            Log.i(LOG_TAG, "Cursor size" + c.getCount());
            if (c.moveToFirst()) {

                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry._ID));
                    String Name = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_Name));
                    String formattedDate = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate));

                    Log.i(LOG_TAG, String.format("Fetched Expense with Id:%d, Name:%s", Id, Name));
                    Expense expense = new Expense();
                    expense.Id = Id;
                    expense.Name = Name;
                    expense.LastUpdated = isoSdf.parse(formattedDate);

                    String[] exprojection = {
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE,
                            ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT,
                    };

                    String orderBy = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER + " DESC";

                    String selection = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId + " =?";
                    String[] selectionArgs = {String.valueOf(Id)};

                    HashMap<Long, Double> PaidBy = new HashMap<>();
                    HashMap<Long, Double> PaidTo = new HashMap<>();

                    try (SQLiteDatabase exDB = dbHelper.getReadableDatabase()) {
                        Cursor cex = exDB.query(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME,
                                exprojection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                orderBy);

                        if (cex.moveToFirst()) {

                            do {
                                long expenseId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId));
                                long payerId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER));
                                long payeeId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE));
                                Double amount = cex.getDouble(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT));

                                if (PaidBy.containsKey(payerId)) {
                                    Double existingAmount = PaidBy.get(payerId);
                                    PaidBy.remove(payerId);
                                    Double newAmount = existingAmount + amount;
                                    PaidBy.put(payerId, newAmount);
                                } else {
                                    PaidBy.put(payerId, amount);
                                }

                                if (PaidTo.containsKey(payeeId)) {
                                    Double existingAmount = PaidTo.get(payeeId);
                                    PaidTo.remove(payeeId);
                                    Double newAmount = existingAmount + amount;
                                    PaidTo.put(payeeId, newAmount);
                                } else {
                                    PaidTo.put(payeeId, amount);
                                }

                                Log.v(LOG_TAG, String.format("Expense Member Payee:%d, Payer:%d, Amount:%f", payeeId, payerId, amount));
                                if (expenseId != Id) {
                                    Log.e(LOG_TAG, "Got a different expense id than the one expected");
                                    //TODO: DB Corrupt have to check
                                    return new ArrayList<>();
                                }
                            } while (cex.moveToNext());

                            for (long memberId : PaidBy.keySet()) {
                                Member member = getMember(memberId);
                                expense.PaidBy.put(member, PaidBy.get(memberId));
                            }

                            for (long memberId : PaidTo.keySet()) {
                                Member member = getMember(memberId);
                                expense.PaidTo.put(member, PaidTo.get(memberId));
                            }
                        }
                    }

                    Log.i(LOG_TAG, " Expense" + expense);
                    expenses.add(expense);
                } while (c.moveToNext());

            }

        } catch (ParseException pex) {
            Log.e(LOG_TAG, "Exception while parsing : " + pex.toString());
            //throw new Exception("Not Able to parse the date format");
        }

        Log.i(LOG_TAG, "Returned expenses" + expenses.toString());
        return expenses;
    }

    @Override
    public Expense getExpense(long Id) {

        Expense expense = null;

        // Readable DB Helper
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    ExpenseContract.ExpenseEntry._ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_Name,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate,
            };

            String qselection = ExpenseContract.ExpenseEntry._ID + "=?";
            String qselection_arguments[] = {String.valueOf(Id)};


            String sortOrder = ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate + " DESC";


            Cursor c = db.query(ExpenseContract.ExpenseEntry.TABLE_NAME,
                    projection,
                    qselection,
                    qselection_arguments,
                    null,
                    null,
                    sortOrder);
            Log.i(LOG_TAG, "Cursor size" + c.getCount());
            if (c.moveToFirst()) {


                //long Id = c.getLong(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry._ID));
                String Name = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_Name));
                String formattedDate = c.getString(c.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_LastUpdate));

                Log.i(LOG_TAG, String.format("Fetched Expense with Id:%d, Name:%s", Id, Name));
                expense = new Expense();
                expense.Id = Id;
                expense.Name = Name;
                expense.LastUpdated = isoSdf.parse(formattedDate);

                String[] exprojection = {
                        ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId,
                        ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER,
                        ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE,
                        ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT,
                };

                String orderBy = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER + " DESC";

                String selection = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId + " =?";
                String[] selectionArgs = {String.valueOf(Id)};

                HashMap<Long, Double> PaidBy = new HashMap<>();
                HashMap<Long, Double> PaidTo = new HashMap<>();

                try (SQLiteDatabase exDB = dbHelper.getReadableDatabase()) {
                    Cursor cex = exDB.query(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME,
                            exprojection,
                            selection,
                            selectionArgs,
                            null,
                            null,
                            orderBy);

                    if (cex.moveToFirst()) {

                        do {
                            long expenseId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId));
                            long payerId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER));
                            long payeeId = cex.getLong(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE));
                            Double amount = cex.getDouble(cex.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT));

                            if (PaidBy.containsKey(payerId)) {
                                Double existingAmount = PaidBy.get(payerId);
                                PaidBy.remove(payerId);
                                Double newAmount = existingAmount + amount;
                                PaidBy.put(payerId, newAmount);
                            } else {
                                PaidBy.put(payerId, amount);
                            }

                            if (PaidTo.containsKey(payeeId)) {
                                Double existingAmount = PaidTo.get(payeeId);
                                PaidTo.remove(payeeId);
                                Double newAmount = existingAmount + amount;
                                PaidTo.put(payeeId, newAmount);
                            } else {
                                PaidTo.put(payeeId, amount);
                            }

                            Log.v(LOG_TAG, String.format("Expense Member Payee:%d, Payer:%d, Amount:%f", payeeId, payerId, amount));
                            if (expenseId != Id) {
                                Log.e(LOG_TAG, "Got a different expense id than the one expected");
                                //TODO: DB Corrupt have to check
                                return null;
                            }
                        } while (cex.moveToNext());

                        for (long memberId : PaidBy.keySet()) {
                            Member member = getMember(memberId);
                            expense.PaidBy.put(member, PaidBy.get(memberId));
                        }

                        for (long memberId : PaidTo.keySet()) {
                            Member member = getMember(memberId);
                            expense.PaidTo.put(member, PaidTo.get(memberId));
                        }
                    }
                }

                Log.i(LOG_TAG, " Expense" + expense);

            }

        } catch (ParseException pex) {
            Log.e(LOG_TAG, "Exception while parsing : " + pex.toString());
            //throw new Exception("Not Able to parse the date format");
        }

        Log.i(LOG_TAG, "Returned expense" + expense.toString());
        return expense;
    }

    @Override
    public Settlement addSettlement(Settlement settlement) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID, settlement.PayeeId);
            values.put(SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID, settlement.PayerId);
            values.put(SettlementContract.SettlementEntry.COLUMN_NAME_AMOUNT, settlement.PaymentAmount);

            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = db.insert(SettlementContract.SettlementEntry.TABLE_NAME, null, values);

            Log.i(LOG_TAG, "Added Settlement Id:" + newRowId);
            settlement.Id = newRowId;

            return settlement;
        }
    }

    @Override
    public ArrayList<Settlement> getSettlements() {
        ArrayList<Settlement> settlements = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    SettlementContract.SettlementEntry._ID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_AMOUNT,
            };

            String orderBy = SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID + " DESC";

            Cursor c = db.query(SettlementContract.SettlementEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    orderBy);

            if (c.moveToFirst()) {
                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry._ID));
                    long PayerId = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID));
                    long PayeeId = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID));
                    double Amount = c.getDouble(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_AMOUNT));

                    Settlement settlement = new Settlement(PayerId, PayeeId, Amount, Id);

                    settlements.add(settlement);
                } while (c.moveToNext());
            }
        }

        return settlements;
    }

    public ArrayList<Settlement> getSettlements(long memberId) {
        ArrayList<Settlement> settlements = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    SettlementContract.SettlementEntry._ID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID,
                    SettlementContract.SettlementEntry.COLUMN_NAME_AMOUNT,
            };

            String selection = SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID + " =? OR " + SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID + " =?";
            String[] selectionArgs = {String.valueOf(memberId), String.valueOf(memberId)};

            String orderBy = SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID + " DESC";

            Cursor c = db.query(SettlementContract.SettlementEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy);

            if (c.moveToFirst()) {
                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry._ID));
                    long PayerId = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_PAYERID));
                    long PayeeId = c.getLong(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_PAYEEID));
                    double Amount = c.getDouble(c.getColumnIndexOrThrow(SettlementContract.SettlementEntry.COLUMN_NAME_AMOUNT));

                    Settlement settlement = new Settlement(PayerId, PayeeId, Amount, Id);

                    settlements.add(settlement);
                } while (c.moveToNext());
            }
        }

        return settlements;
    }

    public ArrayList<MicroExpense> getMicroExpense(long memberId) {
        ArrayList<MicroExpense> microExpenses = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String[] projection = {
                    ExpenseMemberContract.ExpenseMemberEntry._ID,
                    ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER,
                    ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE,
                    ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT,
                    ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId,
            };

            String selection = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER + " =? OR " + ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE + " =?";
            String[] selectionArgs = {String.valueOf(memberId), String.valueOf(memberId)};

            String orderBy = ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER + " DESC";

            Cursor c = db.query(ExpenseMemberContract.ExpenseMemberEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    orderBy);

            if (c.moveToFirst()) {
                do {
                    long Id = c.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry._ID));
                    long PayerId = c.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYER));
                    long PayeeId = c.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_PAYEE));
                    double Amount = c.getDouble(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_AMOUNT));
                    long ExpenseId = c.getLong(c.getColumnIndexOrThrow(ExpenseMemberContract.ExpenseMemberEntry.COLUMN_NAME_ExpenseId));

                    MicroExpense microExpense = new MicroExpense(PayerId, PayeeId, Amount, ExpenseId, Id);
                    microExpenses.add(microExpense);
                } while (c.moveToNext());
            }
        }

        return microExpenses;
    }

    @Override
    public HashMap<Member, Double> getOutstandingTx(long memberId) {
        HashMap<Member, Double> outStandingTx = new HashMap<>();

        HashMap<Long, Double> localOS = new HashMap<>();

        ArrayList<Settlement> settlements = getSettlements(memberId);

        for (Settlement settlement : settlements) {
            double amount = 0;
            if (settlement.PayerId == memberId) {
                amount = settlement.PaymentAmount;
            } else if (settlement.PayeeId == memberId) {
                amount = -settlement.PaymentAmount;
            }

            if (localOS.containsKey(memberId)) {
                double newAmount = localOS.get(memberId) + amount;
                localOS.remove(memberId);
                localOS.put(memberId, newAmount);
            } else {
                localOS.put(memberId, amount);
            }
        }

        ArrayList<MicroExpense> microExpenses = getMicroExpense(memberId);

        for (MicroExpense microExpense : microExpenses) {
            double amount = 0;
            if (microExpense.PayerId == memberId) {
                amount = microExpense.Amount;
            } else if (microExpense.PayeeId == memberId) {
                amount = -microExpense.Amount;
            }

            if (localOS.containsKey(memberId)) {
                double newAmount = localOS.get(memberId) + amount;
                localOS.remove(memberId);
                localOS.put(memberId, newAmount);
            } else {
                localOS.put(memberId, amount);
            }
        }

        for (long id : localOS.keySet()) {
            Member member = getMember(id);
            outStandingTx.put(member, localOS.get(id));
        }

        return outStandingTx;
    }
}
