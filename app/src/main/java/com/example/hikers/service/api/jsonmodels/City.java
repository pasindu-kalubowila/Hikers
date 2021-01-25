package com.example.hikers.service.api.jsonmodels;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("country")
    private String country;

    @SerializedName("coordination")
    private Coordination coordination;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCoordination(Coordination coordination) {
        this.coordination = coordination;
    }

    public Coordination getCoordination() {
        return coordination;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return
                "City{" + "country = '" + country + '\'' + ",coordination = '" + coordination + '\'' + ",name = '" + name + '\'' + ",id = '" + id + '\'' + "}";
    }
}