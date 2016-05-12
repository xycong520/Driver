package cn.jdywl.driver.ui.stage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.adapter.stage.StageServerAdapter;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.ServiceItem;
import cn.jdywl.driver.network.MyJsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * Created by Administrator on 2016/5/12.
 */
public class StageInfoActivity extends BaseActivity {
    //设置tag，用于在activity stop时取消Volley的请求
    public static final String TAG = LogHelper.makeLogTag(StageInfoActivity.class);
    public static final String ID = "id";
    public static final String PAC = "PAC";
    public static final String ADDRESS = "address";
    public static final String STAGENAME = "stageName";
    public static final String CENTER = "center";
    public static final String MASTER = "master";
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvProvinceAndCity)
    TextView tvProvinceAndCity;
    @Bind(R.id.tvAddress)
    TextView tvAddress;
    @Bind(R.id.tvCenter)
    TextView tvCenter;
    @Bind(R.id.tvMaster)
    TextView tvMaster;
    @Bind(R.id.rv_server)
    RecyclerView mRecyclerView;
    StageServerAdapter mAdapter;
    List<ServiceItem> mData = new ArrayList<>();
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stageinfo);
        ButterKnife.bind(this);
        setupToolbar();
        setupRecyclerView(mRecyclerView);
        id = getIntent().getIntExtra(ID, -1);
        init();
    }

    private void init() {
        tvAddress.setText(getIntent().getStringExtra(ADDRESS));
        tvProvinceAndCity.setText(getIntent().getStringExtra(PAC));
        tvMaster.setText(getIntent().getStringExtra(MASTER));
        tvCenter.setText(getIntent().getStringExtra(CENTER));
        tvName.setText(getIntent().getStringExtra(STAGENAME));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadServer();
    }


    private void loadServer() {
        AppConst.showDialog(this);
        String position = ApiConfig.STAGE_SERVICE_URL.replace("/", "/" + id + "/");
        String url = ApiConfig.api_url + position;
        MyJsonRequest myReq = new MyJsonRequest(Request.Method.GET,
                url,
                "",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AppConst.dismiss();
                        mData.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                mData.add(new Gson().fromJson(response.getJSONObject(i).toString(), ServiceItem.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 返回错误也要取消refresh
                AppConst.dismiss();
                Helper.processVolleyErrorMsg(StageInfoActivity.this, error);
            }
        });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    @Override
    protected void setRefreshEnabled(boolean enabled) {

    }

    @Override
    protected void cancelVolleyRequest(RequestQueue queue) {

    }

    void setupRecyclerView(RecyclerView rv) {
        //布局为GridView
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 4);
        ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });

        rv.setLayoutManager(mLayoutManager);

        mAdapter = new StageServerAdapter(mData);
        rv.setAdapter(mAdapter);

        rv.setHasFixedSize(true);
        //rv.addItemDecoration(new DividerGridItemDecoration(this));

        /*
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.home_tile_space);
        rv.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        */

        mAdapter.setOnItemClickLitener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name  = (String) v.getTag();
                if ("小板速运".equals(name)){
                    startActivity(new Intent(StageInfoActivity.this,AddOrderActivity.class));
                }
            }
        });
    }
}
