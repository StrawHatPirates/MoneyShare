package com.tachys.moneyshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.model.Member;
import com.tachys.moneyshare.util.CommonUtils;

public class MoneyShareWelcome extends ActionBarActivity {

    EditText name, mail, ph;
    Button bt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_share_welcome);
        name = (EditText) findViewById(R.id.field_input_name);
        mail = (EditText) findViewById(R.id.field_input_mail);
        ph = (EditText) findViewById(R.id.field_input_phone);
        bt_submit = (Button) findViewById(R.id.submit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String my_name = name.getText().toString();
                if (my_name.trim().length() <= 0) {
                    Toast.makeText(getBaseContext(), "Name cannot be empty. All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String my_mail = mail.getText().toString();
                if (my_mail.trim().length() <= 0) {
                    Toast.makeText(getBaseContext(), "Mail ID cannot be empty. All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(my_mail).matches()) {
                    Toast.makeText(getBaseContext(), "Incorrect Mail ID format.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String my_num = ph.getText().toString();
                if (my_num.trim().length() <= 0) {
                    Toast.makeText(getBaseContext(), "Contact number cannot be empty. All fields are required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                IDataAccess ida = DBAccess.getInstance(getBaseContext());

                Member m = new Member(my_name, my_mail, my_num);

                CommonUtils.setPref(getBaseContext(), "my_name", my_name);
                CommonUtils.setPref(getBaseContext(), "my_mail_id", my_mail);
                CommonUtils.setPref(getBaseContext(), "my_phone", my_num);
                CommonUtils.my_member_id = (ida.addMember(m)).Id;
                CommonUtils.setPref(getBaseContext(), "my_member_id", String.valueOf(CommonUtils.my_member_id));
                CommonUtils.my_member_name = my_name;
                CommonUtils.my_member_mail = my_mail;
                CommonUtils.my_member_phone = my_num;

                startActivity(new Intent().setClassName(getBaseContext(), "com.tachys.moneyshare.activity.RVExpenseActivity"));
                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_money_share_welcome, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
