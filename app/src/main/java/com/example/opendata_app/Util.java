package com.example.opendata_app;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

//假資料
public class Util {
    public static String URL_randomPlace = "http://120.119.77.72:8081/api/getRandomPlace";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Data getData(){
        List<BeachData> mList = new ArrayList<>();
        mList.add(new BeachData("龍門公園","1","0","0","1","0","1","beachpark16_1","beachpark","新北市"));
        mList.add(new BeachData("頭城","0","1","1","0","1","1","beachpark1_1","beachpark","宜蘭縣"));
        mList.add(new BeachData("翡翠灣","1","1","0","1","1","0","beachpark15_1","beachpark","新北市"));
        mList.add(new BeachData("崎頂","1","0","1","0","1","0","beachpark6_1","beachpark","苗栗縣"));
        mList.add(new BeachData("磯崎","0","0","1","1","1","1","beachpark2_1","beachpark","花蓮縣"));
        return new Data(mList);
    }
}
