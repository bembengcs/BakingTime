package net.mavenmobile.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mavenmobile.bakingtime.R;
import net.mavenmobile.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bembengcs on 8/20/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {

    private List<Step> mStepList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public StepAdapter(List<Step> stepList, Context context, OnItemClickListener onItemClickListener) {
        mStepList = stepList;
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_step_number)
        TextView mTvStepNumber;
        @BindView(R.id.tv_step_name)
        TextView mTvStepName;
        @BindView(R.id.relative_layout)
        RelativeLayout mRelativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Step step, final int position) {

            if (step.getId() == 0) {
                mTvStepNumber.setText("Introduction");
            } else {
                mTvStepNumber.setText("Step " + String.valueOf(step.getId()));
            }
            mTvStepName.setText(step.getShortDescription());

            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(position);
                }
            });

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list_step, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mStepList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
