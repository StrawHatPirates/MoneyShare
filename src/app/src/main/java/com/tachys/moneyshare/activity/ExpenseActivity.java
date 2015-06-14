package com.tachys.moneyshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.fragment.ExpenseListFragment;

public class ExpenseActivity extends ActionBarActivity implements ExpenseListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_activity);

        if (findViewById(R.id.expense_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }


            Fragment listFragment = new ExpenseListFragment();

            getSupportFragmentManager().beginTransaction().add(R.id.expense_fragment, listFragment).commit();


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
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}