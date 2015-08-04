package com.tachys.moneyshare.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.model.Member;
import com.tachys.moneyshare.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateExpense extends ActionBarActivity {

    private static final String TAG = "CreateExpense";
    LinearLayout payer_layout, payee_layout;
    TextView title;
    IDataAccess dataAccess;
    Expense e;
    HashMap<Member, Double> payer;
    HashMap<Member, Double> payee;
    Context mContex;
    double total_expense = 0;
    boolean isEqualSplit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        dataAccess = new DBAccess(getBaseContext());
        mContex = getBaseContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.enter_title);
        payer_layout = (LinearLayout) findViewById(R.id.payer_layout);
        payee_layout = (LinearLayout) findViewById(R.id.payee_layout);
        e = new Expense();
        payer = new HashMap<>();
        payee = new HashMap<>();

        showSplitDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_expense, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.create) {
            String str_title = title.getText().toString().trim();
            if (str_title.length() <= 0) {
                Toast.makeText(getBaseContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (e.PaidBy.size() <= 0) {
                Toast.makeText(getBaseContext(), "Please select payers/payers", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (e.PaidTo.size() <= 0) {
                Toast.makeText(getBaseContext(), "Please select payees/payees", Toast.LENGTH_SHORT).show();
                return false;
            }
            e.Name = title.getText().toString().trim();
            Log.i(TAG, " name " + e.Name + " by " + e.PaidBy.size() + " to " + e.PaidBy.size());

            //Expense calculated_exp = ProcessorUtil.getIntstance().getCalculatedExpense(e,isEqualSplit);

            int viewCount = payer_layout.getChildCount();
            Log.i(TAG, "payers count " + viewCount);

            for (int i = 0; i < viewCount - 1; ++i) {
                String str = ((TextView) payer_layout.getChildAt(i).findViewById(R.id.member_input_name)).getText().toString();
                String amt = ((TextView) payer_layout.getChildAt(i).findViewById(R.id.amt)).getText().toString();
                Log.i(TAG, "str for " + i + " " + str + " wid " + amt);
            }

            HashMap<Member, Double> paidTo = new HashMap<>();
            if (isEqualSplit) {
                int total_members = e.PaidBy.size() + e.PaidTo.size();

                double perHeadShare = total_expense / total_members;

                for (Member m : e.PaidTo.keySet()) {
                    paidTo.put(m, perHeadShare);
                }

                for (Member m : e.PaidBy.keySet()) {
                    paidTo.put(m, perHeadShare);
                }

                e.PaidTo = paidTo;
            }

            Expense exp = dataAccess.addExpense(e);
            Intent intent = new Intent();
            Bundle b = new Bundle();
            b.putLong("id", exp.Id);
            intent.putExtra("extra", b);
            setResult(CommonUtils.RESULT_CREATED_EXPENSE, intent);
            finish();
            return true;
        } else if (id == R.id.home) {
            super.onBackPressed();
        }

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    public void showSplitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateExpense.this);
        builder.setMessage("Select type of split");
        builder.setCancelable(false);


        builder.setPositiveButton(R.string.equal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                addPayer();
                addPayee();
            }
        });
        builder.setNegativeButton(R.string.unequal, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                isEqualSplit = false;
                dialog.dismiss();
                addPayer();
                addPayee();

            }
        });

        builder.create().show();
    }


    public void showMemberDialog(final DialogResultListner drl, final View v, final ImageButton remove_bt, final ImageButton add_bt) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateExpense.this);

        builder.setMessage("List of members");
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listview = layoutInflater.inflate(R.layout.create_exp_dialog_layout, null);
        LinearLayout ll = (LinearLayout) listview.findViewById(R.id.radiolistview);
        ArrayList<Member> listMembers = DBAccess.getInstance(getBaseContext()).getMembers();
        ArrayList<Member> listContacts = DBAccess.getInstance(getBaseContext()).getAllContacts();
        listMembers.addAll(listContacts);

        RadioGroup rg = new RadioGroup(mContex);
        rg.setOrientation(RadioGroup.VERTICAL);
        Member m;
        for (int i = 0; i < listMembers.size(); ++i) {
            RadioButton rbt = new RadioButton(mContex);
            rbt.setTextColor(getResources().getColor(android.R.color.black));
            m = listMembers.get(i);
            if (m.Name != null)
                rbt.setText(m.Name);
            else if (m.Email != null) {
                rbt.setText(m.Email);
            } else if (m.Phone != null) {
                rbt.setText(m.Phone);
            }
            rbt.setId(i);
            rg.addView(rbt);
        }

        ll.addView(rg);

        final RadioGroup frg = rg;
        final ArrayList<Member> list = listMembers;

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Member m = null;
                int index = frg.getCheckedRadioButtonId();
                if (index >= 0) {
                    m = list.get(index);
                } else {
                    Toast.makeText(mContex, "Select member", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "test");
                if (m != null) {
                    Log.i(TAG, "test " + m.Name);
                    String titleStr = m.Name;
                    if (titleStr != null && titleStr.length() > 0) {
                        ((TextView) v).setText(titleStr);
                    } else {
                        ((TextView) v).setText("None");
                    }


                    drl.onResult(m);

                }
                v.setEnabled(true);
                dialog.dismiss();

            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                v.setEnabled(true);
                dialog.dismiss();
            }
        });

        builder.setView(listview);
        builder.show();
    }

    public void addPayee() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) layoutInflater.inflate(R.layout.member_input_item, null);
        final Member[] local_var = new Member[1];
        final Button more_button = (Button) rl.findViewById(R.id.morebutton);
        final ImageButton bt_remove = (ImageButton) rl.findViewById(R.id.remove_item);
        final ImageButton bt_add = (ImageButton) rl.findViewById(R.id.add_item);
        final TextView tv = (TextView) rl.findViewById(R.id.member_input_name);
        final EditText amt = (EditText) rl.findViewById(R.id.amt);
        if (isEqualSplit) {
            amt.setVisibility(View.GONE);
        }

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_button.setVisibility(View.GONE);
                bt_add.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
            }
        });
        final DialogResultListner listner = new DialogResultListner() {
            @Override
            public void onResult(Member m) {
                Log.i(TAG, "payee ");
                local_var[0] = m;
                Log.i(TAG, "payee " + m.toString());
                if (m != null) {
                    tv.setText(m.Name);

                }
            }
        };
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.PaidTo.remove(local_var[0]);
                payee_layout.removeView(rl);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setEnabled(false);
                showMemberDialog(listner, v, bt_remove, bt_add);
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tv.getText().toString().trim();
                String str_price = amt.getText().toString().trim();
                if (name != null && name.length() > 0 && (isEqualSplit || (str_price != null && str_price.length() > 0))) {
                    bt_remove.setVisibility(View.VISIBLE);
                    bt_add.setVisibility(View.GONE);
                    tv.setEnabled(false);
                    if (isEqualSplit) {
                        str_price = "0";
                    }
                    e.PaidTo.put(local_var[0], Double.valueOf(str_price));
                    Log.i(TAG, "add payee added " + local_var[0].Name);
                    Log.i(TAG, "add payee " + e.PaidTo.size());
                    addPayee();
                } else {
                    Toast.makeText(getBaseContext(), "Please select member", Toast.LENGTH_SHORT).show();
                }
            }
        });


        payee_layout.addView(rl);
    }

    public void addPayer() {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout rl = (RelativeLayout) layoutInflater.inflate(R.layout.member_input_item, null);

        final EditText amt = (EditText) rl.findViewById(R.id.amt);

        final Member[] local_var = new Member[1];
        final Button more_button = (Button) rl.findViewById(R.id.morebutton);
        final ImageButton bt_remove = (ImageButton) rl.findViewById(R.id.remove_item);
        final ImageButton bt_add = (ImageButton) rl.findViewById(R.id.add_item);
        final TextView tv = (TextView) rl.findViewById(R.id.member_input_name);

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_button.setVisibility(View.GONE);
                bt_add.setVisibility(View.VISIBLE);
                amt.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
            }
        });

        final DialogResultListner listner = new DialogResultListner() {
            @Override
            public void onResult(Member m) {
                local_var[0] = m;
                Log.i(TAG, "payer ");
                if (m != null) {
                    Log.i(TAG, " payer is " + m.Name);
                    tv.setText(m.Name);

                }
            }
        };
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "payer remove ");
                total_expense -= e.PaidBy.get(local_var[0]);
                e.PaidBy.remove(local_var[0]);
                payer_layout.removeView(rl);

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setEnabled(false);
                showMemberDialog(listner, v, bt_remove, bt_add);
            }
        });
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_price = amt.getText().toString().trim();
                String name = tv.getText().toString().trim();
                if (name != null && name.length() > 0 && str_price != null && str_price.length() > 0) {
                    double price = Double.valueOf(str_price);
                    amt.setEnabled(false);
                    tv.setEnabled(false);
                    bt_remove.setVisibility(View.VISIBLE);
                    bt_add.setVisibility(View.GONE);
                    e.PaidBy.put(local_var[0], price);
                    total_expense += price;
                    Log.i(TAG, "add payer added " + local_var[0].Name + " " + price);
                    Log.i(TAG, "add payer " + e.PaidBy.size());
                    addPayer();
                } else {
                    Toast.makeText(getBaseContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        payer_layout.addView(rl);
    }


    public interface DialogResultListner {
        public void onResult(Member m);
    }

}
