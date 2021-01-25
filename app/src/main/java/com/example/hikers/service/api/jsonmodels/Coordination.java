package com.example.hikers.service.api.jsonmodels;

import com.google.gson.annotations.SerializedName;

public class Coordination {

    @SerializedName("lon")
    private double lon;

    @SerializedName("lat")
    private double lat;

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    @Override
    public String toString() {
        return
                "Coordination{" +
                        "lon = '" + lon + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}