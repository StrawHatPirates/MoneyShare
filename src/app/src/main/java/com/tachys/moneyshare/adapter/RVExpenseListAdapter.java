package com.tachys.moneyshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.model.Expense;
import com.tachys.moneyshare.util.CommonUtils;

import java.util.List;

public class RVExpenseListAdapter extends RecyclerView.Adapter<RVExpenseListAdapter.RVViewHolder> {

    private static final String TAG = "RVExpenseListAdapter";

    Context mContext;
    List<Expense> items;

    public RVExpenseListAdapter(Context ctx, List<Expense> items) {
        mContext = ctx;
        this.items = items;
    }

    @Override
    public RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_expense_list_item, parent, false);
        return new RVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RVViewHolder holder, int position) {
        Log.d(TAG, "size " + items.size() + " " + position);
        final Expense exp = items.get(position);
        holder.id = exp.Id;
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClassName(mContext, "com.tachys.moneyshare.activity.ExpenseDetail");
                Log.i(TAG, "expense id " + exp.Id);
                i.putExtra("expense_id", exp.Id);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
        holder.title.setText(exp.Name);
        //TODO: Get Current User details from the SharedPref and use it here.
        double amountForPerson = exp.getAmountForPerson(CommonUtils.my_member_id);
        holder.amt.setText(String.valueOf(amountForPerson));
        /*if (amountForPerson >= 0) {
            holder.rl.setBackgroundResource(R.color.green_background);
        } else {
            holder.rl.setBackgroundResource(R.color.red_background);
        }*/
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class RVViewHolder extends RecyclerView.ViewHolder {
        long id;
        RelativeLayout rl;
        TextView title;
        TextView amt;

        public RVViewHolder(View itemView) {
            super(itemView);
            rl = (RelativeLayout) itemView.findViewById(R.id.cardview_expense_list_item);
            title = (TextView) itemView.findViewById(R.id.expense_title);
            amt = (TextView) itemView.findViewById(R.id.amount);
        }
    }

    public void add(Expense e) {
        Log.i(TAG, "adapter add call");
        items.add(e);
        notifyDataSetChanged();
    }

    public void set(List<Expense> l) {
        items = l;
        notifyDataSetChanged();
    }

}
