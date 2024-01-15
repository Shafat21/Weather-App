package com.mms.weatherappfinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("otp.php")
    Call<OTPResponse> requestOTP(@Query("mobileNumber") String mobileNumber);

    @GET("verify.php")
    Call<VerifyResponse> verifyOTP(@Query("Otp") String otp, @Query("referenceNo") String referenceNo);
}
