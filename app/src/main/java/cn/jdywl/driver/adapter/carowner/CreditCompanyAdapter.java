/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package cn.jdywl.driver.adapter.carowner;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.model.CreditCompanyItem;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CreditCompanyAdapter extends RecyclerView.Adapter<CreditCompanyAdapter.ViewHolder> {
    private static final String TAG = CreditCompanyAdapter.class.getSimpleName();

    final private List<CreditCompanyItem> mDataSet;

    private OnItemClickListener mItemListener;

    public interface OnItemClickListener {
        void onItemClick(CreditCompanyItem item);
    }

    private OnBtnClickListener mBtnListener;

    public interface OnBtnClickListener {
        void onBtnClick(CreditCompanyItem item);
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public final class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_company;
        private final TextView tv_interest;
        private final Button btn_info;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                    if (mItemListener != null) {
                        mItemListener.onItemClick(mDataSet.get(getPosition()));
                    }
                }
            });
            tv_company = (TextView) v.findViewById(R.id.tv_company);
            tv_interest = (TextView) v.findViewById(R.id.tv_interest);
            btn_info = (Button) v.findViewById(R.id.btn_info);
            btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mBtnListener != null) {
                        mBtnListener.onBtnClick(mDataSet.get(getPosition()));
                    }
                }
            });

        }

        public TextView getTvCompany() {
            return tv_company;
        }

        public TextView getTvInterest() {
            return tv_interest;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CreditCompanyAdapter(List<CreditCompanyItem> dataSet) {
        mDataSet = dataSet;
    }

    //设置Item点击监听器
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.mItemListener = onItemClickListener;
    }

    //设置Button点击监听器
    public void setOnBtnClickListener(@Nullable OnBtnClickListener onBtnClickListener) {
        this.mBtnListener = onBtnClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_credit_company, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //LogHelper.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTvCompany().setText(mDataSet.get(position).getCompany());
        viewHolder.getTvInterest().setText(mDataSet.get(position).getInterest());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
