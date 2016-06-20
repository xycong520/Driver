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

package cn.jdywl.driver.adapter.drayage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.config.OrderStatus;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.StageOrderItem;
import cn.jdywl.driver.ui.drayage.DrayageOrderInfoActivity;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class DTransportingRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = LogHelper.makeLogTag(DTransportingRvAdapter.class);

    private List<StageOrderItem> mDataSet;

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DATA = 1;

    /**
     * 当列表为空时显示“空试图”
     */
    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvEmpty;

        public EmptyViewHolder(View v) {
            super(v);

            tvEmpty = (TextView) v.findViewById(R.id.tv_empty);
        }

        public TextView getTvEmpty() {
            return tvEmpty;
        }
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    static class DataViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_route)
        TextView tvRoute;
        @Bind(R.id.tv_orderno)
        TextView tvOrderno;
        @Bind(R.id.tv_carinfo)
        TextView tvCarinfo;
        @Bind(R.id.tv_marketprice)
        TextView tvMarketprice;
        @Bind(R.id.tv_bill)
        TextView tvBill;
        @Bind(R.id.tv_location)
        TextView tvLocation;
        @Bind(R.id.tv_status)
        TextView tvStatus;

        public final View mView;

        public DataViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, v);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public DTransportingRvAdapter(List<StageOrderItem> dataSet) {
        mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_EMPTY) {
            //inflate your layout and pass it to view holder
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_empty, viewGroup, false);
            return new EmptyViewHolder(v);

        }

        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_ctransporting, viewGroup, false);

        return new DataViewHolder(v);
    }

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        //数据为空，显示empty holder
        if (viewHolder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) viewHolder;
            emptyHolder.tvEmpty.setText("空空如也~\n托运车辆请点击右下角的“下单”按钮");
            return;
        }

        //正常数据
        DataViewHolder dataHolder = (DataViewHolder) viewHolder;

        final StageOrderItem data = mDataSet.get(position);

        final Context context = dataHolder.mView.getContext();

        // Define click listener for the DataViewHolder's View.
        dataHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                Intent it = new Intent(context, DrayageOrderInfoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("order", data);
                it.putExtras(bundle);
                it.putExtra("from", DrayageOrderInfoActivity.FROM_STRANSPORTING);
                context.startActivity(it);
            }
        });

        dataHolder.tvRoute.setText(data.getDestination());
        dataHolder.tvOrderno.setText(data.getOrder_no());
        dataHolder.tvMarketprice.setText("接收人: " + data.getReceiver_name() + "\n" +
                "联系方式：" + data.getReceiver_phone()
        );
        dataHolder.tvBill.setText(String.format("成交运价:%.2f元",data.getCharge()));

        dataHolder.tvLocation.setText("");
        dataHolder.tvStatus.setText(OrderStatus.getDesc(data.getStatus()));
        dataHolder.tvCarinfo.setVisibility(View.GONE);
//        dataHolder.tvCarinfo.setText(Helper.getCarTypeByid(context, data.getBrand()) + " " + data.getCarNum() + "辆");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size() > 0 ? mDataSet.size() : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSet.size() == 0) {
            return TYPE_EMPTY;
        } else {
            return TYPE_DATA;
        }
    }
}
