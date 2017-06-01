package com.nonda.dtc.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nonda.dtc.R;
import com.nonda.dtc.model.DTCError;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by whaley on 2017/5/28.
 */

public class IssueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private DTCError mError;

    public IssueListAdapter(Context context) {
        this.mContext = context;
    }

    public void setIssueList(DTCError error) {
        this.mError = error;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_issue, parent, false);
        return new IssueListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IssueListViewHolder viewHolder = (IssueListViewHolder)holder;
        viewHolder.issueName.setText(mError.getErrors().get(position));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd", Locale.US);

        viewHolder.checkTime.setText(dateFormat.format(System.currentTimeMillis()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mError == null ? 0 : mError.getErrors().size();
    }

    public class IssueListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.issue_name)
        TextView issueName;

        @BindView(R.id.check_time)
        TextView checkTime;


        public IssueListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
