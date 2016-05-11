package cn.jdywl.driver.config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wuwantao on 15/5/1.
 */
public enum CarType {
    //以下是枚举成员
    @SerializedName("0")
    CAR_BIGSUV(0),
    @SerializedName("1")
    CAR_SUV(1),
    @SerializedName("2")
    CAR_CAR(2);

    private int cartype;

    // 构造函数，枚举类型只能为私有
    CarType(int _cartype) {
        this.cartype = _cartype;
    }

    @Override
    public String toString() {
        switch (this.cartype)
        {
            case 0:
                return "大型SUV";
            case 1:
                return "标准SUV";
            case 2:
                return "标准轿车";
            default:
                return "车型错误";
        }
    }
}
