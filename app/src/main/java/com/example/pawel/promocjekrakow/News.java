package com.example.pawel.promocjekrakow;

import android.os.Parcel;
import android.os.Parcelable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Pawel on 27.05.2017.
 */

public class News {
     String mTitle;
     String mPrice;
      String mUrl;
      String mLoc;
     String mStrPrice;
     String mDesc;


    public News(String title, String price,String url,String loc ,String StrPrice,String Desc) {
        mTitle = title;
        mPrice = price;
        mUrl = url;
        mLoc = loc;
        mStrPrice = StrPrice;
        mDesc = Desc;
    }
    public String getUrl() {
        return mUrl;
    }
    
}