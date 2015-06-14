package com.tachys.moneyshare.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tachys.moneyshare.R;

public class CreateExpense extends ActionBarActivity {

    LinearLayout payer_layout, payee_layout;
    TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.enter_title);
        payer_layout = (LinearLayout) findViewById(R.id.payer_layout);
        payee_layout = (LinearLayout) findViewById(R.id.payee_layout);
        addPayer();
        addPayee();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public void addPayer() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) layoutInflater.inflate(R.layout.member_input_item, null);

        final TextView amt = (TextView) rl.findViewById(R.id.amt);
        final ImageButton bt_remove = (ImageButton) rl.findViewById(R.id.remove_item);
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payer_layout.removeView(rl);
            }
        });
        TextView tv = (TextView) rl.findViewById(R.id.member_input_name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemberDialog(v, bt_remove, amt, true);
            }
        });
        payer_layout.addView(rl);
    }

    public void showMemberDialog(final View v, final ImageButton remove_bt, final TextView amt, final boolean isPayer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateExpense.this);

        builder.setMessage("List of members");
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                remove_bt.setVisibility(View.VISIBLE);
                ((TextView) v).setText("Selected");
                amt.setText("34");
                v.setClickable(false);
                if (isPayer) {
                    addPayer();
                } else {
                    addPayee();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void addPayee() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) layoutInflater.inflate(R.layout.member_input_item, null);

        final TextView amt = (TextView) rl.findViewById(R.id.amt);
        final ImageButton bt_remove = (ImageButton) rl.findViewById(R.id.remove_item);
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payee_layout.removeView(rl);
            }
        });
        TextView et = (TextView) rl.findViewById(R.id.member_input_name);

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMemberDialog(v, bt_remove, amt, false);
            }
        });
        payee_layout.addView(rl);
    }

}
