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
public class MyJsonRequest extends JsonArrayRequest {

    private Map<String, String> headers = new HashMap<String, String>();
    private final Map<String, String> params;

    public MyJsonRequest(int method, String url, String requestBody,
                         Map<String, String> params,
                         Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, requestBody, listener,
                errorListener);
        this.params = params;
        //设置basic auth header
        setBasicAuth();
        setApiVersion();
    }

    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
     *                      indicates no parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public MyJsonRequest(int method, String url, Map<String, String> params, JSONArray jsonRequest,
                         Listener<JSONArray> listener, ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
        //设置basic auth header
        this.params = params;
        setBasicAuth();
        setApiVersion();
    }

    public MyJsonRequest(String url, Map<String, String> params, JSONArray jsonRequest, Listener<JSONArray> listener,
                         ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, params, jsonRequest,
                listener, errorListener);
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


    private String ascii2native(String asciicode) {
        String[] asciis = asciicode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciicode;
        }
        return nativeValue;
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {

            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            Log.i("json", ascii2native(jsonString));
            JSONArray array = new JSONArray(jsonString);
            return Response.success(array,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            je.printStackTrace();
            System.out.println(je.getMessage());
            return Response.error(new ParseError(je));
        }
    }
}
