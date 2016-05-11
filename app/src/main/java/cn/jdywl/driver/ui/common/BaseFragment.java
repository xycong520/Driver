package cn.jdywl.driver.ui.common;


import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;

import cn.jdywl.driver.app.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStop() {
        super.onStop();
        //取消volley 连接
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        if (queue != null) {
            cancelVolleyRequest(queue);
        }
    }

    protected abstract void cancelVolleyRequest(RequestQueue queue);

}
