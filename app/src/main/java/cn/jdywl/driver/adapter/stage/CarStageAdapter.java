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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.Stage;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CarStageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = LogHelper.makeLogTag(CarStageAdapter.class);

    private static final int TYPE_EMPTY = 0;
    private static final int TYPE_DATA = 1;

    private List<Stage> mDataSet;
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

        @Bind(R.id.layoutInfo)
        LinearLayout layoutInfo;
        @Bind(R.id.tv_station)
        TextView tvStation;
        @Bind(R.id.tv_operation_center)
        TextView tvOperationCenter;
        @Bind(R.id.tv_city)
        TextView tvCity;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        int[] bg={
                R.drawable.ic_textbg,
                R.drawable.ic_textbg2,
                R.drawable.ic_textbg3,
                R.drawable.ic_textbg4,
        };
        int[] color={
                0xff52a0c1,0xff52f0f1,0xfff2a0f1,0xff32a0c1
        };
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
    public CarStageAdapter(List<Stage> dataSet) {
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
                .inflate(R.layout.item_carstage, viewGroup, false);

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
        final Stage data = mDataSet.get(position);

        final Context context = dataHolder.mView.getContext();

        // Define click listener for the ViewHolder's View.
        dataHolder.mView.setTag(position);
        dataHolder.mView.setOnClickListener(onClickListener);
        dataHolder.tvAddress.setText(data.getAddress());
        dataHolder.tvOperationCenter.setText(data.getOperation_center());
        dataHolder.tvOperationCenter.setVisibility(View.GONE);
        dataHolder.tvStation.setText(data.getStation());
        dataHolder.tvCity.setText(data.getCity());
        for (int i=0;i<data.getServices().size();i++){
            if (i==4){
                break;
            }else{
                View view = LayoutInflater.from(dataHolder.layoutInfo.getContext()).inflate(R.layout.item_text_,null);
                TextView textView = (TextView) view.findViewById(R.id.tv);
                textView.setBackgroundResource(dataHolder.bg[i%4]);
                textView.setText(data.getServices().get(i).getName());
                textView.setTextColor(dataHolder.color[i%4]);
                dataHolder.layoutInfo.addView(view);
            }
        }
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

    View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
