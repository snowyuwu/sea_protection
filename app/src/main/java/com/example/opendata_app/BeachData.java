package com.example.opendata_app;

public class BeachData {

    //這個是一個地方，例如：隨便一個沿海地區的資料(溫度、濕度、降雨率等)的資料，它裡面有什麼資料就要建對應的變數，然後下面get()它
    private String beachName;
    private String location;
    private int viewPicture;

    public BeachData(int viewPicture, String beachName, String location){
        this.viewPicture = viewPicture;
        this.beachName = beachName;
        this.location = location;
    }

    public int getView(){ return viewPicture;}
    public String getName(){ return beachName;}
    public String getLocation(){ return location;}
}
