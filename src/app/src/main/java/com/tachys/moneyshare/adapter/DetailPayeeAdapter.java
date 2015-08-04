package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.model.Payee;

import java.util.List;

/**
 * Created by tejom_000 on 26-07-2015.
 */
public class DetailPayeeAdapter extends BaseAdapter {
    List<Payee> list;
    Context mContext;

    public class ViewHolder {
        TextView name;
        TextView amt;
    }

    public DetailPayeeAdapter(Context context, List<Payee> items) {
        mContext = context;
        list = items;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Payee getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expense_detail_payer, null);
            holder.name = (TextView) convertView.findViewById(R.id.mem_name);
            holder.amt = (TextView) convertView.findViewById(R.id.mem_amount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(getItem(position).name == null ? "None" : getItem(position).name);
        holder.amt.setText(String.valueOf(getItem(position).amountOwed));
        return convertView;
    }
}
