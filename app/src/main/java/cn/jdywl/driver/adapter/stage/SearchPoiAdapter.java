package cn.jdywl.driver.adapter.stage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.ui.stage.BeanLocation;

/**
 * 类说明：
 *
 * @author Liucd
 * @version 1.0
 * @date 2014-10-30
 */
public class SearchPoiAdapter extends BaseAdapter {
    private Context mContext;
    private List<BeanLocation> cityPoiList;

    public SearchPoiAdapter(Context context, List<BeanLocation> list) {
        this.mContext = context;
        this.cityPoiList = list;
    }

    @Override
    public int getCount() {
        if (cityPoiList != null) {
            return cityPoiList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (cityPoiList != null) {
            return cityPoiList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class CityPoiHolder {
        public TextView tvMLIPoiName, tvMLIPoiAddress;
    }

    private CityPoiHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new CityPoiHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(
                    R.layout.item_mapview_location_poi_lv, null);
            holder.tvMLIPoiName = (TextView) convertView
                    .findViewById(R.id.tvMLIPoiName);
            holder.tvMLIPoiAddress = (TextView) convertView
                    .findViewById(R.id.tvMLIPoiAddress);
            convertView.setTag(holder);
        } else {
            holder = (CityPoiHolder) convertView.getTag();
        }
        BeanLocation cityPoi = cityPoiList.get(position);
        holder.tvMLIPoiName.setText(cityPoi.getLocName());
        holder.tvMLIPoiAddress.setText(cityPoi.getAddStr());
        return convertView;
    }
}
