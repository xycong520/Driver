package cn.jdywl.driver.ui.carowner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.common.CityAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.CityItem;
import cn.jdywl.driver.model.CityPage;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public class CityActivity extends BaseActivity {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(CityActivity.class);

    @Bind(R.id.listview)
    ListView listview;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_filter)
    EditText etFilter;

    private CityPage mCityPage = new CityPage();
    CityAdapter adapter;
    boolean bOrigin;
    boolean isExpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        setupToolbar();

        Intent it = getIntent();
        bOrigin = it.getBooleanExtra(AddOrderFragment.B_ORIGIN, true);
        isExpress = it.getBooleanExtra("isExpress", false);
        if (bOrigin) {
            setToolbarTitle("选择始发地");
            adapter = new CityAdapter(this,
                    R.layout.simple_dropdown_item, (ArrayList<CityItem>) mCityPage.getCityItems(), CityAdapter.ORIGIN_TYPE);
            loadOrigins();
        } else {
            setToolbarTitle("选择目的地");
            adapter = new CityAdapter(this,
                    R.layout.simple_dropdown_item, (ArrayList<CityItem>) mCityPage.getCityItems(), CityAdapter.DESTINATION_TYPE);
            String origin = it.getStringExtra(AddOrderFragment.ORIGIN_CITY);
            loadDestinations(origin);
        }


        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city;
                if (bOrigin) {
                    city = mCityPage.getCityItems().get(position).getOrigin();
                } else {
                    city = mCityPage.getCityItems().get(position).getDestination();
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", city);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        //对城市列表进行筛选
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // When user changed the Text
                CityActivity.this.adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void loadOrigins() {
        String url = ApiConfig.api_url + ApiConfig.ORIGIN_URL;
        if (isExpress) {
            url = ApiConfig.api_url + ApiConfig.STAGE_ORIGINS_URL;
            MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                    url,
                    "",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response == null) {
                                LogHelper.i(TAG, "response为空");
                                return;
                            }
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    mCityPage.getCityItems().add(new Gson().fromJson(response.getJSONObject(i).toString(), CityItem.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //获取分页信息
//                            mCityPage.getCityItems().addAll(response.getCityItems());

                            //
                            adapter.setItemAll((ArrayList<CityItem>) mCityPage.getCityItems());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Helper.processVolleyErrorMsg(CityActivity.this, error);
                        }
                    });
            myReq.setTag(TAG);
            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        } else {
            GsonRequest<CityPage> myReq = new GsonRequest<CityPage>(Request.Method.GET,
                    url,
                    CityPage.class,
                    null,
                    new Response.Listener<CityPage>() {
                        @Override
                        public void onResponse(CityPage response) {
                            if (response == null) {
                                LogHelper.i(TAG, "response为空");
                                return;
                            }
                            //获取分页信息
                            mCityPage.getCityItems().addAll(response.getCityItems());

                            //
                            adapter.setItemAll((ArrayList<CityItem>) response.getCityItems());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Helper.processVolleyErrorMsg(CityActivity.this, error);
                        }
                    });
            myReq.setTag(TAG);
            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        }


    }

    //根据始发地加载目的地
    private void loadDestinations(final String origin) {
        String url = ApiConfig.api_url + ApiConfig.DESTINATION_URL +
                "&origin=" + URLEncoder.encode(origin);
        if (isExpress) {
            url = ApiConfig.api_url + ApiConfig.STAGE_DESTINATIONS_URL +
                    "&origin=" + URLEncoder.encode(origin);
            MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                    url,
                    "",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response == null) {
                                LogHelper.i(TAG, "response为空");
                                return;
                            }
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    mCityPage.getCityItems().add(new Gson().fromJson(response.getJSONObject(i).toString(), CityItem.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //获取分页信息
//                            mCityPage.getCityItems().addAll(response.getCityItems());

                            //
                            adapter.setItemAll((ArrayList<CityItem>) mCityPage.getCityItems());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Helper.processVolleyErrorMsg(CityActivity.this, error);
                        }
                    });
            myReq.setTag(TAG);
            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        } else {
            GsonRequest<CityPage> myReq = new GsonRequest<CityPage>(Request.Method.GET,
                    url,
                    CityPage.class,
                    null,
                    new Response.Listener<CityPage>() {
                        @Override
                        public void onResponse(CityPage response) {
                            if (response == null) {
                                LogHelper.i(TAG, "response为空");
                                return;
                            }
                            //获取分页信息
                            mCityPage.getCityItems().addAll(response.getCityItems());

                            //
                            adapter.setItemAll((ArrayList<CityItem>) response.getCityItems());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Helper.processVolleyErrorMsg(CityActivity.this, error);
                        }
                    });

            myReq.setTag(TAG);

            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        }

    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}
