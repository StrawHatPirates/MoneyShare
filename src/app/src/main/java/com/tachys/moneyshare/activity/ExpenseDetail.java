package com.tachys.moneyshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.adapter.DetailPayeeAdapter;
import com.tachys.moneyshare.adapter.DetailPayerAdapter;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;
import com.tachys.moneyshare.model.Payee;
import com.tachys.moneyshare.model.Payer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExpenseDetail extends ActionBarActivity {

    private static final String TAG = "ExpenseDetail";
    TextView title, amount;
    ListView payerList, payeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.detail_title);
        amount = (TextView) findViewById(R.id.detail_amt_cumulative);
        payerList = (ListView) findViewById(R.id.detail_payer_list);
        payeeList = (ListView) findViewById(R.id.detail_payee_list);
        Log.i(TAG, "onCreate ");
        Intent dataIntent = getIntent();
        if (dataIntent != null) {
            long id = dataIntent.getLongExtra("expense_id", -1);
            Log.i(TAG, "onCreate id " + id);
            if (id != -1) {
                initialize(DBAccess.getInstance(getBaseContext()).getExpense(id));
            }
        }
    }

    public void initialize(Expense exp) {
        if (exp == null) {
            return;
        }

        title.setText(exp.Name);
        amount.setText("Total: " + String.valueOf(exp.getTotalAmount()));
        List<Payee> payees = new ArrayList<>();
        List<Payer> payers = new ArrayList<>();

        Set<Member> memSet = exp.PaidBy.keySet();
        Log.i(TAG, "paid by size " + memSet.size());

        for (Member m : memSet) {
            if (m != null)
                payers.add(new Payer(m.Name, exp.PaidBy.get(m)));
        }

        memSet = exp.PaidTo.keySet();
        Log.i(TAG, "paid to size " + memSet.size());

        for (Member m : memSet) {
            if (m != null)
                payees.add(new Payee(m.Name, exp.PaidTo.get(m)));
        }

        DetailPayerAdapter payerAdapter = new DetailPayerAdapter(getBaseContext(), payers);
        DetailPayeeAdapter payeeAdapter = new DetailPayeeAdapter(getBaseContext(), payees);
        payerList.setAdapter(payerAdapter);
        payeeList.setAdapter(payeeAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_expense_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }/* else if (id == R.id.home) {
            super.onBackPressed();
        }*/

        return super.onOptionsItemSelected(item);
    }
}
