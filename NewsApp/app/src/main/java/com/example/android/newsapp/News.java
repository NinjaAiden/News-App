package com.example.android.newsapp;

/**
 * Created by Aiden on 13/05/2018.
 */
public class News {

    private String mTitle, mDate, mUrl, mSection, mAuthor;

    public News(String title, String date, String url, String section, String author){
        mTitle = title;
        mDate = date;
        mUrl = url;
        mSection = section;
        mAuthor = author;
    }

    public String getTitle()    {return mTitle;}
    public String getDate()     {return mDate;}
    public String getUrl()      {return mUrl;}
    public String getSection()  {return mSection;}
    public String getAuthor()   {return mAuthor;}
}
