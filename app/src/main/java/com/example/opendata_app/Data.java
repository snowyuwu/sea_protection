package com.example.opendata_app;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<BeachData> mList = new ArrayList<>();

    public Data(List<BeachData> mList) {
        this.mList = mList;
    }

    public List<BeachData> getAllData() {
        return mList;
    }

    public void setList(List<BeachData> mList) {
        this.mList = mList;
    }
}
