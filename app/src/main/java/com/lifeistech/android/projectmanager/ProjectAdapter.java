package com.lifeistech.android.projectmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import android.widget.ProgressBar;

public class ProjectAdapter extends ArrayAdapter<Project> {


    private LayoutInflater layoutinflater;

    ProjectAdapter(Context context, int textViewResourceId, List<Project> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Project detail = getItem(position);

        if (convertView == null) {
            convertView = layoutinflater.inflate(R.layout.project_title_layout, null);
        }

        TextView titleText = (TextView) convertView.findViewById(R.id.commentText);
        ProgressBar progressbar = (ProgressBar) convertView.findViewById(R.id.progressBar2);

        titleText.setText(detail.title);

        progressbar.setMax(100);
        progressbar.setProgress(detail.achievement);

        return convertView;
    }
}
