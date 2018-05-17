package com.lifeistech.android.projectmanager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class FeedbackAdapter extends ArrayAdapter<Project> {

    private LayoutInflater layoutinflater;

    FeedbackAdapter(Context context, int textViewResourceId, List<Project> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Project detail = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(com.lifeistech.android.projectmanager.R.layout.project_layout, null);
        }

        TextView dateText = (TextView)convertView.findViewById(com.lifeistech.android.projectmanager.R.id.date);
        TextView fb_titleText = (TextView) convertView.findViewById(com.lifeistech.android.projectmanager.R.id.fb_titleText);
        RatingBar satisfaction = (RatingBar) convertView.findViewById(com.lifeistech.android.projectmanager.R.id.satisfaction);
        satisfaction.setNumStars(5);

        dateText.setText(detail.logdate);
        fb_titleText.setText(detail.fb_title);
        satisfaction.setRating(detail.satisfaction);

        satisfaction.setIsIndicator(true);

        return convertView;
    }
}