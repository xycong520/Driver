package cn.jdywl.driver.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.MainActivity;
import cn.jdywl.driver.R;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.OrderStatus;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.OrderItem;
import cn.jdywl.driver.ui.driver.DDetailActivity;

/**
 * Created by wuwantao on 15/10/5.
 */
public class HomeGvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = LogHelper.makeLogTag(HomeGvAdapter.class);

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NAV = 1;
    private static final int TYPE_NAV_STAGE_HEADER = 5;
    private static final int TYPE_NAV_STAGE = 6;
    private static final int TYPE_MARKET_HEADER = 2;
    private static final int TYPE_MARKET = 3;
    private static final int TYPE_MARKET_FOOTER = 4;

    private Integer[] mTitleSet;
    private Integer[] mImgSet;
    private ArrayList<Integer> mStageSet;
    private ArrayList<Integer> mStageImgSet;
    private List<OrderItem> mDataSet;
    private int mTotalCount = 0;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgView;

        public HeaderViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            imgView = (ImageView) v.findViewById(R.id.iv_home_banner);
        }

        public ImageView getImgView() {
            return imgView;
        }
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class NavViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imgView;

        public NavViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.tv_home_tile);
            imgView = (ImageView) v.findViewById(R.id.iv_home_tile);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImgView() {
            return imgView;
        }
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class MarketHeaderViewHolder extends RecyclerView.ViewHolder {

        public MarketHeaderViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
        }

    }

    public static class NavStageHeaderViewHolder extends RecyclerView.ViewHolder {

        public NavStageHeaderViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                }
            });
        }

    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class MarketViewHolder extends RecyclerView.ViewHolder {

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

        public MarketViewHolder(View v) {
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
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class MarketFooterViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCount;

        public MarketFooterViewHolder(View v) {
            super(v);
            tvCount = (TextView) v.findViewById(R.id.tv_count);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public HomeGvAdapter(Integer[] titleSet, Integer[] imgSet, ArrayList<Integer> stageSet, ArrayList<Integer> stageImgSet, List<OrderItem> dataSet) {
        mTitleSet = titleSet;
        mImgSet = imgSet;
        mStageSet = stageSet;
        mStageImgSet = stageImgSet;
        mDataSet = dataSet;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setTotalCount(int count) {
        mTotalCount = count;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_NAV || viewType == TYPE_NAV_STAGE) {
            //inflate your layout and pass it to view holder
            // Create a new view.
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_home_nav, viewGroup, false);
            return new NavViewHolder(v);

        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.home_banner, viewGroup, false);
            return new HeaderViewHolder(v);

        } else if (viewType == TYPE_MARKET_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_market_header, viewGroup, false);
            return new MarketHeaderViewHolder(v);

        } else if (viewType == TYPE_NAV_STAGE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_nav_header, viewGroup, false);
            return new NavStageHeaderViewHolder(v);

        } else if (viewType == TYPE_MARKET) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_market, viewGroup, false);
            return new MarketViewHolder(v);
        } else if (viewType == TYPE_MARKET_FOOTER) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_market_footer, viewGroup, false);
            return new MarketFooterViewHolder(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        LogHelper.d(TAG, "Element " + position + " set.");

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });

            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                    return false;
                }
            });
        }

        if (viewHolder instanceof HeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
            HeaderViewHolder headerHolder = (HeaderViewHolder) viewHolder;
            headerHolder.getImgView().setImageResource(R.drawable.img_banner);

        } else if (viewHolder instanceof NavViewHolder) {

            //cast holder to VHItem and set data
            NavViewHolder navHolder = (NavViewHolder) viewHolder;
            if (position > MainActivity.MENU_COUNT + 1 && position <= MainActivity.MENU_COUNT + 1 + mStageSet.size()) {
                navHolder.getTextView().setText(mStageSet.get(position - 2 - MainActivity.MENU_COUNT));
                navHolder.getImgView().setImageResource(mStageImgSet.get(position - 2 - MainActivity.MENU_COUNT));
            } else {
                navHolder.getTextView().setText(mTitleSet[position - 1]);
                navHolder.getImgView().setImageResource(mImgSet[position - 1]);
            }
        } else if ((viewHolder instanceof NavStageHeaderViewHolder)) {
            //cast holder to VHHeader and set data for header.
            NavStageHeaderViewHolder vh = (NavStageHeaderViewHolder) viewHolder;
        } else if ((viewHolder instanceof MarketHeaderViewHolder)) {
            //cast holder to VHHeader and set data for header.
            MarketHeaderViewHolder vh = (MarketHeaderViewHolder) viewHolder;
        } else if (viewHolder instanceof MarketViewHolder) {
            MarketViewHolder vh = (MarketViewHolder) viewHolder;
            int pos = position - mTitleSet.length - mStageSet.size() - 3;
            if (mDataSet.size() == 0) {
                return;
            }
            final OrderItem data = mDataSet.get(pos);
            final Context context = vh.mView.getContext();

            // Define click listener for the ViewHolder's View.
            vh.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //LogHelper.d(TAG, "Element " + getPosition() + " clicked.");
                    Intent it = new Intent(context, DDetailActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("order", data);
                    it.putExtras(bundle);

                    context.startActivity(it);
                }
            });

            vh.tvRoute.setText(data.getOrigin() + " — " + data.getDestination());
            vh.tvSenddate.setText(data.getSendtime() + " 启运");
            vh.tvMarketprice.setText("市场运价: " + data.getDriverBill() + "元");

            int status = data.getStatus();
            //String[] sstatus = getContext().getResources().getStringArray(R.array.status);
            vh.tvStatus.setText(OrderStatus.getDesc(status));

            //散车业务不现实竞拍价
            if (data.getCarNum() < ApiConfig.CAR_SIZE) {
                vh.tvExpprice.setVisibility(View.GONE);

                //散车
                vh.tvOrderType.setBackgroundColor(context.getResources().getColor(R.color.bg_sanche));
                vh.tvOrderType.setText("散车");
            } else {
                vh.tvExpprice.setVisibility(View.VISIBLE);
                vh.tvExpprice.setText("期望运价: " + data.getExpectationPrice() + "元");

                //整版车
                vh.tvOrderType.setBackgroundColor(context.getResources().getColor(R.color.bg_zhengban));
                vh.tvOrderType.setText("整板");
            }

            vh.tvCarinfo.setText(Helper.getCarTypeByid(context, data.getBrand()) + " " + data.getCarNum() + "辆");
        } else if (viewHolder instanceof MarketFooterViewHolder) {
            //cast holder to VHHeader and set data for header.
            MarketFooterViewHolder vh = (MarketFooterViewHolder) viewHolder;

            if (mTotalCount > 0) {
                vh.tvCount.setVisibility(View.VISIBLE);
                vh.tvCount.setText(vh.tvCount.getContext().getString(R.string.total_count, mTotalCount));
            } else {
                vh.tvCount.setVisibility(View.GONE);
            }
        } else {
            throw new RuntimeException("未找到填充试图");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTitleSet.length + mStageSet.size() + mDataSet.size() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position >= 1 && position <= MainActivity.MENU_COUNT) {
            return TYPE_NAV;
        } else if (position == MainActivity.MENU_COUNT + 1) {
            return TYPE_NAV_STAGE_HEADER;
        } else if (position > MainActivity.MENU_COUNT + 1 && position <= MainActivity.MENU_COUNT + 1 + mStageSet.size()) {
            return TYPE_NAV_STAGE;
        } else if (position == MainActivity.MENU_COUNT + 1 + mStageSet.size() + 1) {
            return TYPE_MARKET_HEADER;
        } else if (position == (mDataSet.size() + MainActivity.MENU_COUNT + 1 + mStageSet.size() + 2)) {  //最后一个元素
            return TYPE_MARKET_FOOTER;
        } else {
            return TYPE_MARKET;
        }
    }
}
