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

package cn.jdywl.driver.adapter.stage;

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
import cn.jdywl.driver.ui.stage.StageOrderInfoActivity;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class SMarketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = LogHelper.makeLogTag(SMarketAdapter.class);

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DATA = 1;

    private List<StageOrderItem> mDataSet;
    private int mType;

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
    public static class DataViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_route)
        TextView tvRoute;
        @Bind(R.id.tv_senddate)
        TextView tvSenddate;
        @Bind(R.id.tv_carinfo)
        TextView tvCarinfo;
        @Bind(R.id.tv_marketprice)
        TextView tvMarketprice;
        @Bind(R.id.tv_expprice)
        TextView tvExpprice;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_orderType)
        TextView tvOrderType;

        public final View mView;

        public DataViewHolder(View v) {
            super(v);
            mView = v;
            ButterKnife.bind(this, v);

            /*
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                    if (mType == DRIVER_TYPE) {
                        Intent it = new Intent(mContext, DDetailActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable("order", order);
                        it.putExtras(bundle);

                        mContext.startActivity(it);
                    }

                }
            });
            */
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public SMarketAdapter(List<StageOrderItem> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
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
                .inflate(R.layout.item_market, viewGroup, false);

        return new DataViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        //数据为空，显示empty holder
        if (viewHolder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyHolder = (EmptyViewHolder) viewHolder;
            emptyHolder.tvEmpty.setText("空空如也~\n请您稍后再来看看");
            return;
        }

        //正常数据
        DataViewHolder dataHolder = (DataViewHolder) viewHolder;
        final StageOrderItem data = mDataSet.get(position);

        final Context context = dataHolder.mView.getContext();

        // Define click listener for the ViewHolder's View.
        dataHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                Intent it = new Intent(context, StageOrderInfoActivity.class);

                Bundle bundle = new Bundle();
                bundle.putParcelable("order", data);
                it.putExtras(bundle);
                it.putExtra("from", StageOrderInfoActivity.FROM_SMARKET);
                context.startActivity(it);
            }
        });

        dataHolder.tvRoute.setText(data.getOrigin() + " — " + data.getDestination());
        dataHolder.tvSenddate.setText(data.getSendtime() + " 启运");
        dataHolder.tvMarketprice.setText("市场运价: " + data.getCharge() + "元");
        dataHolder.tvExpprice.setText("车价: " + data.getCar_price() + "元");
        dataHolder.tvCarinfo.setText("提车地址："+data.getFrom_address());
        int status = data.getStatus();
        //String[] sstatus = getContext().getResources().getStringArray(R.array.status);
        dataHolder.tvStatus.setText(OrderStatus.getDesc(status));

        //散车业务不现实竞拍价
//        if (data.getCarNum() < ApiConfig.CAR_SIZE) {
//            dataHolder.tvExpprice.setVisibility(View.GONE);
//
//            //散车
//            dataHolder.tvOrderType.setBackgroundColor(context.getResources().getColor(R.color.bg_sanche));
//            dataHolder.tvOrderType.setText("散车");
//        } else {
//            dataHolder.tvExpprice.setVisibility(View.VISIBLE);
//            dataHolder.tvExpprice.setText("期望运价: " + data.getExpectationPrice() + "元");
//
//            //整版车
//            dataHolder.tvOrderType.setBackgroundColor(context.getResources().getColor(R.color.bg_zhengban));
//            dataHolder.tvOrderType.setText("整板");
//        }
//
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
