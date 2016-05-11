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

package cn.jdywl.driver.adapter.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.model.HelptelItem;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class HelptelAdapter extends RecyclerView.Adapter<HelptelAdapter.ViewHolder> {
    private static final String TAG = "HelptelAdapter";

    private List<HelptelItem> mDataSet;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_helptel1;
        private final TextView tv_helptel2;
        private final TextView tv_trailertel;
        private final TextView tv_city;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            tv_helptel1 = (TextView) v.findViewById(R.id.tv_helptel1);
            tv_helptel2 = (TextView) v.findViewById(R.id.tv_helptel2);
            tv_trailertel = (TextView) v.findViewById(R.id.tv_trailertel);
            tv_city = (TextView) v.findViewById(R.id.tv_city);
        }

        public TextView getHelptel1() {
            return tv_helptel1;
        }

        public TextView getHelptel2() {
            return tv_helptel2;
        }

        public TextView getTrailertel() {
            return tv_trailertel;
        }
        public TextView getCity() {
            return tv_city;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public HelptelAdapter(List<HelptelItem> dataSet) {
        mDataSet = dataSet;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_helptel, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //LogHelper.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getHelptel1().setText(mDataSet.get(position).getHelpTel1());
        viewHolder.getHelptel2().setText(mDataSet.get(position).getHelpTel2());
        viewHolder.getTrailertel().setText(mDataSet.get(position).getTrailerTel());
        viewHolder.getCity().setText(mDataSet.get(position).getCity());
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
