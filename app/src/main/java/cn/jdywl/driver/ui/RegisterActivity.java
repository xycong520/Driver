package cn.jdywl.driver.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
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
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.ResponseEntry;
import cn.jdywl.driver.network.GsonRequest;
import cn.jdywl.driver.ui.common.BaseActivity;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = LogHelper.makeLogTag(RegisterActivity.class);
    @Bind(R.id.register_progress)
    ProgressBar registerProgress;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.sv_register_form)
    ScrollView svRegisterForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister() {
        //检查网络链接
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnected())) {
            // 网络无连接，display error
            Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
        }

        // Reset errors.
        etName.setError(null);

        // Store values at the time of the login attempt.
        String name = etName.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name .
        if (TextUtils.isEmpty(name)) {
            etName.setError(getString(R.string.error_field_required));
            focusView = etName;
            cancel = true;
        } else if (!isNameValid(name)) {
            etName.setError(getString(R.string.error_invalid_name));
            focusView = etName;
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

            String url = ApiConfig.api_url + ApiConfig.REGISTER_URL;

            //添加POST参数
            Map<String, String> params = new HashMap<String, String>();
            params.put("name", name);

            // Request a JSON response from the provided URL.
            GsonRequest<ResponseEntry> myReq = new GsonRequest<ResponseEntry>(Request.Method.POST,
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
                                Toast.makeText(RegisterActivity.this, "姓名更新成功", Toast.LENGTH_SHORT).show();
                                RegisterActivity.this.finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "姓名更新失败:" + response.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showProgress(false);
                            Toast.makeText(RegisterActivity.this, "姓名更新失败-" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            //设置TAG，用于取消请求
            myReq.setTag(TAG);

            VolleySingleton.getInstance(this).addToRequestQueue(myReq);
        }
    }

    private boolean isNameValid(String name) {

        return name.length() >= 2;
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

            svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
            svRegisterForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            registerProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            registerProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            svRegisterForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void setRefreshEnabled(boolean enabled) {

    }

    public void cancelVolleyRequest(RequestQueue queue) {
        queue.cancelAll(TAG);
    }
}
