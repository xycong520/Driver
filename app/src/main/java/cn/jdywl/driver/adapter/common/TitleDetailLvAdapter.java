/**
 * Copyright 2013 Ognyan Bankov
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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.jdywl.driver.R;
import cn.jdywl.driver.model.TitleDetailItem;


public class TitleDetailLvAdapter extends ArrayAdapter<TitleDetailItem> {

    public TitleDetailLvAdapter(Context context,
                                int textViewResourceId,
                                List<TitleDetailItem> objects
    ) {
        super(context, textViewResourceId, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_title_detail, null);
        }
        
        ViewHolder holder = (ViewHolder) v.getTag(R.id.id_holder);       
        
        if (holder == null) {
            holder = new ViewHolder(v);
            v.setTag(R.id.id_holder, holder);
        }

        TitleDetailItem entry = getItem(position);

        holder.tv_title.setText(entry.getTitle());
        holder.tv_detail.setText(entry.getDetail());

        return v;
    }
    
    
    private class ViewHolder {
        TextView tv_title;
        TextView tv_detail;

        public ViewHolder(View v) {
            tv_title = (TextView) v.findViewById(R.id.tv_title);
            tv_detail = (TextView) v.findViewById(R.id.tv_detail);
            
            v.setTag(this);
        }
    }
}
