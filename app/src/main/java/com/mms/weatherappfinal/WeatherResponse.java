package com.mms.weatherappfinal;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {


    @SerializedName("coord")
    public Coordinates coordinates;

    @SerializedName("main")
    public MainWeatherInfo mainInfo;

    // Add other fields as needed

    public static class Coordinates {
        @SerializedName("lat")
        public double latitude;

        @SerializedName("lon")
        public double longitude;
    }

    public static class MainWeatherInfo {
        @SerializedName("temp")
        public double temperature;

        @SerializedName("humidity")
        public double humidity;

        @SerializedName("pressure")
        public double pressure;

        // Add other fields as needed
    }


}
