package cn.jdywl.driver.adapter.stage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.model.ServiceItem;

/**
 * Created by Administrator on 2016/5/12.
 */
public class StageServerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ServiceItem> services;
    ImageLoader mImageLoader;

    public StageServerAdapter(List<ServiceItem> services) {
        this.services = services;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_server, parent, false);
        mImageLoader = VolleySingleton.getInstance(parent.getContext()).getImageLoader();
        return new NavViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NavViewHolder navHolder = (NavViewHolder) holder;
        navHolder.getTextView().setText(services.get(position).getName());
        navHolder.getImgView().setImageUrl(services.get(position).getIcon().getUrl(), mImageLoader);
        // 如果设置了回调，则设置点击事件
        if (onClickListener != null) {
            holder.itemView.setTag(services.get(position).getName());
            holder.itemView.setOnClickListener(onClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    View.OnClickListener onClickListener;

    public void setOnItemClickLitener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public static class NavViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final NetworkImageView imgView;

        public NavViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            textView = (TextView) v.findViewById(R.id.tv_home_tile);
            imgView = (NetworkImageView) v.findViewById(R.id.iv_home_tile);
        }

        public TextView getTextView() {
            return textView;
        }

        public NetworkImageView getImgView() {
            return imgView;
        }
    }
}
