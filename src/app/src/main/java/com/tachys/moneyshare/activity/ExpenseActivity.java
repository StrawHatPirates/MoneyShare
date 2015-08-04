package com.tachys.moneyshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.fragment.ExpenseListFragment;
import com.tachys.moneyshare.util.CommonUtils;

public class ExpenseActivity extends ActionBarActivity implements ExpenseListFragment.OnFragmentInteractionListener {

    private static final String TAG = "ExpenseActivity";
    IDataAccess dataAccess;
    Fragment listFragment;
    FrameLayout fragment;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.expense_activity);
        tv = (TextView) findViewById(R.id.expense_message);
        fragment = (FrameLayout) findViewById(R.id.expense_fragment);
        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //listFragment = new ExpenseListFragment();

        initialize(savedInstanceState != null);

    }


    public void initialize(boolean isSaved) {
        dataAccess = new DBAccess(getBaseContext());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (dataAccess.getExpenses().size() > 0) {
            tv.setVisibility(View.GONE);
            fragment.setVisibility(View.VISIBLE);
            if (findViewById(R.id.expense_fragment) != null) {
                if (isSaved) {
                    return;
                }
                listFragment = new ExpenseListFragment();

                transaction.add(R.id.expense_fragment, listFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        } else {
            tv.setVisibility(View.VISIBLE);
            fragment.setVisibility(View.GONE);
            if (listFragment != null) {
                transaction.remove(listFragment);
                transaction.commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        } else if (id == R.id.add_expense) {
            Intent i = new Intent();
            i.setClassName(getBaseContext(), "com.tachys.moneyshare.activity.CreateExpense");
            startActivityForResult(i, CommonUtils.REQUEST_CREATE_EXPENSE, null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        Log.i(TAG, "onFragmentInteraction");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        /*FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.detach(listFragment);
        fragTransaction.attach(listFragment);
        fragTransaction.commit();*/

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult");
        if (listFragment != null) {
            listFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            initialize(false);
        }
    }
}
