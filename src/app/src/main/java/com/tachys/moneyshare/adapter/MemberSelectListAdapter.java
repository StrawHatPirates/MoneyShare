package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.model.Member;

import java.util.ArrayList;

/**
 * Created by tejom_000 on 26-07-2015.
 */
public class MemberSelectListAdapter extends BaseAdapter {

    int isSelected;
    ArrayList<Member> membersList;
    Context mContext;

    public class ViewHolder {
        int id;
        TextView name;
        RadioButton radioButton;
    }

    public MemberSelectListAdapter(Context context, ArrayList<Member> list) {
        mContext = context;
        membersList = list;
        isSelected = -1;
    }

    public Member getSelectedMember() {
        return isSelected != -1 ? getItem(isSelected) : null;
    }


    @Override
    public int getCount() {
        return membersList != null ? membersList.size() : 0;
    }

    @Override
    public Member getItem(int position) {
        return membersList != null ? membersList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.create_exp_radio_button, null);
            holder.name = (TextView) convertView.findViewById(R.id.member_name);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.radio_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(getItem(position).Name);
        holder.radioButton.setSelected(position == isSelected);

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = position;
                notifyDataSetChanged();

            }
        });

        return convertView;
    }
}
