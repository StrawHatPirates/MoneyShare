package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.model.Member;

import java.util.ArrayList;

/**
 * Created by tejom_000 on 16-06-2015.
 */
public class SimpleMemberListAdapter extends BaseAdapter {

    boolean isChecked[];
    ArrayList<Member> membersList;
    Context mContext;

    public SimpleMemberListAdapter(Context ctx, ArrayList<Member> list) {
        mContext = ctx;
        membersList = list;
        isChecked = new boolean[membersList.size()];
    }

    public class ViewHolder {
        TextView name;
        CheckBox cb;
    }

    @Override
    public int getCount() {
        if (membersList != null)
            return membersList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (membersList != null && membersList.size() > 0)
            return membersList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = (View) inflater.inflate(R.layout.create_exp_dialog_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.contactname);
            holder.cb = (CheckBox) convertView.findViewById(R.id.contactcheckBox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (membersList != null && membersList.size() > 0)
            holder.name.setText(membersList.get(position).Name);
        if (isChecked[position])
            holder.cb.setChecked(true);
        else
            holder.cb.setChecked(false);
        final ViewHolder tmpHolder = holder;
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tmpHolder.cb.isChecked())
                    isChecked[position] = true;
                else
                    isChecked[position] = false;
            }
        });

        return convertView;
    }
}
