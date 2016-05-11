package cn.jdywl.driver.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jdywl.driver.R;
import cn.jdywl.driver.model.CityItem;

/**
 * Created by wuwantao on 15/5/3.
 */
public class CityAdapter extends ArrayAdapter<CityItem> {

    public static int ORIGIN_TYPE = 0;
    public static int DESTINATION_TYPE = 1;

    private final String MY_DEBUG_TAG = "CityAdapter";
    private ArrayList<CityItem> items;
    private ArrayList<CityItem> itemsAll;
    private ArrayList<CityItem> suggestions;
    private int viewResourceId;
    private int cityType;   //0:表示始发地，1:表示目的地

    public CityAdapter(Context context, int viewResourceId, ArrayList<CityItem> items, int type) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<CityItem>) items.clone();
        this.suggestions = new ArrayList<CityItem>();
        this.viewResourceId = viewResourceId;
        this.cityType = type;
    }

    //设置itemall
    public void setItemAll(ArrayList<CityItem> items)
    {
        this.itemsAll = (ArrayList<CityItem>) items.clone();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        CityItem city = items.get(position);
        if (city != null) {
            TextView cityLabel = (TextView) v.findViewById(R.id.text1);
            if (cityLabel != null) {
//              LogHelper.i(MY_DEBUG_TAG, "getView CityItem Name:"+city.getName());
                if (cityType == ORIGIN_TYPE) {
                    cityLabel.setText(city.getOrigin());
                } else {
                    cityLabel.setText(city.getDestination());
                }
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            if (cityType == ORIGIN_TYPE) {
                return ((CityItem) (resultValue)).getOrigin();
            } else {
                return ((CityItem) (resultValue)).getDestination();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                 for (CityItem city : itemsAll) {
                    String citystr;
                    if (cityType == ORIGIN_TYPE) {
                        citystr = city.getOrigin();
                    } else {
                        citystr = city.getDestination();
                    }
                    if (city.getPinyin().toLowerCase().startsWith(constraint.toString().toLowerCase())
                            || city.getAbb().toLowerCase().startsWith(constraint.toString().toLowerCase())
                            || citystr.startsWith(constraint.toString().toLowerCase())
                            || city.getLetter().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(city);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                //没有筛选是返回所有列表
                FilterResults filterResults = new FilterResults();
                filterResults.values = itemsAll;
                filterResults.count = itemsAll.size();
                return filterResults;
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<CityItem> filteredList = (ArrayList<CityItem>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CityItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}