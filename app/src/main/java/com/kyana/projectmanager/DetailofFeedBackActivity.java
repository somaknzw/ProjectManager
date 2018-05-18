package com.kyana.projectmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import io.realm.Realm;

public class DetailofFeedBackActivity extends AppCompatActivity {

    public Realm realm;
    public TextView text_fbtitle;
    public TextView text_fbcommnet;
    public TextView text_satisfaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kyana.projectmanager.R.layout.activity_detailfeedback);

        //realmを開く
        realm = realm.getDefaultInstance();

        text_fbtitle = (TextView) findViewById(com.kyana.projectmanager.R.id.text_fbtitle);
        text_fbcommnet = (TextView) findViewById(com.kyana.projectmanager.R.id.text_fbcmment);
        text_satisfaction = (TextView)findViewById(com.kyana.projectmanager.R.id.text_stf);
        showData();


        setTitle(getIntent().getStringExtra("date"));

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    public void showData() {
        final Project detail = realm.where(Project.class).equalTo("dayid",
                getIntent().getStringExtra("dayid")).findFirst();

        text_fbtitle.setText(detail.fb_title);
        text_fbcommnet.setText(detail.comment);
        text_satisfaction.setText(String.valueOf(detail.satisfaction)+" / 5.0");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }


}
