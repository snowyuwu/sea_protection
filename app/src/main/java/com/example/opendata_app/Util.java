package com.example.opendata_app;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Data getData(){
        List<BeachData> mList = new ArrayList<>();
        mList.add(new BeachData( R.drawable.zhongao,"中澳沙灘","小琉球"));
        mList.add(new BeachData( R.drawable.waiao,"外澳沙灘","宜蘭"));
        mList.add(new BeachData( R.drawable.white,"白沙灣海水浴場","新北市"));
        mList.add(new BeachData( R.drawable.zhongao,"中澳沙灘","小琉球"));
        mList.add(new BeachData( R.drawable.waiao,"外澳沙灘","宜蘭"));
        mList.add(new BeachData( R.drawable.white,"白沙灣海水浴場","新北市"));
        mList.add(new BeachData( R.drawable.zhongao,"中澳沙灘","小琉球"));
        mList.add(new BeachData( R.drawable.waiao,"外澳沙灘","宜蘭"));
        mList.add(new BeachData( R.drawable.white,"白沙灣海水浴場","新北市"));
        mList.add(new BeachData( R.drawable.white,"白沙灣海水浴場","新北市"));


        return new Data(mList);
    }
}
