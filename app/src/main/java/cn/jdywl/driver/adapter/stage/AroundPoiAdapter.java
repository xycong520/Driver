package cn.jdywl.driver.adapter.stage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.HashMap;
import java.util.List;

import cn.jdywl.driver.R;

public class AroundPoiAdapter extends BaseAdapter {
	private Context mContext;
	private List<PoiInfo> mkPoiInfoList;
	private int selected = -1;
	HashMap<Integer, Boolean> mChosenItem = new HashMap<Integer, Boolean>();

	public AroundPoiAdapter(Context context, List<PoiInfo> list) {
		this.mContext = context;
		this.mkPoiInfoList = list;
	}

	@Override
	public int getCount() {
		return mkPoiInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mkPoiInfoList != null) {
			return mkPoiInfoList.get(position);
		}
		return null;
	}

	public void setNewList(List<PoiInfo> list, int index) {
		this.mkPoiInfoList = list;
		this.selected = index;
		this.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class RecordHolder {
		public RelativeLayout rlMLPIItem;
		public ImageView ivMLISelected;
		public TextView tvMLIPoiName, tvMLIPoiAddress;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		RecordHolder holder = null;
		if (convertView == null) {
			holder = new RecordHolder();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(
					R.layout.item_mapview_location_poi_lv, null);
			holder.ivMLISelected = (ImageView) convertView
					.findViewById(R.id.ivMLISelected);
			holder.tvMLIPoiName = (TextView) convertView
					.findViewById(R.id.tvMLIPoiName);
			holder.tvMLIPoiAddress = (TextView) convertView
					.findViewById(R.id.tvMLIPoiAddress);
			holder.rlMLPIItem = (RelativeLayout) convertView
					.findViewById(R.id.rlMLPIItem);
			// View findViewById = convertView.findViewById(R.id.rlMLPIItem);
			convertView.setTag(holder);
		} else {
			holder = (RecordHolder) convertView.getTag();
		}
		holder.tvMLIPoiName.setText(mkPoiInfoList.get(position).name);
		holder.tvMLIPoiAddress.setText(mkPoiInfoList.get(position).address);
		Boolean state = mChosenItem.get(position);
		if (state != null && state) {
			holder.ivMLISelected.setImageResource(R.drawable.icon_checkbox_locationlist);
		} else {
			holder.ivMLISelected.setImageResource(R.drawable.icon_checkbox_planlist_u);
		}
		holder.rlMLPIItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mkPoiInfoList.size(); i++) {
					mChosenItem.put(i, false);
				}
				mChosenItem.put(position, true);
				 AroundPoiAdapter.this.notifyDataSetChanged();
				// Intent intent = new Intent();
				// intent.setAction("select_address_changed");
				// mContext.sendBroadcast(intent);
			}
		});
		// if (selected >= 0 && selected == position) {
		// holder.rlMLPIItem.setSelected(true);
		// } else {
		// holder.rlMLPIItem.setSelected(false);
		// }
		return convertView;
	}
}
