package com.nomercy.meetly.Model;

import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String placeName;

    @SerializedName("photo")
    String photo;

    @SerializedName("adress")
    String adress;

    @SerializedName("description")
    private String placeDescription;
    private int placeImage;




    public Place(int id, String placeName, String placeDescription, int placeImage){
        this.id = id;
        this.placeName=placeName;
        this.placeDescription = placeDescription;
        this.placeImage = placeImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public void setPlaceName(String name) {
        this.placeName = name;
    }

    public String getPlaceDescription() {
        return this.placeDescription;
    }

    public void setPlaceDescription(String company) {
        this.placeDescription = company;
    }

    public int getPlaceImage() {
        return this.placeImage;
    }

    public void setPlaceImage(int image) {
        this.placeImage = image;
    }
}
