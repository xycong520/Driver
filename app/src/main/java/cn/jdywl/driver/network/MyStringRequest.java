package cn.jdywl.driver.network;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cn.jdywl.driver.config.AppConfig;

/**
 * Created by xycong on 2016/5/8.
 */
public class MyStringRequest extends StringRequest {

    private Map<String, String> headers = new HashMap<String, String>();
    private final Map<String, String> params;

    public MyStringRequest(int method, String url,
                           Map<String, String> params,
                           Listener<String> listener, ErrorListener errorListener) {
        super(method, url, listener,
                errorListener);
        this.params = params;
        //设置basic auth header
        setBasicAuth();
        setApiVersion();
    }


    private void setBasicAuth() {
        if (AppConfig.bLogin && (!AppConfig.phone.isEmpty()) && (!AppConfig.password.isEmpty())) {
            String loginEncoded = new String(Base64.encode((AppConfig.phone + ":" + AppConfig.password).getBytes(), Base64.NO_WRAP));
            this.headers.put("Authorization", "Basic " + loginEncoded);
            this.headers.put("Accept", "application/vnd.jindouyun.v1+json");
        }
    }

    private void setApiVersion() {

        this.headers.put("Accept", "application/vnd.jindouyun.v1+json");

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }
}
