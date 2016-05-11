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

package cn.jdywl.driver.network;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.model.Stage;


public class GsonRequest<T> extends Request<T> {
    private final Gson mGson;
    private final Class<T> mClazz;
    private final Listener<T> mListener;
    private Map<String, String> headers = new HashMap<String, String>();

    private final Map<String, String> params;
    private boolean debug;

    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> params,
                       Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        if (url.equals(ApiConfig.api_url + ApiConfig.STAGE_OFMASTER_URL)) {
            debug = true;
        }
        this.mClazz = clazz;
        this.params = params;
        this.mListener = listener;
        mGson = new Gson();

        //设置basic auth header
        setBasicAuth();
        setApiVersion();
    }
    public GsonRequest(int method,
                       String url,
                       Map<String, String> params,
                       Listener<T> listener,
                       ErrorListener errorListener) {
        super(method, url, errorListener);
        if (url.equals(ApiConfig.api_url + ApiConfig.STAGE_OFMASTER_URL)) {
            debug = true;
        }
        Type type = new TypeToken<ArrayList<Stage>>() {}.getType();
        mClazz = (Class<T>) Type.class;
        this.params = params;
        this.mListener = listener;
        mGson = new Gson();

        //设置basic auth header
        setBasicAuth();
        setApiVersion();
    }

    public GsonRequest(int method,
                       String url,
                       Class<T> clazz,
                       Map<String, String> params,
                       Listener<T> listener,
                       ErrorListener errorListener,
                       Gson gson) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.params = params;
        this.mListener = listener;
        mGson = gson;

        //设置basic auth header
        setBasicAuth();
        setApiVersion();
    }


    private void setBasicAuth() {
        if (AppConfig.bLogin && (!AppConfig.phone.isEmpty()) && (!AppConfig.password.isEmpty())) {
            String loginEncoded = new String(Base64.encode((AppConfig.phone + ":" + AppConfig.password).getBytes(), Base64.NO_WRAP));
            this.headers.put("Authorization", "Basic " + loginEncoded);
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

        return params != null ? params : super.getParams();
    }

    public void setHeader(String title, String content) {
        headers.put(title, content);
    }


    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
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
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = "";
            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//            if (debug) {
//                Type type = new TypeToken<ArrayList<Stage>>() {}.getType();
//                json =
//                        "[{\"id\": 1,\"province\": \"\\u9655\\u897f\",\"city\":\"\\u897f\\u5b89\",\"operation_center\":\"\\u897f\\u5b89\\u8fd0\\u8425\\u4e2d\\u5fc3\",\"station\":\"\\u897f\\u5b89\\u6bd4\\u4e9a\\u8fea\\u5357\\u95e8\\u8f66\\u9a7f\\u7ad9\",\"position\":\"\\u9655\\u897f\\u7701\\u897f\\u5b89\\u5e02\\u957f\\u5b89\\u533a\\u4e0a\\u6797\\u82d1\\u4e00\\u8def\",\"master\":\"\\u738b\\u521a\",\"phone\":\"18626468980\",\"longitude\": 2.2,\"latitude\": 3.3,\"address\":\"\\u6c5f\\u82cf\\u5357\\\u4eac\",\"gathertime\":\"0000-00-00 00:00:00\"}]";
//                Log.i("json", ascii2native(json));
//                return (Response<T>) Response.success(mGson.fromJson(json, type),HttpHeaderParser.parseCacheHeaders(response));
//            }
            Log.i("json", ascii2native(json));
            return Response.success(mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}