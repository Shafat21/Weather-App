package com.mms.weatherappfinal;
import com.google.gson.annotations.SerializedName;
public class OTPResponse {
    @SerializedName("referenceNo")
    private String referenceNo;

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
