/**
 * Copyright 2013 Ognyan Bankov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.jdywl.driver.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.model.OrderItem;


public class OrderHistoryAdapter extends ArrayAdapter<OrderItem> {

    public OrderHistoryAdapter(Context context,
                               int textViewResourceId,
                               List<OrderItem> objects
    ) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_order_history, null);
        }

        ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);

        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(R.id.id_holder, holder);
        }

        OrderItem order = getItem(position);

        holder.tv_route.setText(order.getOrigin() + "---->" + order.getDestination());
        holder.tv_orderno.setText("运单号："+order.getOrderNo());
        holder.tv_carinfo.setText(Helper.getCarTypeByid(this.getContext(), order.getBrand()) + " " + order.getCarNum() + "辆");
        holder.tv_marketprice.setText("市场运价: " + order.getDriverBill());
        holder.tv_bill.setText("成交运价: " + order.getBill());

        return v;
    }


    private class ViewHolder {
        TextView tv_route;
        TextView tv_orderno;
        TextView tv_carinfo;
        TextView tv_marketprice;
        TextView tv_bill;


        public ViewHolder(View v) {
            tv_route = (TextView) v.findViewById(R.id.tv_route);
            tv_orderno = (TextView) v.findViewById(R.id.tv_orderno);
            tv_carinfo = (TextView) v.findViewById(R.id.tv_carinfo);
            tv_marketprice = (TextView) v.findViewById(R.id.tv_marketprice);
            tv_bill = (TextView) v.findViewById(R.id.tv_bill);

            v.setTag(this);
        }
    }
}
