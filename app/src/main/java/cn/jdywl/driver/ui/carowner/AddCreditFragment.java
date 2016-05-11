package cn.jdywl.driver.ui.carowner;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jdywl.driver.R;
import cn.jdywl.driver.config.ApiConfig;
import cn.jdywl.driver.helper.LogHelper;
import cn.jdywl.driver.model.CreditCompanyItem;
import cn.jdywl.driver.ui.common.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCreditFragment extends Fragment {
    private static String TAG = LogHelper.makeLogTag(AddCreditFragment.class);

    private static final String ARG_CARPRICE = "carPrice";

    public static final int SELECT_CREDIT_COMPANY = 0;
    private CreditCompanyItem company = null;

    @Bind(R.id.et_quota)
    EditText etQuota;
    @Bind(R.id.et_company)
    EditText etCompany;
    @Bind(R.id.et_creditBank)
    EditText etCreditBank;
    @Bind(R.id.et_creditName)
    EditText etCreditName;
    @Bind(R.id.et_creditNo)
    EditText etCreditNo;
    @Bind(R.id.et_creditMgr)
    EditText etCreditMgr;
    @Bind(R.id.tv_credit)
    TextView tvCredit;
    @Bind(R.id.cb_credit)
    CheckBox cbCredit;
    @Bind(R.id.ll_creditBank)
    LinearLayout llCreditBank;
    @Bind(R.id.ll_creditName)
    LinearLayout llCreditName;
    @Bind(R.id.ll_creditNo)
    LinearLayout llCreditNo;
    @Bind(R.id.ll_creditMgr)
    LinearLayout llCreditMgr;

    private int carPrice;

    private OnCreditDestroyListener mListener;

    public AddCreditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param carPrice Parameter 1.
     * @return A new instance of fragment AddOrderFragment.
     */
    public static AddCreditFragment newInstance(int carPrice) {
        AddCreditFragment fragment = new AddCreditFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CARPRICE, carPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            carPrice = getArguments().getInt(ARG_CARPRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_credit, container, false);
        ButterKnife.bind(this, view);

        tvCredit.setText(Html.fromHtml(getString(R.string.credit_agreement)));
        //tvCredit.setMovementMethod(LinkMovementMethod.getInstance());
        tvCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                WebView wv = new WebView(getActivity());
                wv.loadUrl(ApiConfig.api_url + ApiConfig.WEB_SERVICE_AGREEMENT);
                wv.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);

                        return true;
                    }
                });
                alert.setView(wv);

                alert.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cbCredit.setChecked(true);
                    }
                });
                alert.show();
            }
        });

        etCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getActivity(), CreditCompanyActivity.class);
                startActivityForResult(it, SELECT_CREDIT_COMPANY);
            }
        });

        etQuota.setHint(String.format("最多为总价(%d)的70%%", carPrice));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == SELECT_CREDIT_COMPANY) {
            // Make sure the request was successful
            if (resultCode == BaseActivity.RESULT_OK) {
                company = data.getParcelableExtra("company");
                llCreditBank.setVisibility(View.VISIBLE);
                //llCreditMgr.setVisibility(View.VISIBLE);
                llCreditName.setVisibility(View.VISIBLE);
                llCreditNo.setVisibility(View.VISIBLE);
                etCompany.setText(company.getCompany());
                etCreditBank.setText(company.getBank());
                etCreditName.setText(company.getAccountName());
                etCreditNo.setText(company.getAccountNo());
                etCreditMgr.setText(company.getContacter() + ":" + company.getPhone());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditDestroyListener) {
            mListener = (OnCreditDestroyListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreditDestroyListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

        LogHelper.i(TAG, "onDestroyView");
        //通知宿主本fragment销毁
        mListener.creditDestroy();
    }


    boolean verification() {
        // Reset errors.
        etQuota.setError(null);
        etCompany.setError(null);

        //校验融资公司
        String company = etCompany.getText().toString();
        if (company.isEmpty()) {
            LogHelper.w(TAG, "轿车类型为空");
            etCompany.setError("融资公司无效");
            return false;
        }

        String quota = etQuota.getText().toString();
        if (TextUtils.isEmpty(quota)) {
            etQuota.setError(getString(R.string.error_invalid_quota));
            etQuota.requestFocus();
            return false;
        }
        if (Integer.valueOf(quota) > carPrice || Integer.valueOf(quota) <= 0) {
            etQuota.setError("融资额度最多为轿车总价的70%");
            etQuota.requestFocus();
            return false;
        }

        if (!cbCredit.isChecked()) {
            AlertDialog.Builder diag = new AlertDialog.Builder(getActivity());
            diag.setTitle("未同意《三方协议》");
            diag.setMessage("只有同意《三方协议》才能提交订单");
            diag.show();
            return false;
        }

        return true;
    }

    /*
    * 提交车单
    */
    Map<String, String> getParas() {

        //获取标单数据
        String creditCompany_id = String.valueOf(company.getId());
        String quota = etQuota.getText().toString();

        //设置URl
        String url = ApiConfig.api_url + ApiConfig.SUBMIT_ORDER_URL;

        //添加POST参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("creditCompany_id", creditCompany_id);
        params.put("quota", quota);

        return params;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCreditDestroyListener {
        void creditDestroy();
    }
}
