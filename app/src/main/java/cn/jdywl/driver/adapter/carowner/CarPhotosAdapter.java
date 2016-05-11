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
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.helper.LogHelper;

/**
 * Provide views to RecyclerView with data from mList.
 */
public class CarPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = CarPhotosAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_PHOTO = 0;

    final private List<String> mList;

    private OnItemClickListener mItemListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public final class HeaderViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_vin)
        TextView tvVin;

        HeaderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public final class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_photo)
        NetworkImageView ivPhoto;

        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * Initialize the mList of the Adapter.
     */
    public CarPhotosAdapter(List<String> list) {
        mList = list;
    }

    //设置Item点击监听器
    public void setOnItemClickListener(@Nullable OnItemClickListener onItemClickListener) {
        this.mItemListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_PHOTO) {
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_carphoto, viewGroup, false);

            return new PhotoViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_header, viewGroup, false);

            return new HeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        LogHelper.d(TAG, "Element " + position + " set.");

        if (viewHolder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) viewHolder;
            headerHolder.tvVin.setText(String.format(Locale.CHINA, "车架号: %s", mList.get(position)));
        } else {
            PhotoViewHolder photoHolder = (PhotoViewHolder) viewHolder;

            //加载照片
            String url = mList.get(position);
            ImageLoader mImageLoader = VolleySingleton.getInstance(photoHolder.ivPhoto.getContext()).getImageLoader();
            photoHolder.ivPhoto.setImageUrl(url, mImageLoader);

            // 如果设置了回调，则设置点击事件
            if (mItemListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemListener.onItemClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).startsWith("http:")) {
            return TYPE_PHOTO;
        } else {
            return TYPE_HEADER;
        }
    }
}
