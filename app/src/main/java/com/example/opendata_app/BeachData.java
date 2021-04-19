package com.example.opendata_app;

public class BeachData {

    //這個是一個地方，例如：隨便一個沿海地區的資料(溫度、濕度、降雨率等)的資料，它裡面有什麼資料就要建對應的變數，然後下面get()它
    private String Area,Swim,Diving,Surfing,JetSki,BananaBoat,AquaBoard,image,city;

    public BeachData(String Area,String Swim,String Diving,String Surfing,String JetSki,String BananaBoat, String AquaBoard,String image, String city){
        this.Area = Area;
        this.Swim = Swim;
        this.Diving = Diving;
        this.Surfing = Surfing;
        this.JetSki = JetSki;
        this.BananaBoat = BananaBoat;
        this.AquaBoard = AquaBoard;
        this.image = image;
        this.city = city;
    }

    public String getArea(){ return Area;}
    public String getSwim(){ return Swim;}
    public String getDiving(){ return Diving;}
    public String getSurfing(){ return Surfing;}
    public String getJetSki(){ return JetSki;}
    public String getBananaBoat(){ return BananaBoat;}
    public String getAquaBoard(){ return AquaBoard;}
    public String getImage(){ return image;}
    public String getCity(){ return city;}
}
