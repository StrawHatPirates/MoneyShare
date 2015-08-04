package com.tachys.moneyshare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.tachys.moneyshare.R;
import com.tachys.moneyshare.adapter.ExpenseListAdapter;
import com.tachys.moneyshare.dataaccess.IDataAccess;
import com.tachys.moneyshare.dataaccess.db.DBAccess;
import com.tachys.moneyshare.util.CommonUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ExpenseListFragment extends ListFragment implements AbsListView.OnItemClickListener {

    private static final String TAG = "ExpenseListFragment";
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ExpenseListAdapter mAdapter;
    private IDataAccess dataAccess;

    public ExpenseListFragment() {
        Log.i(TAG, "ExpenseListFragment");

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        dataAccess = new DBAccess(getActivity().getBaseContext());
        mAdapter = new ExpenseListAdapter(getActivity().getBaseContext(), dataAccess.getExpenses());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        //TextView tv = (TextView) view.findViewById(R.id.expense_message);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        /*if (mAdapter.getCount() <= 0) {
            tv.setVisibility(View.VISIBLE);
        } else {*/
        //tv.setVisibility(View.GONE);
        mListView.setAdapter(mAdapter);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        //}
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(TAG, "onAttach");
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(dataAccess.getExpenses().get(position).getCumulativeAmount());
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonUtils.REQUEST_CREATE_EXPENSE && resultCode == CommonUtils.RESULT_CREATED_EXPENSE && data != null) {
            Long id = data.getBundleExtra("extra") != null ? (data.getBundleExtra("extra")).getLong("id", -1) : -1;
            if (id != -1) {
                mAdapter.add(dataAccess.getExpense(id));
            }
        }
    }
}
