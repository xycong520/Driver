package cn.jdywl.driver.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.app.VolleySingleton;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.config.AppConfig;
import cn.jdywl.driver.config.AppConst;
import cn.jdywl.driver.helper.Helper;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.Profile;
import cn.jdywl.driver.model.ResponseEntry;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.push.BdPushUtils;
import cn.jdywl.driver.ui.common.BaseActivity;


/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = LogHelper.makeLogTag(LoginActivity.class);

    public static final int LOGIN = 1;  // The request code

    @Bind(R.id.progress_login)
    ProgressBar progressLogin;
    @Bind(R.id.actv_phone)
    AutoCompleteTextView actvPhone;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.btn_loginCode)
    Button btnLoginCode;
    @Bind(R.id.btn_signin)
    Button btnSignin;
    @Bind(R.id.nsv_login_form)
    NestedScrollView nsvLoginForm;
    @Bind(R.id.rv_login)
    RelativeLayout rvLogin;
    @Bind(R.id.ll_register)
    LinearLayout llRegister;
    @Bind(R.id.et_name)
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupToolbar();

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == R.id.ime_action_signin || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //手机号码和验证码都有效时才使能登录按钮
                if (isPasswordValid(s.toString()) && Helper.isPhone(actvPhone.getText().toString())) {
                    btnSignin.setEnabled(true);
                } else {
                    btnSignin.setEnabled(false);
                }
            }
        });

        actvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //手机号码有效时才使能获取密码按钮
                if (Helper.isPhone(s.toString())) {
                    btnLoginCode.setEnabled(true);
                    if (isPasswordValid(etPassword.getText().toString())) {
                        btnSignin.setEnabled(true);
                    } else {
                        btnSignin.setEnabled(false);
                    }
                } else {
                    btnLoginCode.setEnabled(false);
                    btnSignin.setEnabled(false);
                }
            }
        });

        btnSignin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //login code button
        btnLoginCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogincode();
            }
        });
    }

    /**
     * 登出系统
     */
    public static void logout(Context context) {
        // 退出登录，删除用户认证信息
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(AppConst.KEY_PREF_AUTH_ISLOGIN);
        editor.remove(AppConst.KEY_PREF_AUTH_PHONE);
        editor.remove(AppConst.KEY_PREF_AUTH_PASSWORD);
        editor.remove(AppConst.KEY_PREF_AUTH_NAME);
        editor.remove(AppConst.KEY_PREF_AUTH_ROLES_MASTER);
        editor.remove(AppConst.KEY_PREF_AUTH_ROLES_DRIVER);
        editor.remove(AppConst.KEY_PREF_AUTH_LOGINTIME);
        editor.commit();//需要立即生效

        AppConfig.bLogin = false;
        AppConfig.phone = "";
        AppConfig.password = "";
        AppConfig.name = "";
        AppConfig.logintime = 0L;

        //退出用户登录后，解绑百度push
        BdPushUtils.unBindForApp(context);
    }

    public void getLogincode() {
        boolean cancel = false;
        String phone = actvPhone.getText().toString();
        // Check for a valid phone number.
        if (TextUtils.isEmpty(phone)) {
            actvPhone.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!Helper.isPhone(phone)) {
            actvPhone.setError(getString(R.string.error_invalid_phone));
            cancel = true;
        }

        if (cancel) {
            actvPhone.requestFocus();
            return;
        }

        btnLoginCode.setEnabled(false);
        //30秒后才能重新获取密码
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

                btnLoginCode.setText(String.format("剩余%d秒", millisUntilFinished / 1000));
            }

            public void onFinish() {
                btnLoginCode.setEnabled(true);
                btnLoginCode.setText("获取密码");
            }
        }.start();

        String url = ApiConfig.api_url + ApiConfig.LOGINCODE_URL + "&phone=" + phone;

        // Request a JSON response from the provided URL.
        GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.GET,
                url,
                ResponseEntry.class,
                null,
                new Response.Listener<ResponseEntry>() {
                    @Override
                    public void onResponse(ResponseEntry response) {
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }
                        if (response.getStatusCode() == 0) {
                            Toast.makeText(LoginActivity.this, "登录密码正在发送到您的手机", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "登录密码发送失败:" + response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "登录密码发送失败，请稍后再试"/* + error.getMessage()*/, Toast.LENGTH_SHORT).show();
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(myReq);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        //检查网络链接
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            // 网络无连接，display error
            Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reset errors.
        actvPhone.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String phone = actvPhone.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid phone number.
        if (TextUtils.isEmpty(phone)) {
            actvPhone.setError(getString(R.string.error_field_required));
            focusView = actvPhone;
            cancel = true;
        } else if (!Helper.isPhone(phone)) {
            actvPhone.setError(getString(R.string.error_invalid_phone));
            focusView = actvPhone;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_field_required));
            focusView = etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            String url = ApiConfig.api_url + ApiConfig.LOGIN_URL;

            //添加POST参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("phone", phone);
            params.put("password", password);
            params.put("user_type", ApiConfig.CAROWNER_TYPE);

            // Request a JSON response from the provided URL.
            GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.POST,
                    //"http://validate.jsontest.com/?json={'key':'value'}",
                    url,
                    ResponseEntry.class,
                    params,
                    new Response.Listener<ResponseEntry>() {
                        @Override
                        public void onResponse(ResponseEntry response) {
                            showProgress(false);

                            if (response == null) {
                                LogHelper.i(TAG, "response为空");
                                return;
                            }

                            if (response.getStatusCode() == 0) {


                                String name = response.getMessage();  // 登录成功时，message字段传递用户名
                                LogHelper.i(TAG, name);

                                //登录成功
                                AppConfig.bLogin = true;
                                AppConfig.phone = actvPhone.getText().toString();
                                AppConfig.password = etPassword.getText().toString();
                                AppConfig.name = name;
                                AppConfig.roles = response.getRoles();
                                AppConfig.logintime = System.currentTimeMillis() / 1000;//记录登录时间

                                // 存储认证信息，避免下次重新登录
                                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean(AppConst.KEY_PREF_AUTH_ISLOGIN, true);
                                editor.putString(AppConst.KEY_PREF_AUTH_PHONE, AppConfig.phone);
                                editor.putString(AppConst.KEY_PREF_AUTH_PASSWORD, AppConfig.password);
                                editor.putString(AppConst.KEY_PREF_AUTH_NAME, name);
                                editor.putLong(AppConst.KEY_PREF_AUTH_LOGINTIME, AppConfig.logintime);
                                //TODO:没有处理角色减少的情况
                                if (response.getRoles() != null) {
                                    for (String roles : response.getRoles()) {
                                        if ("station_driver".equals(roles)) {
                                            editor.putString(AppConst.KEY_PREF_AUTH_ROLES_DRIVER, roles);
                                        } else if ("station_master".equals(roles)) {
                                            editor.putString(AppConst.KEY_PREF_AUTH_ROLES_MASTER, roles);
                                        }
                                    }
                                }
                                editor.commit(); //需要立即生效

                                if (name.length() <= 0) {
                                    //登录成功，但无姓名
                                    rvLogin.setVisibility(View.INVISIBLE);
                                    llRegister.setVisibility(View.VISIBLE);

                                    setToolbarTitle("完善个人资料");

                                } else {
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                    backToMain();
                                }

                            } else {
                                etPassword.setError(getString(R.string.error_incorrect_password));
                                etPassword.requestFocus();
                                Toast.makeText(LoginActivity.this, "登录失败:" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showProgress(false);
                            Toast.makeText(LoginActivity.this, "登录失败, 请检查手机号和密码是否正确"/* + error.getMessage()*/, Toast.LENGTH_SHORT).show();
                        }
                    });

            //设置TAG，用于取消请求
            myReq.setTag(TAG);

            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        }
    }

    private boolean isPasswordValid(String password) {
        //验证码的长度固定为6
        return password.length() == 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            nsvLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
            nsvLoginForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    nsvLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressLogin.setVisibility(show ? View.VISIBLE : View.GONE);
            progressLogin.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressLogin.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressLogin.setVisibility(show ? View.VISIBLE : View.GONE);
            nsvLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 更新个人资料
     *
     * @param view
     */
    public void onUpdateProfile(View view) {
        String name = etName.getText().toString();

        if (name.length() < 2) {
            etName.setError(getString(R.string.error_invalid_name));
            etName.requestFocus();
            return;
        }

        showProgress(true);
        updateProfile(name);
    }

    private void updateProfile(String name) {
        String url = ApiConfig.api_url + ApiConfig.CAROWNER_UPDATE_PROFILE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);

        GsonRequest<Profile> myReq = new GsonRequest<Profile>(Request.Method.PUT,
                url,
                Profile.class,
                params,
                new Response.Listener<Profile>() {
                    @Override
                    public void onResponse(Profile response) {

                        showProgress(false);
                        //mInError = false;
                        if (response == null) {
                            LogHelper.i(TAG, "response为空");
                            return;
                        }

                        Profile profile = response;

                        //登录成功,且姓名有效
                        AppConfig.name = profile.getName();

                        // 存储认证信息，避免下次重新登录
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(AppConst.KEY_PREF_AUTH_NAME, AppConfig.name);
                        editor.commit(); //需要立即生效

                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                        //登录成功，返回
                        backToMain();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        Helper.processVolleyErrorMsg(LoginActivity.this, error);
                    }
                });

        //设置TAG，用于取消请求
        myReq.setTag(TAG);

        VolleySingleton.getInstance(this).addToRequestQueue(myReq);
    }

    private void backToMain() {
        /*
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setAction(MainActivity.ACTION_LOGIN_OK);
        startActivity(intent);
        */

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();//关闭Activity
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }

}



