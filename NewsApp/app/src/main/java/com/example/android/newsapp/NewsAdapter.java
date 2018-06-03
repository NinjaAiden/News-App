package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aiden on 13/05/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, List<News> news){
        super(context, 0, news);
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNewsStory = getItem(position);

        TextView titleTv = (TextView)listItemView.findViewById(R.id.tvTitle);
        String title = currentNewsStory.getTitle();
        titleTv.setText(title);


        TextView dateTv = (TextView)listItemView.findViewById(R.id.tvDate);
        dateTv.setText(FormatDate(currentNewsStory.getDate()));

        TextView sectionTv = (TextView)listItemView.findViewById(R.id.tvSection);
        String section = currentNewsStory.getSection();
        sectionTv.setText(section);

        TextView authorTv = (TextView)listItemView.findViewById(R.id.tvAuthor);
        String author = currentNewsStory.getAuthor();
        authorTv.setText(author);

        return listItemView;
    }

    private String FormatDate(String inputDate){
        String day = inputDate.substring(8, 10);
        String month = inputDate.substring(4, 8);
        String year = inputDate.substring(0, 4);
        String time = inputDate.substring(11, 19);
        return day + month + year + " " + time;
    }
}
