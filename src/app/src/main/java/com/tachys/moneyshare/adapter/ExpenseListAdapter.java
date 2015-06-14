package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.fragment.dummy.DummyContent;

import java.util.List;

public class ExpenseListAdapter extends ArrayAdapter<DummyContent.DummyItem> {
    public ExpenseListAdapter(Context context, List<DummyContent.DummyItem> items) {
        super(context, R.layout.expense_list_item, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.expense_list_item, null);
            holder = new ViewHolder();
            holder.rl = (RelativeLayout) convertView.findViewById(R.id.expense_list_item);
            holder.title = (TextView) convertView.findViewById(R.id.expense_title);
            holder.amt = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(getContext(), "com.tachys.moneyshare.activity.ExpenseDetail");
                i.putExtra("data_budle", getItemId(position));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });
        holder.title.setText(getItem(position).title);
        holder.amt.setText(getItem(position).amt);
        if (Integer.valueOf(getItem(position).amt) >= 0) {
            holder.rl.setBackgroundResource(R.color.green_background);
        } else {
            holder.rl.setBackgroundResource(R.color.red_background);
        }
        return convertView;
    }

    private static class ViewHolder {
        RelativeLayout rl;
        TextView title;
        TextView amt;
    }
}