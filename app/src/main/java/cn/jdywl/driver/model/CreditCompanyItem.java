
package cn.jdywl.driver.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CreditCompanyItem implements Parcelable {


    /**
     * id : 1
     * company : 北京搬运工汽车科技有限公司
     * contacter : 郑海涛
     * phone : 13241101109
     * bank : 中国建设银行北京天通苑支行
     * accountName : 北京搬运工汽车科技有限公司
     * accountNo : 11001029700059000644
     * introduce : 贷款利息：10万以下:70元/天。10万－15万:100元/天。15万－20万:140元/天。20万－25万:170元/天。25万－30万:210元/天。30万－40万:280元/天。40万－50万:350元/天。50万－70万:490元/天。70万－100万:700元/天。
     */

    @SerializedName("id")
    private int id;
    @SerializedName("company")
    private String company;
    @SerializedName("contacter")
    private String contacter;
    @SerializedName("phone")
    private String phone;
    @SerializedName("bank")
    private String bank;
    @SerializedName("accountName")
    private String accountName;
    @SerializedName("accountNo")
    private String accountNo;
    @SerializedName("introduce")
    private String introduce;
    @SerializedName("interest")
    private String interest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContacter() {
        return contacter;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.company);
        dest.writeString(this.contacter);
        dest.writeString(this.phone);
        dest.writeString(this.bank);
        dest.writeString(this.accountName);
        dest.writeString(this.accountNo);
        dest.writeString(this.introduce);
        dest.writeString(this.interest);
    }

    public CreditCompanyItem() {
    }

    protected CreditCompanyItem(Parcel in) {
        this.id = in.readInt();
        this.company = in.readString();
        this.contacter = in.readString();
        this.phone = in.readString();
        this.bank = in.readString();
        this.accountName = in.readString();
        this.accountNo = in.readString();
        this.introduce = in.readString();
        this.interest = in.readString();
    }

    public static final Parcelable.Creator<CreditCompanyItem> CREATOR = new Parcelable.Creator<CreditCompanyItem>() {
        @Override
        public CreditCompanyItem createFromParcel(Parcel source) {
            return new CreditCompanyItem(source);
        }

        @Override
        public CreditCompanyItem[] newArray(int size) {
            return new CreditCompanyItem[size];
        }
    };
}
