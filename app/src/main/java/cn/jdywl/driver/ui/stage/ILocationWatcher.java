package cn.jdywl.driver.ui.stage;

import com.baidu.location.BDLocation;

/**
 * Created by xycong on 2016/5/7.
 */
public interface ILocationWatcher {
    void onLocationSuccess(BDLocation location);
}
