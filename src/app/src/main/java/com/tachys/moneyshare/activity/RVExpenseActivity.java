package com.tachys.moneyshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.adapter.RVExpenseListAdapter;
import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.fragment.ExpenseListFragment;
import com.tachys.moneyshare.util.CommonUtils;

public class RVExpenseActivity extends ActionBarActivity implements ExpenseListFragment.OnFragmentInteractionListener {

    private static final String TAG = "RVExpenseActivity";
    IDataAccess dataAccess;
    //Fragment listFragment;
    //FrameLayout fragment;
    TextView tv;
    RVExpenseListAdapter rvAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.rv_expense_activity);
        tv = (TextView) findViewById(R.id.rv_expense_message);
        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_expense_fragment);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(llm);
        dataAccess = new DBAccess(getBaseContext());
        rvAdapter = new RVExpenseListAdapter(getBaseContext(), dataAccess.getExpenses());
        rv.setAdapter(rvAdapter);

        //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //listFragment = new ExpenseListFragment();

        //initialize(savedInstanceState != null);

    }


    /* public void initialize(boolean isSaved) {
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
 */
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
        if (rvAdapter != null) {
            rvAdapter.set(dataAccess.getExpenses());
        }
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
        if (requestCode == CommonUtils.REQUEST_CREATE_EXPENSE && resultCode == CommonUtils.RESULT_CREATED_EXPENSE && data != null) {
            Long id = data.getBundleExtra("extra") != null ? (data.getBundleExtra("extra")).getLong("id", -1) : -1;
            if (id != -1 && rvAdapter != null) {
                Log.i(TAG, "onActivityResult adding");
                rvAdapter.add(dataAccess.getExpense(id));
                rvAdapter.notifyDataSetChanged();
            }

        }
    }
}
