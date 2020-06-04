package com.smartclick.custompushnotifications.models;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {
    @SerializedName("timezone")
    public String timezone;

    @SerializedName("timezone_offset")
    public String timezone_offset;
}
